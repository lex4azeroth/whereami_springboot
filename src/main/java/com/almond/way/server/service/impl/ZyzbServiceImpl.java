package com.almond.way.server.service.impl;

import java.security.InvalidParameterException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.almond.way.server.dao.ZyzbDao;
import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.ZYZB;
import com.almond.way.server.service.ZyzbService;

@Service
public class ZyzbServiceImpl implements ZyzbService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private ZyzbDao zyzbDao;
	
	@Override
	public List<ZYZB> getZyzbList(String from, String to) {
		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
			throw new InvalidParameterException(INVALID_INPUT);
		}
		
		List<ZYZB> zyzbList = zyzbDao.getZyzbList(from, to);
		
		if (zyzbList.size() <= 0) {
			logger.error(NOTHING_FOUND);
			throw new WhereAmIException(NOTHING_FOUND);
		}
		
		return zyzbDao.getZyzbList(from, to);
	}
}
