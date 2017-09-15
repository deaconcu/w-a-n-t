package com.prosper.want.common.service;

import com.prosper.want.common.bean.AuthInfo;
import com.prosper.want.common.exception.NeedAuthorizationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deacon on 2017/9/9.
 * 1. 首次登陆时下发：
 *    {
 *        sessionToken: md5(userId + password + salt + loginTime)
 *        refreshToken: md5(userId + password + salt + expireTime)
 *        expireTime: xxx
 *    }
 * 2. 用户header中提交userId, sessionToken, 服务端校验
 * 3. refreshToken只能用一次
 * 4. 客户端如果发现sessionToken过期，需要提交refreshToken获取
 */
@Component
public class AuthService {

    private Pattern pattern = Pattern.compile("^user_id=(\\d+)&session_token=(\\w+)$");

    public AuthInfo checkValid(String authString) {
        try {
            Matcher m = pattern.matcher(authString);
            if (m.find()) {
                int userId = Integer.parseInt(m.group(1));
                String sessionToken = m.group(2);
                AuthInfo authInfo = new AuthInfo(userId, sessionToken, null);
                checkValid(authInfo);
                return authInfo;
            } else {
                throw new NeedAuthorizationException();
            }
        } catch (Exception e) {
            throw new NeedAuthorizationException(e);
        }
    }

    public void checkValid(AuthInfo authInfo) {
        if (!isValid(authInfo)) {
            throw new NeedAuthorizationException();
        }
    }

    public boolean isValid(AuthInfo authInfo) {
        // TODO
        return true;
    }
}
