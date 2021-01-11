package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client_zhihu_fsr.ReturnData.PublishReturnData;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.client_zhihu_fsr.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private TextView textViewUserName;
    private TextView textViewDescribe;
    private ImageButton imageButtonHead;
    private Button buttonEditQuestion;
    private Button buttonHowManyAnswer;
    private String originAddress = "http://42.192.88.213:8080/api/question/";
    private int questionId;
    private PublishReturnData publishReturnData;//这个和发布问题后获取的数据一样，所以不再重复写

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);
        initView();
        initEvent();
        initDataIn();
        sendRequestWithHttpURLConnection();
    }

    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.tv_Title);
        textViewUserName = (TextView) findViewById(R.id.tv_UserName);
        textViewDescribe = (TextView) findViewById(R.id.tv_describe);
        imageButtonHead = (ImageButton) findViewById(R.id.ib_head);
        buttonEditQuestion = (Button) findViewById(R.id.bt_EditQuestion);
        buttonHowManyAnswer = (Button) findViewById(R.id.bt_HowManyAnswer);
    }


    //注册事件方法
    private void initEvent() {
        buttonEditQuestion.setOnClickListener(this);
        buttonHowManyAnswer.setOnClickListener(this);
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extra_QuestionId", 1);
        Log.d("QuestionActivity", "questionId is " + questionId);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_EditQuestion:
                Intent intent = new Intent(MyQuestionActivity.this, EditQuestionActivity.class);
                intent.putExtra("extraQuestionId", questionId);
                intent.putExtra("extraTitle", publishReturnData.getData().getTitle());
                intent.putExtra("extraDesc", publishReturnData.getData().getDesc());
                startActivity(intent);
                break;

            case R.id.bt_HowManyAnswer:
                Intent intent2 = new Intent(MyQuestionActivity.this,AnswersListActivity.class);
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


    private void sendRequestWithHttpURLConnection() {
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
                    Log.d("QuestionActivity", "responseData is " + responseData);
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }


    private void parseJSONWithJSONObject(String jsonData) {
        Gson gson = new Gson();
        publishReturnData = gson.fromJson(jsonData, PublishReturnData.class);
        Log.d("MyQuestionActivity", "publishReturnData is  " + publishReturnData.toString());
        //切回IU线程
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