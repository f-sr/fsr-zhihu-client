package com.example.client_zhihu_fsr.ReturnData;

import java.util.List;

public class HomeReturnData {
    private List<SingleQuestionData> data;
    private String message;
    private int status;
    private int total;


    public List<SingleQuestionData> getList_data() {
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

}
