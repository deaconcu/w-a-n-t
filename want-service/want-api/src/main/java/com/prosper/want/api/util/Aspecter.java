package com.prosper.want.api.util;

import ch.qos.logback.classic.Logger;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.common.anotation.NeedLogin;
import com.prosper.want.common.exception.ExceptionHandlerMap;
import com.prosper.want.common.exception.NeedAuthorizationException;
import com.prosper.want.common.util.CommonConfig;
import com.prosper.want.common.util.CommonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class Aspecter {

    private Logger log = (Logger)LoggerFactory.getLogger(Aspecter.class);

    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private ExceptionHandlerMap exceptionHandlerMap;
    @Autowired
    private Jedis jedis;
    @Autowired
    private UserMapper user;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object doControllerAround(ProceedingJoinPoint pjp) throws Throwable {
        Map<String, Object> response = null;
        try {
            Object[] args = pjp.getArgs();
            long startTime = System.currentTimeMillis();

            MethodSignature signature = (MethodSignature) pjp.getSignature(); 
            Method method = signature.getMethod();

            Class<?> clazz = pjp.getTarget().getClass();
            if (clazz.isAnnotationPresent(NeedLogin.class) || method.isAnnotationPresent(NeedLogin.class)) {
                ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = sra.getRequest();

                String userId = request.getHeader("userId");
                String postSessionId = request.getHeader("sessionId");
                String sessionId = jedis.get(Constant.CacheName.session + userId);
                if (sessionId == null || !sessionId.equals(postSessionId)) {
                    throw new NeedAuthorizationException();
                }
            }

            Object dataResponse = pjp.proceed(args);

            response = new HashMap<String, Object>();
            if (dataResponse != null) {
                response.put("data", dataResponse);
            }

            response.put("cost", (double)(System.currentTimeMillis() - startTime) / 1000);
            response.put("code", Constant.OpCode.SUCCESS);
        } catch (Exception e) {
            Method method = exceptionHandlerMap.getMethod(e.getClass());
            if (method != null) {
                Object errorResponse = method.invoke(exceptionHandlerMap.getExceptionHandler(method), e);
                return errorResponse;
            }
        }
        return response;
    }

    @Around("@within(org.springframework.scheduling.annotation.Scheduled) || "
            + "@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void doTaskAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            pjp.proceed();
        } catch (Exception e) {
            log.error("batch operation failed, must check for exception, "
                    + "exception:\n" + CommonUtil.getStackTrace(e));
        }
    }

}
