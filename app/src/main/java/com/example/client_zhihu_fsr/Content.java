package com.example.client_zhihu_fsr;

public class Content {

    private String title;
    private String comment;
    private String name;
    private int headImageId;


    public Content(String title,int headImageId,String name,String comment){
        this.title=title;
        this.comment=comment;
        this.name = name;
        this.headImageId =headImageId;
    }

    public String getTitle(){
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return headImageId;
    }
}
