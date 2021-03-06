package com.prosper.want.common.validation;

import com.prosper.want.common.exception.InternalException;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class Validation {
	
	private static final Logger log = LoggerFactory.getLogger(Validation.class);

	public <T> T getObject(String s, Class<T> classType) {
		return getObject(s, classType, null, null);
	}

	/**
	 * 通过JSON串获取对象并校验字段
	 * @param s json串
	 * @param classType 对象类型
	 * @param fields 需要校验的字段
	 * @param fieldsExcluded 不需要校验的字段
	 * @return 对象
	 */
	public <T> T getObject(String s, Class<T> classType, String[] fields, String[] fieldsExcluded) {
		// get object from json
		T t = null;
		try {
			t = JsonUtil.getObject(s, classType);
		} catch (Exception e) {
			throw new InvalidArgumentException("input data is an invalid json string", e);
		}

		// validate
		List<Field> fieldList = new LinkedList<Field>();
		if (fields != null && fields.length > 0) {
			for (String fieldName : fields) {
				Field field;
				try {
					field = t.getClass().getDeclaredField(fieldName);
				} catch (Exception e) {
					log.warn("can't get field named:" + fieldName);
					continue;
				}
				fieldList.add(field);
			}
		} else {
			Field[] declaredFields = t.getClass().getDeclaredFields();
			List<String> fieldExcludedList = Arrays.asList(fieldsExcluded == null ? new String[]{} : fieldsExcluded);
			for (Field field: declaredFields) {
				if (!fieldExcludedList.contains(field.getName())) {
					fieldList.add(field);
				}
			}
		}

		for (Field field: fieldList) {
			Object fieldObject;
			String name = field.getName();
			
			try {
				field.setAccessible(true);
				fieldObject = field.get(t);
			} catch (Exception e) {
				throw new InternalException(e);
			}
			
			if (field.isAnnotationPresent(NotNull.class)) {
				checkNotNull(fieldObject, name);
			} else if (field.isAnnotationPresent(IsInt.class)) {
				long min = field.getAnnotation(IsInt.class).min();
				long max = field.getAnnotation(IsInt.class).max();
				checkIsInt(fieldObject, name, min, max);
			} else if (field.isAnnotationPresent(IsString.class)) {
				int min = field.getAnnotation(IsString.class).minLength();
				int max = field.getAnnotation(IsString.class).maxLength();
				boolean empty = field.getAnnotation(IsString.class).empty();
				checkIsString(fieldObject, name, empty, min, max);
			} else if (field.isAnnotationPresent(InInt.class)) {
				long[] value = field.getAnnotation(InInt.class).value();
				checkInInt(fieldObject, name, value);
			} else if (field.isAnnotationPresent(IsDate.class)) {
				boolean value = field.getAnnotation(IsDate.class).laterOn();
				checkIsDate(fieldObject, name, value);
			} else if (field.isAnnotationPresent(IsLocationPoint.class)) {
				checkIsLocationPoint(fieldObject, name);
			}
		}
		return t;
	}

	private void checkNotNull(Object object, String fieldName) {
		if (object == null) {
			throw new InvalidArgumentException("");
		}
	}

	private void checkIsInt(Object object, String fieldName, long min, long max) {
		long value = 0;
		if (object instanceof Long) {
			value = (Long)object;
		} else if (object instanceof Integer) {
			value = (int)object;
		} else if (object instanceof Short) {
			value = (short)object;
		} else {
			throw new InvalidArgumentException(fieldName + " is not exist or it is not a number");
		}
		
		if (value < min || value > max) {
			throw new InvalidArgumentException(fieldName + " is out of range, min:" + min + ", max:" + max);
		}
	}
	
	private void checkIsString(Object object, String fieldName, boolean empty, int min, int max) {
		if (object instanceof String) {
			String value = (String) object;
			int size = value.length();
			if (empty && size == 0) {
				return;
			}
			if (size < min || size > max) {
				throw new InvalidArgumentException(fieldName + " length is out of range, min:" + min + ", max:" + max);
			}
		} else {
			throw new InvalidArgumentException(fieldName + " is not exist or it is not a string");
		}
	}
	
	private void checkInInt(Object object, String fieldName, long[] intList) {
		long value = 0;
		if (object instanceof Long) {
			value = (Long)object;
		} else if (object instanceof Integer) {
			value = (int)object;
		} else if (object instanceof Short) {
			value = (short)object;
		} else {
			throw new InvalidArgumentException(fieldName + " is not exist or it is not a number");
		}

		Arrays.sort(intList);
		if (Arrays.binarySearch(intList, value) < 0) {
			throw new InvalidArgumentException(fieldName + " is not in array:" + Arrays.toString(intList));
		}
	}

	private void checkIsDate(Object fieldObject, String name, boolean value) {
		// TODO
	}

	private void checkIsLocationPoint(Object fieldObject, String name) {
	    // TODO
	}

	public enum VerifyType {		
		all, field, partial, none
	}
	
	/**
	 * 检查页数, 0 < page < 2000 && 0 < pageSize < 100
	 */
	public void checkPage(int page, int pageSize) {
		if (page <= 0 || page > 2000 || pageSize <= 0 || pageSize > 100) {
			throw new InvalidArgumentException("page or page size is invalid");
		}
	}

}














