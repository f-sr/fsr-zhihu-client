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

import com.example.client_zhihu_fsr.Activity.AnswerActivity;
import com.example.client_zhihu_fsr.R;


import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<AnswerItem> mAnswerItemList;

    //内部类，基本数据结构
    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView itemAnswer;
        ImageView itemHeadImage;
        TextView itemName;

        //内部类构造函数，传入布局
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemAnswer =(TextView)view.findViewById(R.id.tvAnswer);
            itemHeadImage =(ImageView) view.findViewById(R.id.imHead);
            itemName =(TextView)view.findViewById(R.id.tvName);
        }
    }



    //适配器构造函数
    public AnswerAdapter(List<AnswerItem> answerItemList) {
        mAnswerItemList = answerItemList;
    }



    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //对一个item设置监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                AnswerItem answerItem = mAnswerItemList.get(position);
                boolean isMine;
                int answerID=answerItem.getAnswerId();
                Log.d("AnswerAdapter","answerID ="+answerID);

                SharedPreferences sp = v.getContext().getSharedPreferences("loginToken",0);
                int uId = sp.getInt("uid",0);
                Log.d("AnswerAdapter","uid/AnswerUId is    "+uId+"/"+answerItem.getAnswererId());
                if(uId == answerItem.getAnswererId()) {

                    //表示点进的是自己的回答(可编辑)
                    String answer = answerItem.getAnswer();
                    isMine = true;
                    Intent intent =new Intent(v.getContext(), AnswerActivity.class);
                    intent.putExtra("extraAnswer",answer);
                    intent.putExtra("extraAnswerIsMine",isMine);
                    intent.putExtra("extraAnswerId",answerID);
                    v.getContext().startActivity(intent);

                    Toast.makeText(v.getContext(),"you clicked your answer ", Toast.LENGTH_SHORT).show();//测试用，提示
                }else {
                    //表示点进的是别人的回答(不可编辑)
                    String answer = answerItem.getAnswer();
                    isMine = false;
                    Intent intent =new Intent(v.getContext(), AnswerActivity.class);
                    intent.putExtra("extraAnswer",answer);
                    intent.putExtra("extraAnswerIsMine",isMine);
                    intent.putExtra("extraAnswerId",answerID);
                    v.getContext().startActivity(intent);

                    Toast.makeText(v.getContext(),"you clicked others answer ",Toast.LENGTH_SHORT).show();//测试用，提示
                }

            }
        });
        return holder;
    }



    //传展示的信息 到 界面
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AnswerItem answerItem = mAnswerItemList.get(position);
        holder.itemAnswer.setText(answerItem.getAnswer());
        holder.itemHeadImage.setImageResource(answerItem.getHeadImageId());
        holder.itemName.setText(answerItem.getName());

    }



    //对item计数
    @Override
    public int getItemCount() {
        return mAnswerItemList.size();
    }

    public long getItemId(int position) {
        return position;
    }
}
