package com.example.client_zhihu_fsr.RecyclerViewAdapter;

public class AnswerItem {

    private int answererId;
    private String name;
    private int headImageId;
    private String answer ;

    public AnswerItem(String name, int headImageId, String answer ,int answererId) {
        this.name = name;
        this.headImageId = headImageId;
        this.answer = answer;
        this.answererId = answererId;
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

    @Override
    public String toString() {
        return "AnswerItem{" +
                "answererId=" + answererId +
                ", name='" + name + '\'' +
                ", headImageId=" + headImageId +
                ", answer='" + answer + '\'' +
                '}';
    }
}
