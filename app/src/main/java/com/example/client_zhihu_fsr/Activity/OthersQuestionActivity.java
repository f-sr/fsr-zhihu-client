package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.PublishReturn_data;
import com.google.gson.Gson;
import com.sackcentury.shinebuttonlib.ShineButton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OthersQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private TextView textViewAgree;
    private TextView textViewStar;
    private TextView textViewUserName;
    private TextView textViewDescribe;
    private ImageButton imageButtonHead;
    private Button buttonWriteAnswer;
    private ShineButton buttonHeart;
    private ShineButton buttonStar;
    private Button buttonHowManyAnswer;
    private Button buttonComment;
    private String originAddress = "http://42.192.88.213:8080/api/question/";
    private int questionId;
    private PublishReturn_data publishReturn_data;//这个和发布问题后获取的数据一样，所以不再重复写

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_question);

        initView();
        initEvent();
        sendRequestWithHttpURLConnection();
    }





    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.tv_Title);
        textViewAgree = (TextView) findViewById(R.id.tv_agree);
        textViewStar = (TextView) findViewById(R.id.tv_star);
        textViewUserName = (TextView) findViewById(R.id.tv_UserName);
        textViewDescribe = (TextView) findViewById(R.id.tv_describe);
        imageButtonHead = (ImageButton) findViewById(R.id.ib_head);
        buttonWriteAnswer = (Button) findViewById(R.id.bt_WriterAnswer);
        buttonHeart = (ShineButton) findViewById(R.id.bt_heart);
        buttonStar = (ShineButton) findViewById(R.id.bt_star);
        buttonHowManyAnswer = (Button) findViewById(R.id.bt_HowManyAnswer);
        buttonComment = (Button) findViewById(R.id.bt_comment);
    }


    //注册事件方法
    private void initEvent() {
        buttonWriteAnswer.setOnClickListener(this);
        buttonHeart.setOnClickListener(this);
        buttonStar.setOnClickListener(this);
        buttonHowManyAnswer.setOnClickListener(this);
        buttonComment.setOnClickListener(this);

        Intent intent = getIntent();
        questionId = intent.getIntExtra("extra_QuestionId",1);
        Log.d("QuestionActivity","questionId is "+questionId);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_heart :
                break;

            case R.id.bt_star :
                break;

            case R.id.bt_WriterAnswer:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OthersQuestionActivity.this,WriteAnswerActivity.class));
                    }
                },10);

                break;

            case R.id.bt_comment :
                break;

            case R.id.bt_HowManyAnswer:
                break;

            default:
                break;
        }
    }





    private void  sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer Address = new StringBuffer(originAddress);
                    Address.append(questionId);
                    String originAddressNew = new String(Address);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(originAddressNew)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("QuestionActivity","responseData is "+responseData);
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
        publishReturn_data = gson.fromJson(jsonData, PublishReturn_data.class);
        Log.d("MyQuestionActivity","Title is "+ publishReturn_data.getData().getTitle());
        Handler();
    }

    private void Handler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTitle.setText(publishReturn_data.getData().getTitle());
                textViewDescribe.setText(publishReturn_data.getData().getDesc());
                textViewUserName.setText(publishReturn_data.getData().getQuestioner().getName());
            }
        });
    }














}