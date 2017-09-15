package com.prosper.want.api.bean;

import com.prosper.want.common.validation.*;

/**
 * Created by deacon on 2017/6/12.
 */
public class Want {
    @IsInt(min=1)
    private int id;
    @IsString(maxLength = 100)
    private String title;
    @IsString(minLength = 10, maxLength = 2000)
    private String content;
    @IsInt(max=999)
    private int attendance;
    @IsDate(laterOn = true)
    private String scheduleTime;
    @IsString(empty = true, minLength = 1, maxLength = 100)
    private String scheduleLocation;
    @IsLocationPoint
    private String scheduleLocationPoint;
    private int userId;
    private int state;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleLocation() {
        return scheduleLocation;
    }

    public void setScheduleLocation(String scheduleLocation) {
        this.scheduleLocation = scheduleLocation;
    }

    public String getScheduleLocationPoint() {
        return scheduleLocationPoint;
    }

    public void setScheduleLocationPoint(String scheduleLocationPoint) {
        this.scheduleLocationPoint = scheduleLocationPoint;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
