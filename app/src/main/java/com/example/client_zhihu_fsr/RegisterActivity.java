package com.example.client_zhihu_fsr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_PhoneNumber;
    private EditText editText_Email;
    private EditText editText_userName;
    private EditText editText_password;
    private Button button_verifyRegister;
    private String originAddress = "http://42.192.88.213:8080/api/user/sign";
    private RegisterReturn_data registerReturn_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
    }




    //初始化控件方法
    private void initView() {
        editText_PhoneNumber = (EditText) findViewById(R.id.et_PhoneNumber);
        editText_Email = (EditText) findViewById(R.id.et_Email);
        editText_userName = (EditText) findViewById(R.id.et_UserName);
        editText_password = (EditText) findViewById(R.id.et_Password);
        button_verifyRegister = (Button) findViewById(R.id.bt_VerifiRegister);
    }

    //注册事件方法
    private void initEvent() {
        button_verifyRegister.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.bt_VerifiRegister :
                sendRequestWithHttpURLConnection();
                break;

            default:
                break;
        }
    }

    private void sendRequestWithHttpURLConnection(){
//          String phoneNumber = "13905690000";
//          String email = "1115244149@qq.com";
//          String name = "LiXing";
//          String password = "123456";

         String phoneNumber = editText_PhoneNumber.getText().toString().trim();
         String email = editText_Email.getText().toString().trim();
         String name = editText_userName.getText().toString().trim();
         String password = editText_password.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "邮箱不能为空！", Toast.LENGTH_LONG).show();
            return;
        }else if(name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if(password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
                return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建jSON格式数据
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("mail", email);
                    json.put("password", password);
                    json.put("name", name);
                    json.put("phone", phoneNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //POST
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url(originAddress)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("RegisterActivitypppp","responseData is "+responseData);
                    parseJSONWithJSONObject(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }

    //解析回传JSON数据
    private void parseJSONWithJSONObject(String jsonData) {

        Gson gson = new Gson();
        registerReturn_data = gson.fromJson(jsonData, RegisterReturn_data.class);
        Log.d("RegisterActivity", "message is " + registerReturn_data.getMessage());
        Log.d("RegisterActivity", "status is " + registerReturn_data.getStatus());
        //处理后续事件
        Handler(registerReturn_data);

    }





    private  void Handler(RegisterReturn_data registerReturn_data){

        if(registerReturn_data.getMessage().equals("success")){

            Intent intent_return = new Intent();
            intent_return.putExtra("data_return","注册成功！");
            setResult(RESULT_OK,intent_return);
            finish();
        } else {
            //在线程中无法启动UI
            showResponse_error(registerReturn_data);
        }
    }

    //回主线程
    private void showResponse_error(RegisterReturn_data registerReturn_data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,registerReturn_data+"用户存在！",Toast.LENGTH_SHORT).show();
            }
        });
    }



}

