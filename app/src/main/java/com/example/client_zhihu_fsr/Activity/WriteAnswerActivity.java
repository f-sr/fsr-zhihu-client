package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.WriteAnswerReturnData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WriteAnswerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewTitle;
    private EditText editTextAnswer;
    private Button buttonAnswer;
    private String originAddress = "http://42.192.88.213:8080/api/answer/create?questionID=";
    private String NewAddress;
    private WriteAnswerReturnData writeAnswerReturnData;
    private int questionId;
    private String questionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        initView();
        initEvent();
        initDataIn();

    }


    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView)findViewById(R.id.tvTitle);
        editTextAnswer = (EditText)findViewById(R.id.etAnswer);
        buttonAnswer = (Button)findViewById(R.id.btAnswer);
    }


    //注册事件方法
    private void initEvent() {
        buttonAnswer.setOnClickListener(this);

    }

    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extraQuestionId",1);
        questionTitle = intent.getStringExtra("extraTitle");
        textViewTitle.setText(questionTitle);
        
        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(questionId);
        NewAddress = new String(Address);
        Log.d("WriteAnswerActivity","questionId is "+questionId);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAnswer :
                sendRequestWithWriteAnswer();
                break;

            default:
                break;
        }
    }

    private void sendRequestWithWriteAnswer(){

        String answer = editTextAnswer.getText().toString().trim();

        if (answer.isEmpty()) {
            Toast.makeText(WriteAnswerActivity.this, "回答不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建jSON格式数据
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("content", answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //POST
                try {
                    SharedPreferences sp = getSharedPreferences("loginToken",0);
                    String token = sp.getString("token","");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url(NewAddress)
                            .addHeader("authorization",token)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("WriteAnswerActivity","responseData is "+responseData);

                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }


    //解析数据
    private void parseJSONWithJSONObject(String jsonData){

        Gson gson = new Gson();
        writeAnswerReturnData = gson.fromJson(jsonData, WriteAnswerReturnData.class);
        Log.d("WriteAnswerActivity","writeAnswerReturnData is "+writeAnswerReturnData.toString());


        //切回UI线程展示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(writeAnswerReturnData.getMessage().equals("success")){
                    Toast.makeText(WriteAnswerActivity.this,"回答成功",Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }
















}