package com.example.client_zhihu_fsr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private List<Content> mContentList;

    //内部类，基本数据结构
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentTitle;
        ImageView contentHeadImage;
        TextView contentName;
        TextView contentDescribe;
        TextView contentAgreeNumber;
        TextView contentCommentNumber;


        public ViewHolder(View view){
            super(view);
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = mContentList.get(position);
        holder.contentTitle.setText(content.getTitle());
        holder.contentHeadImage.setImageResource(content.getHeadImageId());
        holder.contentName.setText(content.getName());
        holder.contentDescribe.setText(content.getDescribe());
        holder.contentAgreeNumber.setText(content.getAgreeNumber());
        holder.contentCommentNumber.setText(content.getCommentNumber());
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}
