package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.HotQuestionAdapter;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.HotQuestionItem;
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

public class HotQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private List<HotQuestionItem> hotQuestionItemList = new ArrayList<>();
    private  Button buttonMine;
    private Button buttonPublish;
    private Button buttonHomePage;
    private Button buttonHotQuestion;
    private Button buttonRecommendation;
    private RecyclerView mHotQuestionRecyclerView;
    private QuestionListReturnData questionListReturnData;
    private String originAddress = "http://42.192.88.213:8080/api/questions/topQ";
    private HotQuestionAdapter mHotQuestionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_question);
        initView();
        initEvent();
        sendRequestWithQuestionList();
        mHotQuestionAdapter = new HotQuestionAdapter(hotQuestionItemList);
        mHotQuestionRecyclerView.setAdapter(mHotQuestionAdapter);
        mHotQuestionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hotQuestionItemList.clear();
                sendRequestWithQuestionList();
                mHotQuestionAdapter = new HotQuestionAdapter(hotQuestionItemList);

                mHotQuestionAdapter.notifyDataSetChanged();
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
        mHotQuestionRecyclerView = (RecyclerView) findViewById(R.id.rvQuestion);
        buttonHomePage = (Button)findViewById(R.id.btHomePage);
        buttonHotQuestion = (Button)findViewById(R.id.btHot);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        buttonRecommendation = (Button)findViewById(R.id.btRecommendation);
    }


    //注册事件方法
    private void initEvent() {
        buttonMine.setOnClickListener(this);
        buttonPublish.setOnClickListener(this);
        buttonHomePage.setOnClickListener(this);
        buttonHotQuestion.setOnClickListener(this);
        buttonRecommendation.setOnClickListener(this);
        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mHotQuestionRecyclerView.setLayoutManager(layoutManager);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMine :
                Intent intentMine =new Intent(HotQuestionActivity.this, MineActivity.class);
                startActivityForResult(intentMine,2);
                break;

            case R.id.btPublish :
                Intent intentPublish = new Intent(HotQuestionActivity.this, PublishActivity.class);
                startActivityForResult(intentPublish,1);
                break;

            case R.id.btHomePage :
                break;

            case R.id.btRecommendation:
                Intent intent = new Intent(HotQuestionActivity.this, HomeActivity.class);
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
                    Log.d("HotActivity","responseData is "+responseData);
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
        Log.d("HotActivity", "hotReturnData is " + questionListReturnData.toString());

        List<SingleQuestionData> questionsList = questionListReturnData.getData();//问题列表提取成功


        for(int i = 0; i< 10; i++){
            HotQuestionItem hotQuestionItem = new HotQuestionItem(questionsList.get(i).getQuestioner().getId() , questionsList.get(i).getId(),questionsList.get(i).getTitle(),R.drawable.head,questionsList.get(i).getQuestioner().getName(),questionsList.get(i).getDesc(),questionsList.get(i).getViewCount(),questionsList.get(i).getAnswersCount(),questionsList.get(i).getHot(),i+1);
            hotQuestionItemList.add(hotQuestionItem);
        }

         Handler(questionListReturnData);
    }

    private void Handler(QuestionListReturnData questionListReturnData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(questionListReturnData.getMessage().equals("success")) {
                    //Toast.makeText(HotQuestionActivity.this, "热门问题列表加载成功", Toast.LENGTH_LONG).show();
                    mHotQuestionAdapter.notifyDataSetChanged();
                }

            }
        });
    }


    //    protected void onResume() {}


    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://publish
                break;
            case 2://mine
                break;
            default:
                break;
        }
    }





}