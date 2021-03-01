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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.EditQuestionReturnData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditSelfIntroductionActivity extends AppCompatActivity implements View.OnClickListener{

    private String originAddress = "http://42.192.88.213:8080/api/profile/";
    private String newAddress;
    private EditText editTextName;
    private EditText editTextDesc;
    private Button buttonSave;

    private String token;
    private String name;
    private String desc;
    private EditQuestionReturnData editQuestionReturnData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_self_introduction);
        initView();
        initEvent();
        initDataIn();
        editTextName.setText(name);
        editTextDesc.setText(desc);


    }



    //初始化控件方法
    private void initView() {
       editTextName = (EditText) findViewById(R.id.etName);
       editTextDesc = (EditText) findViewById(R.id.etDesc);
       buttonSave = (Button) findViewById(R.id.btSaveIntroduction);
    }

    //注册事件方法
    private void initEvent() {
        buttonSave.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSaveIntroduction :
                sendRequestForEditSelfIntroduction();
                break;

            default:
                break;
        }
    }



    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");

        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        token = sp.getString("token", "");
        int uid = sp.getInt("uid", 876);
        //完善IP
        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(uid);
        newAddress = new String(Address);
    }




    //修改简介
    private void sendRequestForEditSelfIntroduction() {

        name = editTextName.getText().toString().trim();
        desc = editTextDesc.getText().toString().trim();
        Log.d("EditQuestionActivity","desc : "+desc);

        if (name.isEmpty()) {
            Toast.makeText(EditSelfIntroductionActivity.this, "用户名不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (desc.isEmpty()) {
            Toast.makeText(EditSelfIntroductionActivity.this, "签名不能为空！", Toast.LENGTH_LONG).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建jSON格式数据
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("name", name);
                    json.put("desc", desc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //POST
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url(newAddress)
                            .addHeader("authorization",token)
                            .put(requestBody)
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
        editQuestionReturnData = gson.fromJson(jsonData, EditQuestionReturnData.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(editQuestionReturnData.getMessage().equals("success")) {
                    Toast.makeText(getApplicationContext(), "编辑简介成功", Toast.LENGTH_SHORT).show();
                    Log.d("EditSelfIntroduction","editQuestionReturnData="+editQuestionReturnData.toString());
                    Intent intent = new Intent();
                    intent.putExtra("name",name);
                    intent.putExtra("desc",desc);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }






    }