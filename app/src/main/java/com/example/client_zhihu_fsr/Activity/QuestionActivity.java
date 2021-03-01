package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client_zhihu_fsr.ReturnData.QuestionListReturnData;
import com.example.client_zhihu_fsr.ReturnData.QuestionReturnData;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private TextView textViewUserName;
    private TextView textViewDescribe;
    private ImageButton imageButtonHead;
    private Button buttonEditQuestion;
    private Button buttonWriterAnswer;
    private Button buttonHowManyAnswer;
    private String originAddress = "http://42.192.88.213:8080/api/question/";
    String originAddressNew;
    private int questionId;
    private QuestionReturnData questionReturnData;//这个和发布问题后获取的数据一样，所以不再重复写
    private int AnswersCount;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
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
        buttonWriterAnswer = (Button) findViewById(R.id.bt_WriterAnswer);
    }


    //注册事件方法
    private void initEvent() {
        buttonEditQuestion.setOnClickListener(this);
        buttonHowManyAnswer.setOnClickListener(this);
        buttonWriterAnswer.setOnClickListener(this);
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extra_QuestionId", 404);
        boolean isMyQuestion = intent.getBooleanExtra("extraIsMyQuestion", false);
        userName = intent.getStringExtra("extraName");
        Log.d("QuestionActivity", "questionId is " + questionId);
        //拼接出IP
        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(questionId);
        originAddressNew = new String(Address);


        //分辨是不是自己的Question
        if (isMyQuestion) {
            buttonWriterAnswer.setVisibility(View.GONE);
        } else if (!isMyQuestion) {
            buttonEditQuestion.setVisibility(View.GONE);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_EditQuestion:
                Intent intent = new Intent(QuestionActivity.this, EditQuestionActivity.class);
                intent.putExtra("extraQuestionId", questionId);
                intent.putExtra("extraTitle", questionReturnData.getData().getTitle());
                intent.putExtra("extraDesc", questionReturnData.getData().getDesc());
                startActivity(intent);
                break;

            case R.id.bt_HowManyAnswer:
                Intent intent2 = new Intent(QuestionActivity.this, AnswersListActivity.class);
                intent2.putExtra("extraQuestionId", questionId);
                intent2.putExtra("extraTitle", questionReturnData.getData().getTitle());
                //intent2.putExtra("extraAnswersCount", AnswersCount);
                intent2.putExtra("extraViewCount", questionReturnData.getData().getViewCount());
                startActivity(intent2);
                break;

            case R.id.bt_WriterAnswer:
                SharedPreferences sp = v.getContext().getSharedPreferences("loginToken",0);
                int uId = sp.getInt("uid",10086);
                if(uId == 10086){
                    Intent intentMain = new Intent(QuestionActivity.this,MainActivity.class);
                    startActivity(intentMain);
                    Toast.makeText(QuestionActivity.this, "请先登录!", Toast.LENGTH_LONG).show();
                    break;
                }
                //
                Intent intent3 = new Intent(QuestionActivity.this, WriteAnswerActivity.class);
                intent3.putExtra("extraQuestionId", questionId);
                intent3.putExtra("extraTitle", questionReturnData.getData().getTitle());
                startActivityForResult(intent3,3);
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
        questionReturnData = gson.fromJson(jsonData, QuestionReturnData.class);
        AnswersCount = questionReturnData.getData().getAnswersCount();//记录回答数
        Log.d("QuestionActivity", "questionReturnData is  " + questionReturnData.toString());
        Log.d("QuestionActivity","count = "+AnswersCount);
        //切回IU线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTitle.setText(questionReturnData.getData().getTitle());
                textViewDescribe.setText(questionReturnData.getData().getDesc());
                //textViewUserName.setText(questionReturnData.getData().getQuestioner().getName());
                textViewUserName.setText(userName);
                buttonHowManyAnswer.setText(questionReturnData.getData().getAnswersCount() + "人回答了这个问题");
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode ,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3://写回答成功
                if(resultCode == RESULT_OK){
                    //更新回答数
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AnswersCount++;
                            Log.d("QuestionActivity","count = "+AnswersCount);
                            buttonHowManyAnswer.setText(AnswersCount+ "人回答了这个问题");

                        }
                    });
                }
                break;
            default:
                break;
        }
    }



}