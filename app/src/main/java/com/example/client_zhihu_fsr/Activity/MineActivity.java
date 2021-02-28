package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionItem;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.QuestionAdapter;
import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.SelfIntroductionReturnData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    private List<QuestionItem> questionItemList = new ArrayList<>();
    private Button buttonHomePage;
    private Button buttonQuestion;
    private Button buttonAnswer;
    private Button buttonAgree;
    private Button buttonEditMaterials;
    private TextView textViewUserName;

    private SelfIntroductionReturnData selfIntroductionReturnData;
    private String token;
    private String originAddressSelfIntroduction = "http://42.192.88.213:8080/api/profile/";
    private String NewAddressSelfIntroduction ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        initEvent();
        sendRequestWithSelfIntroduction();


    }



    //初始化控件方法
    private void initView() {
        buttonHomePage = (Button)findViewById(R.id.btHomePage) ;
        buttonQuestion = (Button)findViewById(R.id.btQuestion);
        buttonAnswer = (Button)findViewById(R.id.btAnswer);
        buttonAgree = (Button) findViewById(R.id.btAgree);
        buttonEditMaterials = (Button) findViewById(R.id.btEditMaterials);
        textViewUserName = (TextView) findViewById(R.id.tvUserName);
    }


    //注册事件方法
    private void initEvent() {

        buttonHomePage.setOnClickListener(this);
        buttonQuestion.setOnClickListener(this);
        buttonAnswer.setOnClickListener(this);
        buttonAgree.setOnClickListener(this);


        //token
        SharedPreferences sp = getSharedPreferences("loginToken",0);
        token = sp.getString("token","");
        int uid = sp.getInt("uid",0);
        StringBuffer Address = new StringBuffer(originAddressSelfIntroduction);
        Address.append(uid);
        NewAddressSelfIntroduction = new String(Address);
    }


    @Override
    //实现onClick方法
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btHomePage:
                Intent intentHome = new Intent(MineActivity.this, HomeActivity.class);
                setResult(RESULT_OK, intentHome);
               // finish();
                startActivity(intentHome);

                break;

            case R.id.btQuestion:
                Intent intentQuestion = new Intent(MineActivity.this, MineQuestionActivity.class);
                startActivity(intentQuestion);
                break;

            case R.id.btAnswer:
                Intent intentAnswer = new Intent(MineActivity.this, MineAnswerActivity.class);
                startActivity(intentAnswer);

                break;

            case R.id.btAgree:
                Intent intentAgree = new Intent(MineActivity.this, MineAgreeActivity.class);
                startActivity(intentAgree);
                break;



            default:
                break;
        }
    }


    private void sendRequestWithSelfIntroduction(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .addHeader("authorization", token)
                            .url(NewAddressSelfIntroduction)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("MineActivity","responseData is(selfIntroduction) "+responseData);
                    parseJSONForSelfIntroduction(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }

    private void parseJSONForSelfIntroduction(String jsonData){

        Gson gson = new Gson();
        Type type = new TypeToken<SelfIntroductionReturnData>() {}.getType();
        selfIntroductionReturnData = gson.fromJson(jsonData, type);
        Log.d("MineActivity", "selfIntroductionReturnData is " + selfIntroductionReturnData.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewUserName.setText(selfIntroductionReturnData.getData().getName());
            }
        });
    }





}