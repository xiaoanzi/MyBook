package com.app.mybook.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/13.
 */
@Table(name = "rating")
public class Rating extends Model implements Serializable{
    @Column(name = "numRaters")
    private String numRaters;   //图书的评论人数
    @Column(name = "average")
    private String average; //图书的评分

    public Rating(){
        super();
    }
    public String getNumRaters() {
        return numRaters;
    }

    public void setNumRaters(String numRaters) {
        this.numRaters = numRaters;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
