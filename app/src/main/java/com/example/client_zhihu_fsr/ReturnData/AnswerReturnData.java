package com.example.client_zhihu_fsr.ReturnData;

public class AnswerReturnData {
    private String message;
    private int status;
    private SingleAnswerData data;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public SingleAnswerData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "AnswerReturnData{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
