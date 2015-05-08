package com.app.mybook.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/14.
 */
@Table(name = "authorUser")
public class AuthorUser extends Model implements Serializable{
    @Column(name = "authorUserId")
    private String authorUserId;
    @Column(name = "name")
    private String name;
    @Column(name = "avatar")
    private String avatar;  //用户头像

    public String getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(String authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
