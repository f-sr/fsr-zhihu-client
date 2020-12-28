package com.example.client_zhihu_fsr;

import java.util.List;

public class HomeReturn_data {
    private List<Data_data> data;
    private String message;
    private int status;
    private int total;


    public List<Data_data> getList_data() {
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
