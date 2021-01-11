package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.LoginReturnData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_PhoneNumber;
    private EditText editText_Password;
    private Button button_register;
    private Button button_login;
    private CheckBox checkBox_displayPassword;
    private TextView ResponseText;
    private String originAddress = "http://42.192.88.213:8080/api/user/login";
    private LoginReturnData loginReturnData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }


    //初始化控件方法
    private void initView() {
        button_register = (Button) findViewById(R.id.bt_Register);
        button_login = (Button) findViewById(R.id.bt_Login);
        editText_PhoneNumber = (EditText) findViewById(R.id.et_PhoneNumber);
        editText_Password = (EditText) findViewById(R.id.et_Password);
        editText_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        checkBox_displayPassword = (CheckBox) findViewById(R.id.checkbox);//显示密码
        checkBox_displayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    editText_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editText_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


    //注册事件方法
    private void initEvent() {
        button_register.setOnClickListener(this);
        button_login.setOnClickListener(this);
    }


    @Override
    //实现onClick方法
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Login:
                sendRequestForLogin();
                break;
            case R.id.bt_Register:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void sendRequestForLogin() {
//        String number = editText_PhoneNumber.getText().toString().trim();
//        String password = editText_Password.getText().toString().trim();

          String number = "123456@163.com";
          String password = "123456";
//
//        String number = "fsr@163.com";
//        String password = "123456";

        if (number.isEmpty()) {
            Toast.makeText(MainActivity.this, "账号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        //开启一个HTTP请求线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建jSON格式数据
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("mail", number);
                    json.put("password", password);
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
//                  showResponse(responseData);
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
        loginReturnData = gson.fromJson(jsonData, LoginReturnData.class);
        Log.d("MainActivity", "loginReturnData is " + loginReturnData.toString());

        //登录成功
        if(loginReturnData.getMessage().equals("success")){
            //把token和用户ID 存为全APP共享数据
            SharedPreferences sp  = getSharedPreferences("loginToken",0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token","Bearer "+loginReturnData.getToken());
            editor.putInt("uid",loginReturnData.getUid());
            editor.commit();
            String token = sp.getString("token","");

            Intent intent =new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            //登录失败
            loginError(loginReturnData);
        }
    }


    //登录失败处理
    private void loginError(LoginReturnData loginReturnData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loginReturnData.getMessage().equals("record not found")) {
                    Toast.makeText(getApplicationContext(), "用户不存在，请重新输入！", Toast.LENGTH_SHORT).show();
                }
                else if(loginReturnData.getMessage().equals("crypto/bcrypt: hashedPassword is not the hash of the given password")){
                    Toast.makeText(getApplicationContext(), "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), loginReturnData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }











}