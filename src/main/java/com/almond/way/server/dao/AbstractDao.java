package com.almond.way.server.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao extends SqlSessionDaoSupport{  
	
    @Autowired  
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){  
          
        super.setSqlSessionFactory(sqlSessionFactory);  
    }  
      
} 
