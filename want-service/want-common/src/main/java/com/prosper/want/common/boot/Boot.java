package com.prosper.want.common.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prosper.want.common.exception.SpringExceptionHandlers;

public class Boot {

    private static final Logger log = LoggerFactory.getLogger(SpringExceptionHandlers.class);

    public static void main(String[] args) {
        String applicationClassName = System.getProperty("app");
        Object appObject = null;
        if (applicationClassName == null) {
            appObject = new DefaultSpringApplication();
        } else {
            try {
                Class<?> appClass = Class.forName(applicationClassName);
                appObject = appClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("cannot create application object", e);
            }
            
            if (!(appObject instanceof Application)) {
                throw new RuntimeException("application must extends com.prosper.want.common.boot.Application");
            }
        }
        ((Application)appObject).run(args);
    }
}