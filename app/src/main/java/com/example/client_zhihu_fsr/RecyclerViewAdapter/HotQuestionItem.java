package com.example.client_zhihu_fsr.RecyclerViewAdapter;

public class HotQuestionItem {

    private int uId;
    private int questionId;
    private String title;
    private String describe;
    private String name;
    private int headImageId;
    private int ViewsCount;
    private int AnswersCount;
    private int hot;
    private int rank;


    public HotQuestionItem(int uId, int questionId, String title,  int headImageId,String name, String describe,int viewsCount, int answersCount,int hot, int rank) {
        this.uId = uId;
        this.questionId = questionId;
        this.title = title;
        this.describe = describe;
        this.name = name;
        this.headImageId = headImageId;
        ViewsCount = viewsCount;
        AnswersCount = answersCount;
        this.hot = hot;
        this.rank = rank;

    }

    public int getuId() {
        return uId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getTitle() {
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

    public int getRank() {
        return rank;
    }

    public int getHot() {
        return hot;
    }


    @Override
    public String toString() {
        return "HotQuestionItem{" +
                "uId=" + uId +
                ", questionId=" + questionId +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", name='" + name + '\'' +
                ", headImageId=" + headImageId +
                ", ViewsCount=" + ViewsCount +
                ", AnswersCount=" + AnswersCount +
                ", hot=" + hot +
                ", rank=" + rank +
                '}';
    }
}
