package com.prosper.want.common.aspect;

import com.prosper.want.common.annotation.AuthCheck;
import com.prosper.want.common.bean.AuthInfo;
import com.prosper.want.common.exception.ExceptionHandlerMap;
import com.prosper.want.common.exception.NeedAuthorizationException;
import com.prosper.want.common.service.AuthService;
import com.prosper.want.common.util.CommonConstant;
import com.prosper.want.common.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by deacon on 2017/9/9.
 */
@Aspect
@Component
public class ControllerAspect {

    private static Logger LOG = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private ExceptionHandlerMap exceptionHandlerMap;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object doControllerAround(ProceedingJoinPoint pjp) throws Exception {
        long startTime = System.currentTimeMillis();
        Object response = null;
        try {
            Object[] args = pjp.getArgs();
            Object[] argsModified = new Object[args.length];

            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Class<?> clazz = method.getDeclaringClass();

            if (clazz.isAnnotationPresent(AuthCheck.class) || method.isAnnotationPresent(AuthCheck.class)) {
                HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authString = request.getHeader("Auth");
                AuthInfo info = authService.checkValid(authString);

                request.setAttribute("authInfo", info);
                int i = 0;
                for (Object o: args) {
                    if (o instanceof AuthInfo) {
                        argsModified[i++] = info;
                    } else {
                        argsModified[i++] =  o;
                    }
                }
            } else {
                argsModified = args;
            }
            response = pjp.proceed(argsModified);

            Map<String, Object> castResponse;
            if (response == null) {
                response = new HashMap<String, Object>();
            }

            if (response instanceof Map) {
                castResponse = (Map<String, Object>) response;
                castResponse.put("cost", (double)(System.currentTimeMillis() - startTime) / 1000);
                castResponse.put("code", CommonConstant.ResponseCode.SUCCESS);
            }
        } catch (Throwable e) {
            Method method = exceptionHandlerMap.getMethod(e.getClass());
            if (method != null) {
                Object errorResponse = method.invoke(exceptionHandlerMap.getExceptionHandler(method), e);
                return errorResponse;
            }
        }
        long cost = System.currentTimeMillis() - startTime;
        if (cost > 100) {
            LOG.warn("execute time exceed 100ms, cost:" + Long.toString(cost) + ", method:" +
                    pjp.getSignature().getDeclaringTypeName() + ":" + pjp.getSignature().getName());
        }
        return response;
    }
}
