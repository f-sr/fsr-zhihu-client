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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }
}
