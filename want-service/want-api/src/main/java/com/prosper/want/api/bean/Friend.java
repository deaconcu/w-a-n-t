package com.prosper.want.api.bean;

/**
 * Created by deacon on 2017/6/12.
 */
public class Friend {

    private int id;
    private int friendFrom;
    private int friendTo;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendFrom() {
        return friendFrom;
    }

    public void setFriendFrom(int friendFrom) {
        this.friendFrom = friendFrom;
    }

    public int getFriendTo() {
        return friendTo;
    }

    public void setFriendTo(int friendTo) {
        this.friendTo = friendTo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
