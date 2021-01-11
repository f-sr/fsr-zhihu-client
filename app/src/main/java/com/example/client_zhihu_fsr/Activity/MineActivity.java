package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionItem;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionAdapter;
import com.example.client_zhihu_fsr.R;

import java.util.ArrayList;
import java.util.List;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    private List<QuestionItem> questionItemList = new ArrayList<>();
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


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_release);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        QuestionAdapter adapter = new QuestionAdapter(questionItemList);
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
                Intent intentHome = new Intent(MineActivity.this, HomeActivity.class);
                setResult(RESULT_OK, intentHome);
                finish();
                break;

            default:
                break;
        }
    }






}