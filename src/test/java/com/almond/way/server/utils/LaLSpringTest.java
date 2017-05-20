package com.almond.way.server.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.LaL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LaLSpringTest {
	
	@Test
	public void testGetLalPointsForLine1() {
		List<LaL> lals = LaLUtil.getLalPoints(1);
		assertTrue(lals.size() == 17);
	}
	
	@Test
	public void testGetLalPointsForLine2() {
		List<LaL> lals = LaLUtil.getLalPoints(2);
		assertTrue(lals.size() == 17 * 2);
	}
	
	@Test
	public void testGetLalPointsForLine25() {
		List<LaL> lals = LaLUtil.getLalPoints(25);
		Assert.assertTrue(lals.size() == 13);
	}
	
	@Test(expected = WhereAmIException.class)
	public void testGetLalPointsForLin26() {
		try {
			LaLUtil.getLalPoints(26);
		} catch (WhereAmIException e) {
			assertEquals(String.format(LaLUtil.NO_MORE_LINES, 26), e.getMessage());
			throw e;
		}
	}
}
