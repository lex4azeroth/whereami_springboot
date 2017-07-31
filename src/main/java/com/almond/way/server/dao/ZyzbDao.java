package com.almond.way.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.almond.way.server.model.ZYZB;

public interface ZyzbDao {

	List<ZYZB> getZyzbList(@Param("from") String from, @Param("to") String to);
}
