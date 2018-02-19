package com.almond.way.server.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.LaL;

@RunWith(SpringRunner.class)
public class LaLSpringTest {
	
	@Test
	public void testGetLalPointsForLineXD01() {
		List<LaL> lals = LaLUtil.getLalPoints("XD01");
		assertTrue(lals.size() == 27);
	}
	
	@Test
	public void testGetLalPointsForLineSD03() {
		List<LaL> lals = LaLUtil.getLalPoints("SD03");
		assertTrue(lals.size() == 21);
	}
	
	@Test
	public void testGetLalPointsForLineXC09() {
		List<LaL> lals = LaLUtil.getLalPoints("XC09");
		Assert.assertTrue(lals.size() == 12);
	}
	
	@Test
	public void testGetLalPointsForLineXB10() {
		List<LaL> lals = LaLUtil.getLalPoints("XB10");
		Assert.assertTrue(lals.size() == 14);
	}
	
	@Test
	public void testGetLalPointsForLineXC26() {
		List<LaL> lals = LaLUtil.getLalPoints("XC26");
		Assert.assertTrue(lals.size() == 21);
	}
	
	@Test
	public void testGetLalPointsForLineSF01() {
		List<LaL> lals = LaLUtil.getLalPoints("SF01");
		Assert.assertTrue(lals.size() == 17);
	}
	
	@Test
	public void testGetLalPointsForLineSB08() {
		List<LaL> lals = LaLUtil.getLalPoints("SB08");
		Assert.assertTrue(lals.size() == 20);
	}
	
	@Test
	public void testGetLalPointsForLineSC10() {
		List<LaL> lals = LaLUtil.getLalPoints("SC10");
		Assert.assertTrue(lals.size() == 16);
	}
	
	@Test
	public void testGetLalPointsForLineXF08() {
		List<LaL> lals = LaLUtil.getLalPoints("XF08");
		Assert.assertTrue(lals.size() == 18);
	}
	
	@Test(expected=WhereAmIException.class)
	public void testNotFound() {
		try {
			LaLUtil.getLalPoints("WTF");
		} catch (WhereAmIException e) {
			Assert.assertEquals("GD {WTF} not found", e.getMessage());
			throw e;
		}
	}
	
}
