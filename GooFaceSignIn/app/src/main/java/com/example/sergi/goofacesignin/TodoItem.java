package com.example.sergi.goofacesignin;

/**
 * Created by Sergi on 27/01/2017.
 */

public class TodoItem {
    String title;
    String data;

    public TodoItem(String title, String data){
        this.title = title; this.data=data;
    }

    public String getTitle(){
        return title;
    }
    public String getData(){
        return data;
    }
}
