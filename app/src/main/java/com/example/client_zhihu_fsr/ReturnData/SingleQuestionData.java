
package com.example.client_zhihu_fsr.ReturnData;

public class SingleQuestionData {

        private int id;
        private String title;
        private String desc;
        private Questioner questioner;
        private String createdAt;
        private String updatedAt;
        private int answersCount;

        public static class Questioner{
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

        public int getAnswersCount() { return answersCount; }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }


        public String toString(){
            return "0000000000000000000000000000000000";
        }



}
