package com.example.client_zhihu_fsr;

public class Content {

    private String title;
    private String describe;
    private String name;
    private int headImageId;
    private String agreeNumber;
    private String commentNumber;


    public Content(String title,int headImageId,String name,String describe,String agreeNumber,String commentNumber){
        this.title=title;
        this.describe=describe;
        this.name = name;
        this.headImageId =headImageId;
        this.agreeNumber = agreeNumber;
        this.commentNumber = commentNumber;
    }

    public String getTitle(){
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public String getName() {
        return name;
    }

    public int getHeadImageId() {
        return headImageId;
    }

    public String getAgreeNumber() {
        return agreeNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }
}
