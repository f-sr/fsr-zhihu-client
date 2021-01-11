package com.example.client_zhihu_fsr.ReturnData;

public class SingleAnswerData {

    private int id;
    private String content;
    private String questionTitle;
    private Answerer answerer;
    private int supportersCount;
    private int voted;
    private String createdAt;
    private String updatedAt;

    public static class Answerer {
        private int id;
        private String name;
        private String desc;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "Answerer{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public Answerer getAnswerer() {
        return answerer;
    }

    public int getSupportersCount() {
        return supportersCount;
    }

    public int getVoted() {
        return voted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public String toString() {
        return "SingleAnswerData{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", answerer=" + answerer +
                ", supportersCount=" + supportersCount +
                ", voted=" + voted +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
