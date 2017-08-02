package com.prosper.want.api.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MapperI<T> {
	
	T selectOne(@Param("id") Object id);
	
	List<T> selectListByPage(@Param("limit") int limit, @Param("offset") int offset);
	
	List<T> selectListByIds(@Param("ids") List<Integer> ids);
	
	long countByIds(@Param("ids") List<Integer> ids);

	long insert(T t);
	
	void update(T t);

	void delete(long id);
	
	void deleteAll();
	
}
