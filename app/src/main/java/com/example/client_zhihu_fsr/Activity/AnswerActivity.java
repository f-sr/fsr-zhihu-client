package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.ReturnData.AnswerReturnData;
import com.example.client_zhihu_fsr.ReturnData.SingleAnswerData;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewAnswer;
    private Button buttonEditAnswer;

    private ToggleButton toggleButtonAgree;
    private ToggleButton toggleButtonDisagree;

    private String answer;
    private int answerId;
    private String answerOriginAddress = "http://42.192.88.213:8080/api/answer/";
    private String answerNewAddress ;

    private String agreeOriginAddress = "http://42.192.88.213:8080/api/voter/";
    private String agreeNewAddress ;
    private String disagreeNewAddress ;//反对


    private AnswerReturnData answerReturnData;
    private int supportNUm;
    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        initView();
        initEvent();
        initDataIn();
        sendRequestForAnswer();

    }

    //初始化控件方法
    private void initView() {
        textViewAnswer = (TextView) findViewById(R.id.tvAnswer);
        buttonEditAnswer = (Button) findViewById(R.id.btEditAnswer);
        toggleButtonAgree=(ToggleButton) findViewById(R.id.ToggleBtAgree);
        toggleButtonDisagree = (ToggleButton) findViewById(R.id.ToggleBtDisagree);
    }



    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
       // answer = intent.getStringExtra("extraAnswer");
        answerId = intent.getIntExtra("extraAnswerId",404);
        Boolean isMine = intent.getBooleanExtra("extraAnswerIsMine",false);
       // textViewAnswer.setText(answer);
        //是否是自己的回答
        Log.d("AnswerAdapter","uid/AnswerUId isMine    "+isMine);
        if(isMine){
            buttonEditAnswer.setVisibility(View.VISIBLE);//把编辑回答按钮显示出来
        }

        //
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        token = sp.getString("token", "");

        //完善IP
        StringBuffer Address1 = new StringBuffer(answerOriginAddress);
        Address1.append(answerId);
        answerNewAddress = new String(Address1);

        //点赞
        StringBuffer Address2 = new StringBuffer(agreeOriginAddress);
        Address2.append(answerId);
        Address2.append("?upOrDown=true");
        agreeNewAddress = new String(Address2);

        //踩
        StringBuffer Address3 = new StringBuffer(agreeOriginAddress);
        Address3.append(answerId);
        Address3.append("?upOrDown=false");
        disagreeNewAddress = new String(Address3);
    }


    //注册事件方法
    private void initEvent() {
        buttonEditAnswer.setOnClickListener(this);
//        buttonAgree.setOnClickListener(this);
//        buttonDisagree.setOnClickListener(this);

        //点赞的监听
        toggleButtonAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){//开
                    toggleButtonDisagree.setVisibility(View.INVISIBLE);
                    supportNUm++;
                    toggleButtonAgree.setTextOn("赞同"+String.valueOf(supportNUm));
                    sendRequestForAgree();
                }
                else{//关
                    toggleButtonDisagree.setVisibility(View.VISIBLE);
                    supportNUm--;
                    toggleButtonAgree.setTextOff("赞同"+String.valueOf(supportNUm));
                    sendRequestForCancelAgree();
                }
            }
        });


        //反对的监听
        toggleButtonDisagree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){//开
                    toggleButtonAgree.setVisibility(View.INVISIBLE);
                    sendRequestForDisagree();
                }
                else{//关
                    toggleButtonAgree.setVisibility(View.VISIBLE);
                    sendRequestForCancelDisagree();
                }
            }
        });


    }






    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btEditAnswer :

                Intent intent = new Intent(AnswerActivity.this, EditAnswerActivity.class);
                intent.putExtra("extraAnswer",answer);
                intent.putExtra("extraAnswerId",answerId);
                Log.d("AnswerActivity","answerId = "+answerId);
                startActivityForResult(intent,103);
                break;

            default:
                break;
        }
    }


    private void sendRequestForAnswer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Get
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(answerNewAddress)
                            .addHeader("authorization", token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
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
        answerReturnData = gson.fromJson(jsonData, AnswerReturnData.class);
        Log.d("AnswerActivity","answerReturnData is "+ answerReturnData.toString());

        //切回UI线程做后处理
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (answerReturnData.getMessage().equals("success")) {
                    Toast.makeText(getApplicationContext(), "回答信息加载成功", Toast.LENGTH_SHORT).show();

                    answer = answerReturnData.getData().getContent();
                    textViewAnswer.setText(answer);
                    //之前点过赞
                    if(answerReturnData.getData().getVoted() == 1) {
                        supportNUm=answerReturnData.getData().getSupportersCount()-1;//避免重复记录赞数
                        toggleButtonAgree.setChecked(true);
                        toggleButtonAgree.setTextOn("赞同"+String.valueOf(supportNUm));
                        toggleButtonAgree.setVisibility(View.VISIBLE);
                    }
                    //之前点过踩
                    else if(answerReturnData.getData().getVoted() == -1){
                        supportNUm=answerReturnData.getData().getSupportersCount();
                        toggleButtonDisagree.setChecked(true);
                        toggleButtonDisagree.setVisibility(View.VISIBLE);
                    }

                    //之前没有任何操作
                    else if(answerReturnData.getData().getVoted() == 0){
                        supportNUm=answerReturnData.getData().getSupportersCount();
                        toggleButtonAgree.setChecked(false);
                        toggleButtonAgree.setTextOff("赞同"+String.valueOf(supportNUm));
                        toggleButtonAgree.setVisibility(View.VISIBLE);
                        toggleButtonDisagree.setVisibility(View.VISIBLE);
                    }



                }else {
                    Toast.makeText(getApplicationContext(), "回答信息加载失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //点赞请求
    private void sendRequestForAgree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post
                try {
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    JSONObject json = new JSONObject();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(agreeNewAddress)
                            .post(requestBody)
                            .addHeader("authorization", token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswerActivity", "AgreeReturnData is " + responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }



    //点赞取消
    private void  sendRequestForCancelAgree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(agreeNewAddress)
                            .delete()
                            .addHeader("authorization", token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswerActivity", "AgreeReturnData is " + responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }




    //反对请求
    private void sendRequestForDisagree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post
                try {
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                    JSONObject json = new JSONObject();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(disagreeNewAddress)
                            .post(requestBody)
                            .addHeader("authorization", token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswerActivity", "DisagreeReturnData is " + responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }



    //点赞取消
    private void  sendRequestForCancelDisagree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //post
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(disagreeNewAddress)
                            .delete()
                            .addHeader("authorization", token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswerActivity", "DisagreeReturnData is " + responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();

    }



    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103://编辑回答 返回的结果，OK表示保存，Cancel表示删除
                if(resultCode == RESULT_OK){
                    answer = data.getStringExtra("save");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewAnswer.setText(answer);
                        }
                    });
                }else if(resultCode == RESULT_CANCELED){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
                break;

            default:
                break;
        }
    }









}