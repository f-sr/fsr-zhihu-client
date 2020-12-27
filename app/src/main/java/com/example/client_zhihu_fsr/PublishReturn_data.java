package com.example.client_zhihu_fsr;

public class PublishReturn_data {
    private Data data;
    private String message;
    private int status;

    public static class Data{
        private int id;
        private String title;
        private String desc;
        private Questioner questioner;
        private int answerCount;
        private String createdTime;
        private String updatedTime;

        public static class Questioner{
            private int id_user;
            private String name;
            private String desc_user;

            public int getId_user() {
                return id_user;
            }

            public String getName() {
                return name;
            }

            public String getDesc_user() {
                return desc_user;
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

        public int getAnswerCount() {
            return answerCount;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }


    }


    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
