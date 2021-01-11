package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AnswersListActivity extends AppCompatActivity {

    private List<AnswerItem> answerItemList = new ArrayList<>();
    private TextView textViewTitle;
    private TextView textViewViews;
    private TextView textViewAnswers;
    private RecyclerView mAnswerRecyclerView;
    private String originAddress = "http://42.192.88.213:8080/api/answers/listByQuestion?questionID=";
    private String timeOrderAddress = "&order=create_time";
    private String supportersOrderAddress = "&order=create_time";
    private String NewAddress;//最终的目标地址
    private AnswersListReturnData answersListReturnData;
    private AnswerAdapter mAnswerAdapter;
    private int questionId;
    private String questionTitle;
    private int answersCount;
    private int viewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_list);
        initView();
        initEvent();
        initDataIn();
        sendRequestWithQuestionList();

        mAnswerAdapter = new AnswerAdapter(answerItemList);
        mAnswerRecyclerView.setAdapter(mAnswerAdapter);
        mAnswerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线
    }


    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.tvTitle);
        textViewViews = (TextView) findViewById(R.id.tvViews);
        textViewAnswers = (TextView) findViewById(R.id.tvAnswers);
        mAnswerRecyclerView = (RecyclerView) findViewById(R.id.rvAnswersList);
    }


    //注册事件方法
    private void initEvent() {

        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAnswerRecyclerView.setLayoutManager(layoutManager);
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extraQuestionId",1);
        questionTitle = intent.getStringExtra("extraTitle");
        viewCount = intent.getIntExtra("extraViewCount",1);
        answersCount = intent.getIntExtra("extraAnswersCount",1);

        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(questionId);
        Address.append(timeOrderAddress);//按发布时间排序
        NewAddress = new String(Address);
        Log.d("AnswersListActivity","NewAddress is "+NewAddress);
    }




    private void sendRequestWithQuestionList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(NewAddress)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswersListActivity","responseData is "+responseData);
                    parseJSONWithGson(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }


    private void parseJSONWithGson(String jsonData){

        Gson gson = new Gson();
        Type type = new TypeToken<AnswersListReturnData>() {}.getType();
        answersListReturnData = gson.fromJson(jsonData, type);
        Log.d("AnswersListActivity", "answersListReturnData is " + answersListReturnData.toString());
        List<SingleAnswerData> answersList = answersListReturnData.getData();//问题列表提取成功

        for(int i = 0; i< answersListReturnData.getTotal(); i++){
            AnswerItem answerItem = new AnswerItem(answersList.get(i).getAnswerer().getName(),R.drawable.head,answersList.get(i).getContent(),answersList.get(i).getAnswerer().getId());//content为后端的回答，等于answer
            answerItemList.add(answerItem);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(answersListReturnData.getMessage().equals("success")) {
                    textViewTitle.setText(questionTitle);
                    textViewViews.setText(viewCount+"浏览");
                    textViewAnswers.setText(answersCount+"回答");
                    Toast.makeText(AnswersListActivity.this, "回答列表加载成功", Toast.LENGTH_LONG).show();
                }

            }
        });
    }












}