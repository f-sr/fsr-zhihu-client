package com.example.client_zhihu_fsr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Content> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button button_Mine = (Button)findViewById(R.id.bt_Mini);

        button_Mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,MineActivity.class);
                startActivity(intent);
            }
        });

        Button button_Publish = (Button)findViewById(R.id.bt_Publish);
        button_Publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this,PublishActivity.class);
                startActivity(intent2);
            }
        });



        initContents();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_All);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContentAdapter adapter = new ContentAdapter(contentList);
        recyclerView.setAdapter(adapter);



    }


    private void initContents(){
        //完成对contentList初始化，从数据库中传入数据

        for(int i=0;i<4;i++){
            Content apple = new Content("标题111",R.drawable.head,"张三","描述111111111111111111111111111111111111111111111111111111","10 赞同","20评论");
            contentList.add(apple);
            Content banana = new Content("标题222",R.drawable.head2,"张式","描述2222222222222222222222222222222222222222222","10 赞同","20评论");
            contentList.add(banana);
            Content orange = new Content("标题333",R.drawable.head2,"张会","描述33333333333333333333333333333333333333333333333333333333","10 赞同","20评论");
            contentList.add(orange);
            Content strawberry = new Content("标题444",R.drawable.head,"张得分","描述44444444444444444444444444444444444444444444444","10 赞同","20评论");
            contentList.add(strawberry);
            Content pig = new Content("标题555",R.drawable.head,"张否","描述555555555555555555555555555555555555555555555555555555555","10 赞同","20评论");
            contentList.add(pig);
        }
    }



}