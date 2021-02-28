package com.example.client_zhihu_fsr.ReturnData;

public class SelfIntroductionReturnData {
    Data data;
    String message;
    int status;


    public static class Data {
        int id;
        String name;
        int gender;
        String desc;
        User user;
        String createdAt;
        String updatedAt;

        public static class User {
            int id;
            String mail;
            String password;
            String phone;

            public int getId() {
                return id;
            }

            public String getMail() {
                return mail;
            }

            public String getPassword() {
                return password;
            }

            public String getPhone() {
                return phone;
            }
        }
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getGender() {
            return gender;
        }

        public String getDesc() {
            return desc;
        }

        public User getUser() {
            return user;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
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
