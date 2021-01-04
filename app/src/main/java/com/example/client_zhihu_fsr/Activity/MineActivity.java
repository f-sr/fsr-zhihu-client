package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.Content;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.ContentAdapter;
import com.example.client_zhihu_fsr.R;

import java.util.ArrayList;
import java.util.List;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Content> contentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button button_homePage;
    private Button button_release;
    private Button button_answer;
    private Button button_editMaterials;
    private TextView textView_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        initEvent();


      //  initContents();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_release);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContentAdapter adapter = new ContentAdapter(contentList);
        recyclerView.setAdapter(adapter);
    }

    //初始化控件方法
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_release);
        button_homePage = (Button)findViewById(R.id.bt_HomePage) ;
        button_release = (Button)findViewById(R.id.bt_release);
        button_answer = (Button)findViewById(R.id.bt_answer);
        button_editMaterials = (Button) findViewById(R.id.bt_EditMaterials);
        textView_userName = (TextView) findViewById(R.id.tv_UserName);
        //textView_userName.setText("admin");

    }


    //注册事件方法
    private void initEvent() {
        button_homePage.setOnClickListener(this);
    }



    @Override
    //实现onClick方法
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_HomePage:
                Intent intentHome =new Intent(MineActivity.this, HomeActivity.class);
                setResult(RESULT_OK,intentHome);
                finish();

                break;


            default:
                break;
        }
    }








//    private void initContents(){
//        //完成对contentList初始化，从数据库中传入数据
//
//        for(int i=0;i<4;i++){
//            Content apple = new Content("震惊111",R.drawable.head,"张三","描述111111111111111111111111111111111111111111111111111111","10 赞同","20评论");
//            contentList.add(apple);
//            Content banana = new Content("震惊222",R.drawable.head2,"张式","评论2222222222222222222222222222222222222222222","10 赞同","20评论");
//            contentList.add(banana);
//            Content orange = new Content("震惊333",R.drawable.head2,"张会","评论33333333333333333333333333333333333333333333333333333333","10 赞同","20评论");
//            contentList.add(orange);
//            Content strawberry = new Content("震惊444",R.drawable.head,"张得分","评论44444444444444444444444444444444444444444444444","10 赞同","20评论");
//            contentList.add(strawberry);
//            Content pig = new Content("震惊555",R.drawable.head,"张否","评论5555555555555555555555555555555555555555555555555555555555","10 赞同","20评论");
//            contentList.add(pig);
//        }
//    }

}