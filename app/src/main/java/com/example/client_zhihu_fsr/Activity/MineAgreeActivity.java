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
import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerAdapter;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerItem;
import com.example.client_zhihu_fsr.ReturnData.AnswersListReturnData;
import com.example.client_zhihu_fsr.ReturnData.SingleAnswerData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineAgreeActivity extends AppCompatActivity {

    private List<AnswerItem> answerItemList = new ArrayList<>();
    private AnswersListReturnData answersListReturnData;
    private RecyclerView myAgreeRecyclerView;
    private AnswerAdapter myAnswerAdapter;
    private TextView textViewMyAgree;

    private SwipeRefreshLayout swipeRefreshLayout;
    private String token;
    private String originAddress = "http://42.192.88.213:8080/api/answers/listByVoter?voterID=";
    private String NewAddress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_agree);

        initView();
        initEvent();
        sendRequestWithAgreeList();
        myAnswerAdapter = new AnswerAdapter(answerItemList);
        myAgreeRecyclerView.setAdapter(myAnswerAdapter);
        myAgreeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendRequestWithAgreeList();
                //  mQuestionAdapter = new QuestionAdapter(questionItemList);

                myAnswerAdapter.notifyDataSetChanged();
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
        textViewMyAgree = (TextView) findViewById(R.id.tvMyAgree) ;
        myAgreeRecyclerView = (RecyclerView) findViewById(R.id.rvMyAgreeList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }


    //注册事件方法
    private void initEvent() {

        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myAgreeRecyclerView.setLayoutManager(layoutManager);

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



    private void sendRequestWithAgreeList(){
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
        Type type = new TypeToken<AnswersListReturnData>() {}.getType();
        answersListReturnData = gson.fromJson(jsonData, type);
        Log.d("MineAgreeActivity", "answersListReturnData is " + answersListReturnData.toString());
        List<SingleAnswerData> answersList = answersListReturnData.getData();//问题列表提取成功

        answerItemList.clear();
        for(int i = 0; i< answersListReturnData.getTotal(); i++){

            AnswerItem answerItem = new AnswerItem(answersList.get(i).getId(), answersList.get(i).getAnswerer().getId(),answersList.get(i).getAnswerer().getName(),R.drawable.head,answersList.get(i).getContent(),answersList.get(i).getSupportersCount(),answersList.get(i).getVoted());//content为后端的回答，等于answer
            answerItemList.add(answerItem);
            Log.d("MineAgreeActivity"," answerItemList.toString() "+answerItem.getAnswer());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(answersListReturnData.getMessage().equals("success")) {
                    //Toast.makeText(MineAgreeActivity.this, "我的回答列表加载成功", Toast.LENGTH_LONG).show();
                    myAnswerAdapter.notifyDataSetChanged();
                }

            }
        });
    }




}