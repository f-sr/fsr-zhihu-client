package com.example.client_zhihu_fsr.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client_zhihu_fsr.R;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerAdapter;
import com.example.client_zhihu_fsr.RecyclerViewAdapter.AnswerItem;
import com.example.client_zhihu_fsr.ReturnData.AnswersListReturnData;
import com.example.client_zhihu_fsr.ReturnData.SingleAnswerData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnswersListActivity extends AppCompatActivity {

    private List<AnswerItem> answerItemList = new ArrayList<>();
    private AnswersListReturnData answersListReturnData;
    private RecyclerView mAnswerRecyclerView;
    private AnswerAdapter mAnswerAdapter;


    private TextView textViewTitle;
    private TextView textViewViews;
    private TextView textViewAnswers;

    private String originAddress = "http://42.192.88.213:8080/api/answers/listByQuestion?questionID=";
    private String timeOrderAddress = "&order=create_time";
    private String supportersOrderAddress = "&order=supporters_count";
    private String NewAddress;//最终的目标地址


    private int questionId;
    private String questionTitle;
   // private int answersCount;
    private int viewCount;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_list);
        initView();
        initEvent();
        initDataIn();
        sendRequestWithAnswerList();
        mAnswerAdapter = new AnswerAdapter(answerItemList);
        mAnswerRecyclerView.setAdapter(mAnswerAdapter);
        mAnswerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线



//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                Log.d("888888888888888","88888888888888888");
//                sendRequestWithAnswerList();
//                //  mQuestionAdapter = new QuestionAdapter(questionItemList);
//
//                mAnswerAdapter.notifyDataSetChanged();
////                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 100);
//            }
//        });

    }


    //初始化控件方法
    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.tvTitle);
        textViewViews = (TextView) findViewById(R.id.tvViews);
        textViewAnswers = (TextView) findViewById(R.id.tvAnswers);
        mAnswerRecyclerView = (RecyclerView) findViewById(R.id.rvAnswersList);
    //    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout111);
    }


    //注册事件方法
    private void initEvent() {

        //recyclerView设置布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAnswerRecyclerView.setLayoutManager(layoutManager);
    }


    //初始化传入该活动的数据
    private void initDataIn() {
        //传入问题的ID
        Intent intent = getIntent();
        questionId = intent.getIntExtra("extraQuestionId",405);
        questionTitle = intent.getStringExtra("extraTitle");
        viewCount = intent.getIntExtra("extraViewCount",407);
        //answersCount = intent.getIntExtra("extraAnswersCount",406);

        StringBuffer Address = new StringBuffer(originAddress);
        Address.append(questionId);
        Address.append(timeOrderAddress);//按发布时间排序
        NewAddress = new String(Address);
        Log.d("AnswersListActivity","NewAddress is "+NewAddress);
    }




    private void sendRequestWithAnswerList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(NewAddress)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("AnswersListActivity","responseData is "+responseData);
                    parseJSONWithGson(responseData);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }


    private void parseJSONWithGson(String jsonData){

        Gson gson = new Gson();
        Type type = new TypeToken<AnswersListReturnData>() {}.getType();
        answersListReturnData = gson.fromJson(jsonData, type);
        Log.d("AnswersListActivity", "answersListReturnData is " + answersListReturnData.toString());
        List<SingleAnswerData> answersList = answersListReturnData.getData();//问题列表提取成功

        answerItemList.clear();
        for(int i = 0; i< answersListReturnData.getTotal(); i++){
            AnswerItem answerItem = new AnswerItem(answersList.get(i).getId(), answersList.get(i).getAnswerer().getId(),answersList.get(i).getAnswerer().getName(),R.drawable.head,answersList.get(i).getContent(),answersList.get(i).getSupportersCount(),answersList.get(i).getVoted());//content为后端的回答，等于answer
            answerItemList.add(answerItem);
            Log.d("AnswersListActivity"," answerItemList.toString() "+answerItem.getAnswer());
        }



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(answersListReturnData.getMessage().equals("success")) {
                    textViewTitle.setText(questionTitle);
                    textViewViews.setText(viewCount+"浏览");
                    textViewAnswers.setText(mAnswerAdapter.getItemCount()+"回答");
                    mAnswerAdapter.notifyDataSetChanged();
                }

            }
        });
    }



    protected void onResume() {
        super.onResume();
        //显示信息的界面
        setContentView(R.layout.activity_answers_list);
        initView();
        initEvent();
        initDataIn();
        sendRequestWithAnswerList();
        mAnswerAdapter = new AnswerAdapter(answerItemList);
        mAnswerRecyclerView.setAdapter(mAnswerAdapter);
        mAnswerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//划线
    }










}