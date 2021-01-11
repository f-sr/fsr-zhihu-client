package com.example.client_zhihu_fsr.ReturnData;

public class LoginReturnData {
    private String message;
    private int status;
    private String token;
    private int uid;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public int getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "LoginReturnData{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", uid=" + uid +
                '}';
    }
}
