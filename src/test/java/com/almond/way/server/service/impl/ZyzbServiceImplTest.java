package com.almond.way.server.service.impl;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.almond.way.server.dao.ZyzbDao;
import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.service.ZyzbService;

@RunWith(MockitoJUnitRunner.class)
public class ZyzbServiceImplTest {

	@Mock
	private ZyzbDao zyzbDao;

	@InjectMocks
	private ZyzbService service = new ZyzbServiceImpl();

	private static final String EMPTY = "";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		zyzbDao = Mockito.mock(ZyzbDao.class);
	}

	@Test(expected = InvalidParameterException.class)
	public void testGetZyzbListWithEmptyFromAndTo() {
		try {
			service.getZyzbList(EMPTY, EMPTY);
		} catch (WhereAmIException e) {
			assertEquals(ZyzbService.INVALID_INPUT, e.getMessage());
			throw e;
		}
	}

	@Test(expected = InvalidParameterException.class)
	public void testGetZyzbListWithNull() {
		try {
			service.getZyzbList(null, null);
		} catch (WhereAmIException e) {
			assertEquals(ZyzbService.INVALID_INPUT, e.getMessage());
			throw e;
		}
	}

	@Test(expected = InvalidParameterException.class)
	public void testGetZyzbListWithEmptyFrom() {
		try {
			service.getZyzbList(null, "abc");
		} catch (WhereAmIException e) {
			assertEquals(ZyzbService.INVALID_INPUT, e.getMessage());
			throw e;
		}
	}

	@Test(expected = InvalidParameterException.class)
	public void testGetZyzbListWithEmptyTo() {
		try {
			service.getZyzbList("ddd", "");
		} catch (WhereAmIException e) {
			assertEquals(ZyzbService.INVALID_INPUT, e.getMessage());
			throw e;
		}
	}

	@Test(expected = WhereAmIException.class)
	public void testGetZyzbListNotFound() {
		try {
			String from = "from";
			String to = "to";
			Mockito.when(zyzbDao.getZyzbList(from, to)).thenReturn(null);
			service.getZyzbList(from, to);
		}
		catch (WhereAmIException e) {
			assertEquals(ZyzbService.NOTHING_FOUND, e.getMessage());
			throw e;
		}
	}
}
