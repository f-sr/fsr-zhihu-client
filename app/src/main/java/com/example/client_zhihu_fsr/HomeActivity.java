package com.example.client_zhihu_fsr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private HomeReturn_data homeReturn_data;
    private String originAddress = "http://42.192.88.213:8080/api/questions/list";

    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendRequestWithHttpURLConnection();
        ContentAdapter adapter = new ContentAdapter(contentList);
        recyclerView.setAdapter(adapter);

        switch (requestCode) {
            case 1:

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContentAdapter adapter = new ContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
    }

    //初始化控件方法
    private void initView() {
        button_Mine = (Button)findViewById(R.id.bt_Mini);
        button_Publish = (Button)findViewById(R.id.bt_Publish);
        recyclerView = (RecyclerView) findViewById(R.id.rv_All);
    }


    //注册事件方法
    private void initEvent() {
        button_Mine.setOnClickListener(this);
        button_Publish.setOnClickListener(this);

        //注册recycleView
//        initContents();
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Mini :
                Intent intent =new Intent(HomeActivity.this,MineActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_Publish :
                Intent intent2 = new Intent(HomeActivity.this,PublishActivity.class);
                startActivityForResult(intent2,1);
                break;

            default:
                break;
        }
    }






//    private void initContents(){
//        //完成对contentList初始化，从数据库中传入数据
//        for(int i=0;i<4;i++){
//            Content apple = new Content("标题111",R.drawable.head,"张三","描述111111111111111111111111111111111111111111111111111111","10 赞同","20评论");
//            contentList.add(apple);
//            Content banana = new Content("标题222",R.drawable.head2,"张式","描述2222222222222222222222222222222222222222222","10 赞同","20评论");
//            contentList.add(banana);
//            Content orange = new Content("标题333",R.drawable.head2,"张会","描述33333333333333333333333333333333333333333333333333333333","10 赞同","20评论");
//            contentList.add(orange);
//            Content strawberry = new Content("标题444",R.drawable.head,"张得分","描述44444444444444444444444444444444444444444444444","10 赞同","20评论");
//            contentList.add(strawberry);
//            Content pig = new Content("标题555",R.drawable.head,"张否","描述555555555555555555555555555555555555555555555555555555555","10 赞同","20评论");
//            contentList.add(pig);
//        }
//    }

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
        java.lang.reflect.Type type = new TypeToken<HomeReturn_data>() {}.getType();
        homeReturn_data = gson.fromJson(jsonData, type);

        List<Data_data> list_questions = homeReturn_data.getList_data();//问题列表提取成功



        Log.d("HomeActivity","jsonData is "+jsonData);
        Log.d("HomeActivity", "message is " + homeReturn_data.getMessage());
        Log.d("HomeActivity", "Total is " + homeReturn_data.getTotal());
        Log.d("HomeActivity", "list_questions[0] =  " + list_questions.get(0).toString());//测试成功

        Log.d("HomeActivity", "list_questions[1].Title =  " + list_questions.get(5).getTitle());
        Log.d("HomeActivity", "list_questions[1].userName=  " + list_questions.get(5).getQuestioner().getName());
        Log.d("HomeActivity", "list_questions[1].desc =  " + list_questions.get(5).getDesc());



        for(int i=0;i<homeReturn_data.getTotal();i++){
            Content question = new Content(list_questions.get(i).getTitle(),R.drawable.head,list_questions.get(i).getQuestioner().getName(),list_questions.get(i).getDesc(),"10 赞同","20评论");
            contentList.add(question);

        }


//        Handler(homeReturn_data);
    }

    private void Handler(HomeReturn_data homeReturn_data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(homeReturn_data.getMessage().equals("success")) {
                    Toast.makeText(HomeActivity.this, "问题列表加载成功", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }



}