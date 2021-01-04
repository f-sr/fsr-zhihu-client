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

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private List<Content> mContentList;

    //内部类，基本数据结构
    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView contentTitle;
        ImageView contentHeadImage;
        TextView contentName;
        TextView contentDescribe;
        TextView contentAgreeNumber;
        TextView contentCommentNumber;

        //内部类构造函数，传入布局
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            contentTitle=(TextView)view.findViewById(R.id.tv_Title);
            contentHeadImage=(ImageView) view.findViewById(R.id.im_head);
            contentName=(TextView)view.findViewById(R.id.tv_name);
            contentDescribe=(TextView)view.findViewById(R.id.tv_describe);
            contentAgreeNumber=(TextView)view.findViewById(R.id.tv_agree);
            contentCommentNumber=(TextView)view.findViewById(R.id.tv_comment);
        }
    }



    //适配器构造函数
    public ContentAdapter(List<Content> ContentList) {
        mContentList=ContentList;
    }



    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //对一个item设置监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                Content content = mContentList.get(position);

                SharedPreferences sp = v.getContext().getSharedPreferences("loginToken",0);
                int uId = sp.getInt("uid",0);
                Log.d("ContentAdapter","you id is "+uId);
                Log.d("ContentAdapter","questioner id is "+uId);
                if(uId==content.getuId()){
                    //表示点进的是自己发布的问题
                    Log.d("ContentAdapter","questionId is "+ content.getQuestionId());
                    int questionId = content.getQuestionId();
                    Intent intent =new Intent(v.getContext(), MyQuestionActivity.class);
                    intent.putExtra("extra_QuestionId",questionId);
                    v.getContext().startActivity(intent);
                    //Toast.makeText(v.getContext(),"you clicked your question ",Toast.LENGTH_SHORT).show();//测试用，提示

                }else{//点进了别人发布的问题
                    Log.d("ContentAdapter","questionId is "+ content.getQuestionId());
                    int questionId = content.getQuestionId();
                    Intent intent_others =new Intent(v.getContext(), OthersQuestionActivity.class);
                    intent_others.putExtra("extra_QuestionId",questionId);
                    v.getContext().startActivity(intent_others);
                    //Toast.makeText(v.getContext(),"you clicked others' question ",Toast.LENGTH_SHORT).show();//测试用，
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
        Content content = mContentList.get(position);
        holder.contentTitle.setText(content.getTitle());
        holder.contentHeadImage.setImageResource(content.getHeadImageId());
        holder.contentName.setText(content.getName());
        holder.contentDescribe.setText(content.getDescribe());
        holder.contentAgreeNumber.setText(content.getAgreeNumber());
        holder.contentCommentNumber.setText(content.getCommentNumber());
    }



    //对item计数
    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    public long getItemId(int position) {
        return position;
    }
}
