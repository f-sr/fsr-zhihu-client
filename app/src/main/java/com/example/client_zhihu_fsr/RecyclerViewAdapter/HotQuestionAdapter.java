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

import com.example.client_zhihu_fsr.Activity.QuestionActivity;
import com.example.client_zhihu_fsr.R;

import java.util.List;

public class HotQuestionAdapter extends RecyclerView.Adapter<HotQuestionAdapter.ViewHolder> {

    private List<HotQuestionItem> mHotQuestionItemList;

    //内部类，基本数据结构
    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView itemTitle;
        ImageView itemHeadImage;
        TextView itemName;
        TextView itemDescribe;
        TextView itemViewCount;
        TextView itemAnswersCount;
        TextView rank;
        TextView hot;

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
            rank =(TextView)view.findViewById(R.id.tvRank);
            hot =(TextView)view.findViewById(R.id.tvHot);
        }
    }



    //适配器构造函数
    public HotQuestionAdapter(List<HotQuestionItem> questionItemList) {
        mHotQuestionItemList = questionItemList;
    }



    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_question_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //对一个item设置监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isMyQuestion;

                int position = holder.getAdapterPosition();
                HotQuestionItem hotQuestionItem = mHotQuestionItemList.get(position);

                SharedPreferences sp = v.getContext().getSharedPreferences("loginToken",0);
                int uId = sp.getInt("uid",0);
                Log.d("ContentAdapter","uid/quesUid "+ uId+"/"+hotQuestionItem.getuId());

                if(uId== hotQuestionItem.getuId()){
                    //表示点进的是自己发布的问题
                    isMyQuestion = true;
                    Log.d("ContentAdapter","questionId is "+ hotQuestionItem.getQuestionId());
                    int questionId = hotQuestionItem.getQuestionId();
                    Intent intent =new Intent(v.getContext(), QuestionActivity.class);
                    intent.putExtra("extra_QuestionId",questionId);
                    intent.putExtra("extraIsMyQuestion",isMyQuestion);
                    v.getContext().startActivity(intent);
                    Toast.makeText(v.getContext(),"you clicked your question ",Toast.LENGTH_SHORT).show();//测试用，提示

                }else{//点进了别人发布的问题
                    isMyQuestion = false;
                    Log.d("ContentAdapter","questionId is "+ hotQuestionItem.getQuestionId());
                    int questionId = hotQuestionItem.getQuestionId();
                    Intent intent_others =new Intent(v.getContext(), QuestionActivity.class);
                    intent_others.putExtra("extra_QuestionId",questionId);
                    intent_others.putExtra("extraIsMyQuestion",isMyQuestion);
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
        HotQuestionItem hotQuestionItem = mHotQuestionItemList.get(position);
        holder.itemTitle.setText(hotQuestionItem.getTitle());
        holder.itemHeadImage.setImageResource(hotQuestionItem.getHeadImageId());
        holder.itemName.setText(hotQuestionItem.getName());
        holder.itemDescribe.setText(hotQuestionItem.getDescribe());
        holder.itemViewCount.setText(hotQuestionItem.getViewsCount()+"浏览");
        holder.itemAnswersCount.setText(hotQuestionItem.getAnswersCount()+"回答");
Log.d("HotQuestionAdapter","hotQuestionItem.getHot() = "+hotQuestionItem.getHot());
        holder.hot.setText("热度"+hotQuestionItem.getHot());
        holder.rank.setText(""+hotQuestionItem.getRank());
    }



    //对item计数
    @Override
    public int getItemCount() {
        return mHotQuestionItemList.size();
    }

    public long getItemId(int position) {
        return position;
    }
}
