package com.example.client_zhihu_fsr.RecyclerViewAdapter;

public class QuestionItem {

    private int uId;
    private int questionId;
    private String title;
    private String describe;
    private String name;
    private int headImageId;
    private int ViewsCount;
    private int AnswersCount;



    public QuestionItem(int uId, int questionId, String title, int headImageId, String name, String describe, int ViewsCount, int AnswersCount){
        this.uId=uId;
        this.title=title;
        this.describe=describe;
        this.name = name;
        this.headImageId =headImageId;
        this.ViewsCount = ViewsCount;
        this.AnswersCount = AnswersCount;
        this.questionId = questionId;
    }

    public int getuId() {
        return uId;
    }

    public String getTitle(){
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public String getName() {
        return name;
    }

    public int getHeadImageId() {
        return headImageId;
    }

    public int getViewsCount() {
        return ViewsCount;
    }

    public int getAnswersCount() {
        return AnswersCount;
    }

    public int getQuestionId() {
        return questionId;
    }

    @Override
    public String toString() {
        return "Content{" +
                "uId=" + uId +
                ", questionId=" + questionId +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", name='" + name + '\'' +
                ", headImageId=" + headImageId +
                ", agreeNumber='" + ViewsCount + '\'' +
                ", commentNumber='" + AnswersCount + '\'' +
                '}';
    }
}
