package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionItem;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionAdapter;
import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.SingleQuestionData;
import com.example.client_zhihu_fsr.ReturnData.QuestionListReturnData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private List<QuestionItem> questionItemList = new ArrayList<>();
    private  Button buttonMine;
    private Button buttonPublish;
    private Button buttonHomePage;
    private Button buttonHotQuestion;
    private RecyclerView mQuestionRecyclerView;
    private QuestionListReturnData questionListReturnData;
    private String originAddress = "http://42.192.88.213:8080/api/questions/list?order=create_time";
    private QuestionAdapter mQuestionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initEvent();
        sendRequestWithQuestionList();

    mQuestionAdapter = new QuestionAdapter(questionItemList);
    mQuestionRecyclerView.setAdapter(mQuestionAdapter);
    mQuestionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线





        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionItemList.clear();
                sendRequestWithQuestionList();
              //  mQuestionAdapter = new QuestionAdapter(questionItemList);

                mQuestionAdapter.notifyDataSetChanged();
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
        buttonMine = (Button)findViewById(R.id.btMine);
        buttonPublish = (Button)findViewById(R.id.btPublish);
        mQuestionRecyclerView = (RecyclerView) findViewById(R.id.rvQuestion);
        buttonHomePage = (Button)findViewById(R.id.btHomePage);
        buttonHotQuestion = (Button)findViewById(R.id.btHot);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }


    //注册事件方法
    private void initEvent() {
        buttonMine.setOnClickListener(this);
        buttonPublish.setOnClickListener(this);
        buttonHomePage.setOnClickListener(this);
        buttonHotQuestion.setOnClickListener(this);
        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mQuestionRecyclerView.setLayoutManager(layoutManager);
    }



    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        Boolean signed = sp.getBoolean("Signed", false);
        Log.d("HomeActivity","signed = "+signed);

        switch (v.getId()) {
            case R.id.btMine :
                if(signed){
                    Intent intentMine =new Intent(HomeActivity.this, MineActivity.class);
                    startActivityForResult(intentMine,2);
                }else {
                    Intent intentMain =new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    Toast.makeText(HomeActivity.this, "请先登录!", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btPublish :
                if(signed){
                    Intent intentPublish = new Intent(HomeActivity.this, PublishActivity.class);
                    startActivityForResult(intentPublish,305);
                }else {
                    Intent intentMain =new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    Toast.makeText(HomeActivity.this, "请先登录!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btHomePage :
                break;

            case R.id.btHot:
                Intent intent = new Intent(HomeActivity.this, HotQuestionActivity.class);
                startActivity(intent);

            default:
                break;
        }
    }





    private void sendRequestWithQuestionList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(originAddress)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("HomeActivity","responseData is "+responseData);
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
        java.lang.reflect.Type type = new TypeToken<QuestionListReturnData>() {}.getType();
        questionListReturnData = gson.fromJson(jsonData, type);
        Log.d("HomeActivity", "homeReturnData is " + questionListReturnData.toString());

        List<SingleQuestionData> questionsList = questionListReturnData.getData();//问题列表提取成功


        for(int i = 0; i< questionListReturnData.getTotal(); i++){
            QuestionItem questionItem = new QuestionItem(questionsList.get(i).getQuestioner().getId() , questionsList.get(i).getId(),questionsList.get(i).getTitle(),R.drawable.head,questionsList.get(i).getQuestioner().getName(),questionsList.get(i).getDesc(),questionsList.get(i).getViewCount(),questionsList.get(i).getAnswersCount());
            questionItemList.add(questionItem);
        }

         Handler(questionListReturnData);
    }

    private void Handler(QuestionListReturnData questionListReturnData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(questionListReturnData.getMessage().equals("success")) {
                   // Toast.makeText(HomeActivity.this, "问题列表加载成功", Toast.LENGTH_SHORT).show();
                    mQuestionAdapter.notifyDataSetChanged();
                }

            }
        });
    }


    //    protected void onResume() {}


    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 305://publish
                if(resultCode==RESULT_OK){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            questionItemList.clear();
                            sendRequestWithQuestionList();
                            mQuestionAdapter.notifyDataSetChanged();
                        }
                    });
                }

                break;
            case 2://mine
                break;
            default:
                break;
        }
    }





}