package com.prosper.want.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsString {
	// 是否允许为空字符串
	boolean empty() default false;
	int minLength() default 0;
	int maxLength() default 100000;
}
