package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionAdapter;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionItem;
import com.example.client_zhihu_fsr.ReturnData.QuestionListReturnData;
import com.example.client_zhihu_fsr.ReturnData.SingleQuestionData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineQuestionActivity extends AppCompatActivity {

    private List<QuestionItem> questionItemList = new ArrayList<>();
    private QuestionListReturnData questionListReturnData;
    private RecyclerView myQuestionRecyclerView;
    private QuestionAdapter myQuestionAdapter;
    private TextView textViewMyQuestion;

    private SwipeRefreshLayout swipeRefreshLayout;
    private String token;
    private String originAddress = "http://42.192.88.213:8080/api/questions/list?userID=";
    private String NewAddress ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_question);

        initView();
        initEvent();
        sendRequestWithQuestionList();
        myQuestionAdapter = new QuestionAdapter(questionItemList);
        myQuestionRecyclerView.setAdapter(myQuestionAdapter);
        myQuestionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendRequestWithQuestionList();
                //  mQuestionAdapter = new QuestionAdapter(questionItemList);

                myQuestionAdapter.notifyDataSetChanged();
//                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }



    //初始化控件方法
    private void initView() {
        textViewMyQuestion = (TextView) findViewById(R.id.tvMyQuestion) ;
        myQuestionRecyclerView = (RecyclerView) findViewById(R.id.rvMyQuestionsList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }


    //注册事件方法
    private void initEvent() {

        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myQuestionRecyclerView.setLayoutManager(layoutManager);

        //token
        SharedPreferences sp = getSharedPreferences("loginToken",0);
        token = sp.getString("token","");
        int uid = sp.getInt("uid",0);

        //IP初始化
        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(uid);
        Address.append("&order=create_time");
        NewAddress = new String(Address);
    }



    private void sendRequestWithQuestionList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("authorization", token)
                            .url(NewAddress)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("MineActivity","responseData is "+responseData);
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }


    private void parseJSONWithJSONObject(String jsonData){

        Gson gson = new Gson();
        Type type = new TypeToken<QuestionListReturnData>() {}.getType();
        questionListReturnData = gson.fromJson(jsonData, type);
        Log.d("MineQuestionActivity", "questionListReturnData is " + questionListReturnData.toString());
        List<SingleQuestionData> questionsList = questionListReturnData.getData();//问题列表提取成功

        questionItemList.clear();
        for(int i = 0; i< questionListReturnData.getTotal(); i++){

            QuestionItem questionItem = new QuestionItem(questionsList.get(i).getQuestioner().getId() , questionsList.get(i).getId(),questionsList.get(i).getTitle(),R.drawable.head,questionsList.get(i).getQuestioner().getName(),questionsList.get(i).getDesc(),questionsList.get(i).getViewCount(),questionsList.get(i).getAnswersCount());
            questionItemList.add(questionItem);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(questionListReturnData.getMessage().equals("success")) {
                  //  Toast.makeText(MineQuestionActivity.this, "我的提问列表加载成功", Toast.LENGTH_SHORT).show();
                    myQuestionAdapter.notifyDataSetChanged();
                }

            }
        });
    }


}