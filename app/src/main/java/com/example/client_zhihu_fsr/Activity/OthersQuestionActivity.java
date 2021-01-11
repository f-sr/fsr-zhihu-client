package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.PublishReturnData;
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
    private String originAddressNew;
    private PublishReturnData publishReturnData;//这个和发布问题后获取的数据一样，所以不再重复写

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_question);
        initView();
        initEvent();
        initDataIn();
        sendRequestWithHttpURLConnection();
    }





    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.tv_Title);
        textViewAgree = (TextView) findViewById(R.id.tvViews);
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
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extra_QuestionId",1);
        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(questionId);
        originAddressNew = new String(Address);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_heart :
                break;

            case R.id.bt_star :
                break;

            case R.id.bt_WriterAnswer:
                //
               Intent intent = new Intent(OthersQuestionActivity.this,WriteAnswerActivity.class);
               intent.putExtra("extraQuestionId",questionId);
               intent.putExtra("extraTitle",publishReturnData.getData().getTitle());
               startActivity(intent);
               break;

            case R.id.bt_comment :
                break;

            case R.id.bt_HowManyAnswer:
                Intent intent2 = new Intent(OthersQuestionActivity.this,AnswersListActivity.class);
                intent2.putExtra("extraQuestionId",questionId);
                intent2.putExtra("extraTitle",publishReturnData.getData().getTitle());
                intent2.putExtra("extraAnswersCount",publishReturnData.getData().getAnswersCount());
                intent2.putExtra("extraViewCount",publishReturnData.getData().getViewCount());
                startActivity(intent2);
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
        publishReturnData = gson.fromJson(jsonData, PublishReturnData.class);
        Log.d("MyQuestionActivity","publishReturnData is "+ publishReturnData.toString());


        //请求数据完毕，回UI线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTitle.setText(publishReturnData.getData().getTitle());
                textViewDescribe.setText(publishReturnData.getData().getDesc());
                textViewUserName.setText(publishReturnData.getData().getQuestioner().getName());
                buttonHowManyAnswer.setText(publishReturnData.getData().getAnswersCount()+"人回答了这个问题");
            }
        });
    }










}