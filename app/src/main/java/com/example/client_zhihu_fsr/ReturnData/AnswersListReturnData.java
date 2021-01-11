package com.example.client_zhihu_fsr.ReturnData;

import java.util.List;

public class AnswersListReturnData {
    private List<SingleAnswerData> data;
    private String message;
    private int status;
    private int total;

    public List<SingleAnswerData> getData() {
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
        return "AnswersListReturnData{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
