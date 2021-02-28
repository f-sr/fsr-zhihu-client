package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.EditAnswerReturnData;
import com.example.client_zhihu_fsr.ReturnData.EditQuestionReturnData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditAnswerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextAnswer;
    private Button buttonDelete;
    private Button buttonSave;
    private String answer;
    private int answerId;
    private String originAddress = "http://42.192.88.213:8080/api/answer/";
    private String originAddressNew;
    private String token;
    private EditAnswerReturnData editAnswerReturnData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer);
        initView();
        initEvent();
        initDataIn();
    }

    //初始化控件方法
    private void initView() {
        editTextAnswer = (EditText) findViewById(R.id.etAnswer);
        buttonDelete = (Button) findViewById(R.id.btDelete);
        buttonSave =(Button) findViewById(R.id.btSave);
    }


    //注册事件方法
    private void initEvent() {
        buttonDelete.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        answer = intent.getStringExtra("extraAnswer");
        answerId = intent.getIntExtra("extraAnswerId",404);

        editTextAnswer.setText(answer);

        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(answerId);
        Log.d("EditAnswerActivity","answerID is "+answerId);
        originAddressNew = new String(Address);

        SharedPreferences sp = getSharedPreferences("loginToken",0);
        token = sp.getString("token","");
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btDelete :
                sendRequestWithDeleteAnswer();
                break;

            case R.id.btSave :
                sendRequestWithSaveAnswer();
                break;

            default:
                break;
        }
    }




    //修改评论
    private void sendRequestWithSaveAnswer() {

        answer = editTextAnswer.getText().toString().trim();


        if (answer.isEmpty()) {
            Toast.makeText(EditAnswerActivity.this, "评论不能为空！", Toast.LENGTH_LONG).show();
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
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url(originAddressNew)
                            .addHeader("authorization",token)
                            .put(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("EditAnswerActivity","responseData(save) is "+responseData);
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }





    //删除评论
    private void sendRequestWithDeleteAnswer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(originAddressNew)
                            .addHeader("authorization",token)
                            .delete()
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("EditAnswerActivity","responseData(delete) is "+responseData);

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
        editAnswerReturnData = gson.fromJson(jsonData, EditAnswerReturnData.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(editAnswerReturnData.getMessage().equals("success")) {
                    Toast.makeText(getApplicationContext(), "编辑成功", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(EditQuestionActivity.this,HomeActivity.class);
//                    startActivity(intent);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(EditAnswerActivity.this,AnswersListActivity.class));
                        }
                    },100);
                 //   finish();


                }
            }
        });
    }




}