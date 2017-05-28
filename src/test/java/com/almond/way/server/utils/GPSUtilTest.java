package com.almond.way.server.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.almond.way.server.model.LaL;

public class GPSUtilTest {
	
	@Test
	public void testBD2WGS() {
		LaL actualWGS = GPSUtil.BD2WGS(31.284043, 121.323474);
		LaL expectedWGS = new LaL(121.31256018996311, 31.279614976933726);
		assertEquals(expectedWGS, actualWGS);
	}
	
	@Test
	public void testBD2WGSFailed() {
		LaL actualWGS = GPSUtil.BD2WGS(31.2840423, 121.323474);
		LaL expectedWGS = new LaL(121.31256018996311, 31.279614976933726);
		assertNotEquals(expectedWGS, actualWGS);
	}
}
