package com.example.client_zhihu_fsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_publishTitle;
    private EditText editText_publishDescribe;
    private Button button_checkPublish;
    private PublishReturn_data publishReturn_data;
    private String originAddress = "http://42.192.88.213:8080/api/question/create";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initEvent();
        Button button_checkPublish = (Button) findViewById(R.id.bt_checkPublish);
    }


    //初始化控件方法
    private void initView() {
        editText_publishTitle = (EditText) findViewById(R.id.et_publishTitle);
        editText_publishDescribe = (EditText) findViewById(R.id.et_publishDescribe);
        button_checkPublish = (Button) findViewById(R.id.bt_checkPublish);
    }


    //注册事件方法
    private void initEvent() {
        button_checkPublish.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_checkPublish :
                sendRequestWithHttpURLConnection();
                break;

            default:
                break;
        }
    }


    private  void  sendRequestWithHttpURLConnection() {

        String title = editText_publishTitle.getText().toString().trim();
        String desc = editText_publishDescribe.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(PublishActivity.this, "问题不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (desc.isEmpty()) {
            Toast.makeText(PublishActivity.this, "问题描述不能为空！", Toast.LENGTH_LONG).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建jSON格式数据
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("title", title);
                    json.put("desc", desc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //POST
                try {
                    SharedPreferences sp = getSharedPreferences("loginToken",0);
                    String token = sp.getString("token","");
//                    Log.d("PublishActivity","token is "+token);

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url(originAddress)
                            .addHeader("authorization",token)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                  showResponse(responseData);
                    parseJSONWithJSONObject(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }


    private void parseJSONWithJSONObject(String jsonData){

        Gson gson = new Gson();
        publishReturn_data = gson.fromJson(jsonData, PublishReturn_data.class);

        Log.d("PublishActivity","jsonData is "+jsonData);
        Log.d("PublishActivity", "message is " + publishReturn_data.getMessage());
        Log.d("PublishActivity", "status is " + publishReturn_data.getData().getTitle());
        Log.d("PublishActivity", "token is " + publishReturn_data.getData().getQuestioner().getName());

        Handler(publishReturn_data);
    }

    private void Handler(PublishReturn_data publishReturn_data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        if(publishReturn_data.getMessage().equals("success")) {
            Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_LONG).show();
            finish();
                 }
            }
        });
    }










}