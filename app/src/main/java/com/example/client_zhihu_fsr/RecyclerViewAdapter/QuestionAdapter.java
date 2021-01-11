package com.example.client_zhihu_fsr.RecyclerViewAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_zhihu_fsr.Activity.MyQuestionActivity;
import com.example.client_zhihu_fsr.Activity.OthersQuestionActivity;
import com.example.client_zhihu_fsr.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<QuestionItem> mQuestionItemList;

    //内部类，基本数据结构
    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView itemTitle;
        ImageView itemHeadImage;
        TextView itemName;
        TextView itemDescribe;
        TextView itemViewCount;
        TextView itemAnswersCount;

        //内部类构造函数，传入布局
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemTitle =(TextView)view.findViewById(R.id.tv_Title);
            itemHeadImage =(ImageView) view.findViewById(R.id.im_head);
            itemName =(TextView)view.findViewById(R.id.tv_name);
            itemDescribe =(TextView)view.findViewById(R.id.tv_describe);
            itemViewCount =(TextView)view.findViewById(R.id.tvViews);
            itemAnswersCount =(TextView)view.findViewById(R.id.tvAnswersCount);
        }
    }



    //适配器构造函数
    public QuestionAdapter(List<QuestionItem> questionItemList) {
        mQuestionItemList = questionItemList;
    }



    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //对一个item设置监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                QuestionItem questionItem = mQuestionItemList.get(position);

                SharedPreferences sp = v.getContext().getSharedPreferences("loginToken",0);
                int uId = sp.getInt("uid",0);

                if(uId== questionItem.getuId()){
                    //表示点进的是自己发布的问题
                    Log.d("ContentAdapter","questionId is "+ questionItem.getQuestionId());
                    int questionId = questionItem.getQuestionId();
                    Intent intent =new Intent(v.getContext(), MyQuestionActivity.class);
                    intent.putExtra("extra_QuestionId",questionId);
                    v.getContext().startActivity(intent);
                    //Toast.makeText(v.getContext(),"you clicked your question ",Toast.LENGTH_SHORT).show();//测试用，提示

                }else{//点进了别人发布的问题
                    Log.d("ContentAdapter","questionId is "+ questionItem.getQuestionId());
                    int questionId = questionItem.getQuestionId();
                    Intent intent_others =new Intent(v.getContext(), OthersQuestionActivity.class);
                    intent_others.putExtra("extra_QuestionId",questionId);
                    v.getContext().startActivity(intent_others);
                    Toast.makeText(v.getContext(),"you clicked others' question ",Toast.LENGTH_SHORT).show();//测试用，
                }

                //这里暂且只是实现某一个问题的详细展示，具体数据有待确定

            }
        });
        return holder;
    }



    //传展示的信息 到 界面
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ContentAdapter","position"+position);
        QuestionItem questionItem = mQuestionItemList.get(position);
        holder.itemTitle.setText(questionItem.getTitle());
        holder.itemHeadImage.setImageResource(questionItem.getHeadImageId());
        holder.itemName.setText(questionItem.getName());
        holder.itemDescribe.setText(questionItem.getDescribe());
        holder.itemViewCount.setText(questionItem.getViewsCount()+"浏览");
        holder.itemAnswersCount.setText(questionItem.getAnswersCount()+"回答");
    }



    //对item计数
    @Override
    public int getItemCount() {
        return mQuestionItemList.size();
    }

    public long getItemId(int position) {
        return position;
    }
}
