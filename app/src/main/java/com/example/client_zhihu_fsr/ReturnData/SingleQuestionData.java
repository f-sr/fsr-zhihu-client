
package com.example.client_zhihu_fsr.ReturnData;

public class SingleQuestionData {

    private int id;
    private String title;
    private String desc;
    private Questioner questioner;
    private String createdAt;
    private String updatedAt;
    private int answersCount;
    private int viewCount;
    private int hot;

    public static class Questioner {
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
            return "Questioner{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Questioner getQuestioner() {
        return questioner;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getHot() {
        return hot;
    }

    @Override
    public String toString() {
        return "SingleQuestionData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", questioner=" + questioner +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", answersCount=" + answersCount +
                ", viewCount=" + viewCount +
                ", hot=" + hot +
                '}';
    }
}
