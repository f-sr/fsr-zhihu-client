package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerAdapter;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerItem;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionItem;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionAdapter;
import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.AnswersListReturnData;
import com.example.client_zhihu_fsr.ReturnData.HomeReturnData;
import com.example.client_zhihu_fsr.ReturnData.SingleAnswerData;
import com.example.client_zhihu_fsr.ReturnData.SingleQuestionData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineAnswerActivity extends AppCompatActivity implements View.OnClickListener{

    private List<AnswerItem> answerItemList = new ArrayList<>();
    private AnswersListReturnData answersListReturnData;
    private RecyclerView myAnswerRecyclerView;
    private AnswerAdapter myAnswerAdapter;


    private Button button_homePage;
    private Button buttonQuestion;
    private Button buttonAnswer;
    private Button buttonAgree;
    private Button button_editMaterials;
    private TextView textView_userName;





    private SwipeRefreshLayout swipeRefreshLayout;
    private String token;
    private String originAddress = "http://42.192.88.213:8080/api/answers/listByUser?userID=";
    private String NewAddress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        initEvent();
        sendRequestWithAnswerList();
        myAnswerAdapter = new AnswerAdapter(answerItemList);
        myAnswerRecyclerView.setAdapter(myAnswerAdapter);
        myAnswerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendRequestWithAnswerList();
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
        button_homePage = (Button)findViewById(R.id.bt_HomePage) ;
        buttonQuestion = (Button)findViewById(R.id.btQuestion);
        buttonAnswer = (Button)findViewById(R.id.btAnswer);
        buttonAgree = (Button) findViewById(R.id.btAgree);
        button_editMaterials = (Button) findViewById(R.id.bt_EditMaterials);
        textView_userName = (TextView) findViewById(R.id.tv_UserName);
        myAnswerRecyclerView = (RecyclerView) findViewById(R.id.rvMyQuestion);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }


    //注册事件方法
    private void initEvent() {

        button_homePage.setOnClickListener(this);
        buttonQuestion.setOnClickListener(this);
        buttonAnswer.setOnClickListener(this);
        buttonAgree.setOnClickListener(this);

        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myAnswerRecyclerView.setLayoutManager(layoutManager);

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


    @Override
    //实现onClick方法
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_HomePage:
                Intent intentHome = new Intent(MineAnswerActivity.this, HomeActivity.class);
                setResult(RESULT_OK, intentHome);

                startActivity(intentHome);
                break;

            case R.id.btQuestion:
                Intent intentQuestion = new Intent(MineAnswerActivity.this, MineActivity.class);
                startActivity(intentQuestion);
                break;

            case R.id.btAnswer:

                break;

            case R.id.btAgree:
                break;



            default:
                break;
        }
    }

    private void sendRequestWithAnswerList(){
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
        Log.d("AnswersListActivity", "answersListReturnData is " + answersListReturnData.toString());
        List<SingleAnswerData> answersList = answersListReturnData.getData();//问题列表提取成功

        answerItemList.clear();
        for(int i = 0; i< answersListReturnData.getTotal(); i++){

            AnswerItem answerItem = new AnswerItem(answersList.get(i).getId(), answersList.get(i).getAnswerer().getId(),answersList.get(i).getAnswerer().getName(),R.drawable.head,answersList.get(i).getContent(),answersList.get(i).getSupportersCount(),answersList.get(i).getVoted());//content为后端的回答，等于answer
            answerItemList.add(answerItem);
            Log.d("AnswersListActivity"," answerItemList.toString() "+answerItem.getAnswer());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(answersListReturnData.getMessage().equals("success")) {
                    Toast.makeText(MineAnswerActivity.this, "我的回答列表加载成功", Toast.LENGTH_LONG).show();
                }

            }
        });
    }






}