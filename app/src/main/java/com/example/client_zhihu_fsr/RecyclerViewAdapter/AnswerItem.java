package com.example.client_zhihu_fsr.RecyclerViewAdapter;

public class AnswerItem {

    private int supportersCount;
    private int voted;
    private int answererId;
    private String name;
    private int headImageId;
    private String answer ;
    private int answerId;


    public AnswerItem( int answerId , int answererId, String name, int headImageId, String answer,int supportersCount, int voted) {
        this.answerId = answerId;
        this.supportersCount = supportersCount;
        this.voted = voted;
        this.answererId = answererId;
        this.name = name;
        this.headImageId = headImageId;
        this.answer = answer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public String getName() {
        return name;
    }

    public int getHeadImageId() {
        return headImageId;
    }

    public String getAnswer() {
        return answer;
    }

    public int getAnswererId() {
        return answererId;
    }


    public int getSupportersCount() {
        return supportersCount;
    }

    public int getVoted() {
        return voted;
    }

    @Override
    public String toString() {
        return "AnswerItem{" +
                "supportersCount=" + supportersCount +
                ", voted=" + voted +
                ", answererId=" + answererId +
                ", name='" + name + '\'' +
                ", headImageId=" + headImageId +
                ", answer='" + answer + '\'' +
                ", answerId=" + answerId +
                '}';
    }
}
