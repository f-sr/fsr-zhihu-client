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

import com.example.client_zhihu_fsr.RecyclerViewAdapter.Content;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.ContentAdapter;
import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.SingleQuestionData;
import com.example.client_zhihu_fsr.ReturnData.HomeReturnData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Content> contentList = new ArrayList<>();
    private  Button button_Mine;
    private Button button_Publish;
    private RecyclerView recyclerView;
    private HomeReturnData homeReturn_data;
    private Button buttonHomePage;
    private String originAddress = "http://42.192.88.213:8080/api/questions/list?order=create_time";
    private ContentAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initEvent();
        sendRequestWithHttpURLConnection();
        adapter = new ContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentList.clear();
                sendRequestWithHttpURLConnection();
                adapter = new ContentAdapter(contentList);

                adapter.notifyDataSetChanged();
//                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });

    }

    //初始化控件方法
    private void initView() {
        button_Mine = (Button)findViewById(R.id.bt_Mine);
        button_Publish = (Button)findViewById(R.id.bt_Publish);
        recyclerView = (RecyclerView) findViewById(R.id.rv_All);
        buttonHomePage = (Button)findViewById(R.id.bt_HomePage);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }


    //注册事件方法
    private void initEvent() {
        button_Mine.setOnClickListener(this);
        button_Publish.setOnClickListener(this);
        buttonHomePage.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }





    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Mine :
                Intent intentMine =new Intent(HomeActivity.this, MineActivity.class);
                startActivityForResult(intentMine,2);
                break;


            case R.id.bt_Publish :
                Intent intentPublish = new Intent(HomeActivity.this, PublishActivity.class);
                startActivityForResult(intentPublish,1);
                break;

            case R.id.bt_HomePage :

                break;



            default:
                break;
        }
    }





    private void sendRequestWithHttpURLConnection(){
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
        java.lang.reflect.Type type = new TypeToken<HomeReturnData>() {}.getType();
        homeReturn_data = gson.fromJson(jsonData, type);

        List<SingleQuestionData> list_questions = homeReturn_data.getList_data();//问题列表提取成功

//        Log.d("HomeActivity","jsonData is "+jsonData);
//        Log.d("HomeActivity", "message is " + homeReturn_data.getMessage());
//
//        Log.d("HomeActivity", "list_questions[1].Title =  " + list_questions.get(5).getTitle());
//        Log.d("HomeActivity", "list_questions[1].userName=  " + list_questions.get(5).getQuestioner().getName());
//        Log.d("HomeActivity", "list_questions[1].desc =  " + list_questions.get(5).getDesc());


        Log.d("HomeActivity", "Total is " + homeReturn_data.getTotal());

        for(int i=0;i<homeReturn_data.getTotal();i++){
            Content question = new Content(list_questions.get(i).getQuestioner().getId() , list_questions.get(i).getId(),list_questions.get(i).getTitle(),R.drawable.head,list_questions.get(i).getQuestioner().getName(),list_questions.get(i).getDesc(),"10 赞同","20评论");
            contentList.add(question);
        }

       // Handler(homeReturn_data);
    }

    private void Handler(HomeReturnData homeReturn_data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(homeReturn_data.getMessage().equals("success")) {
                    Toast.makeText(HomeActivity.this, "问题列表加载成功", Toast.LENGTH_LONG).show();
                }


            }
        });
    }


 //    protected void onResume() {}



}