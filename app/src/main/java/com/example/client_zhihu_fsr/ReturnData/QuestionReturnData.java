package com.example.client_zhihu_fsr.ReturnData;

public class QuestionReturnData {

    private SingleQuestionData data;
    private String message;
    private int status;


    public SingleQuestionData getData() { return data; }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PublishReturn_data{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
