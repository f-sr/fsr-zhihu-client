package com.example.client_zhihu_fsr.ReturnData;

public class RegisterReturnData {
    private String message;
    private int status;


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RegisterReturn_data{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
