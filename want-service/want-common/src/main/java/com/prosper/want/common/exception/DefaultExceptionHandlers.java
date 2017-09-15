package com.prosper.want.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prosper.want.common.util.CommonUtil;
import com.prosper.want.common.util.Lang;
import com.prosper.want.common.util.CommonConstant.ResponseCode;

@Component
public class DefaultExceptionHandlers {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandlers.class);
	
	@Autowired
	private Lang lang;
	
	@ExceptionHandler(InvalidArgumentException.class)
	public ExceptionResponse handleII(Exception e) {
		return new ExceptionResponse(
				ResponseCode.INVALID_PARAMS,
				lang.getLang(e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
    }
	
	@ExceptionHandler(ResourceNotExistException.class)
	public ExceptionResponse handleRNFE(Exception e) {
		return new ExceptionResponse(
				ResponseCode.RESOURCE_NOT_FOUND,
				lang.getLang(e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
    }
	
	@ExceptionHandler(OperationNotAllowedException.class)
	public ExceptionResponse handleONAE(Exception e) {
		return new ExceptionResponse(
				ResponseCode.NOT_PERMITED,
				lang.getLang(e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
    }
	
	@ExceptionHandler(NeedAuthorizationException.class)
	public ExceptionResponse handleNAE(NeedAuthorizationException e) {
	    return new ExceptionResponse(
	    		ResponseCode.NEED_AUTHORIZATION, lang.getLang("NEED_LOGIN"));
    }
	
	@ExceptionHandler(Exception.class)
	public ExceptionResponse handleE(Exception e) {
		log.error("operation failed, " + "exception:\n" + CommonUtil.getStackTrace(e));
		return new ExceptionResponse(
				ResponseCode.INTERNAL_EXCEPTION,
				lang.getLang("INTERNAL_EXCEPTION") + ":" +
						lang.getLang(e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
    }
	
}











