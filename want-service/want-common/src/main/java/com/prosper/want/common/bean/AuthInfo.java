package com.prosper.want.common.bean;

import sun.net.www.protocol.http.AuthenticationHeader;

/**
 * Created by deacon on 2017/9/9.
 */
public class AuthInfo {
    private int userId;
    private String sessionToken;
    private String refreshToken;

    public AuthInfo() {}

    public AuthInfo(int userId, String sessionToken, String refreshToken) {
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.refreshToken = refreshToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
