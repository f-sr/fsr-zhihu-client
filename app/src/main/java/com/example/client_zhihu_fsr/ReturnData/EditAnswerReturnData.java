package com.example.client_zhihu_fsr.ReturnData;

public class EditAnswerReturnData {
    private String message;
    private int status;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "EditAnswerReturnData{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
