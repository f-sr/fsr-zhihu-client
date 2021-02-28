package com.example.client_zhihu_fsr.ReturnData;

import java.util.List;

public class QuestionListReturnData {
    private List<SingleQuestionData> data;
    private String message;
    private int status;
    private int total;



    public List<SingleQuestionData> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "HomeReturnData{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
