package com.almond.way.server.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

import com.almond.way.server.model.LaL;
import com.almond.way.server.model.Line;

public class LaLUtilTest {

	private static final String LALS_FILE = "mock.lals.properties";

	@Test
	public void testGetDistance() {
		LaL from = new LaL(121.323258f, 31.284051f);
		LaL to = new LaL(121.324309f, 31.28385f);
		assertTrue(LaLUtil.getDistance(from, to) == LaLUtil.getDistance(to, from));
	}

	@Test
	public void testGetDistanceFailed() {
		LaL from = new LaL(121.323258f, 31.284051f);
		LaL to = new LaL(121.324309f, 31.28385f);

		LaL from2 = new LaL(111.3232581f, 22.284051f);
		LaL to2 = new LaL(121.324309f, 31.28385f);

		assertFalse(LaLUtil.getDistance(from, to) == LaLUtil.getDistance(to2, from2));
	}

	@Test
	public void testReadLaLs() {
		List<Line> lines = LaLUtil.readLines(prepareLalsForTest());
		int expectedLines = 3;
		assertEquals(expectedLines, lines.size());
		
		assertEquals("LINE_1", lines.get(0).getName());
		LaL lal = new LaL(121.21234,34.23231);
		assertEquals(lal, lines.get(0).getLals().get(0));
		
		
		assertEquals("LINE_2", lines.get(1).getName());
		lal = new LaL(121.23222,34.23222);
		assertEquals(lal, lines.get(1).getLals().get(0));
		lal = new LaL(121.33,34.88);
		assertEquals(lal, lines.get(1).getLals().get(1));
		
		assertEquals("LINE_26", lines.get(2).getName());
		lal = new LaL(121.11,34.55);
		assertEquals(lal, lines.get(2).getLals().get(0));
		
		File fileToTest = new File(LALS_FILE);
		if (fileToTest.exists()) {
			fileToTest.delete();
		}
		
	}

	private File prepareLalsForTest() {
		File fileToTest = new File(LALS_FILE);
		if (fileToTest.exists()) {
			fileToTest.delete();
		}

		try {
			PrintStream ps = new PrintStream(new FileOutputStream(fileToTest));
			ps.println("LINE_1");
			ps.println("121.21234,34.23231");
			ps.println("LINE_2");
			ps.println("121.23222,34.23222");
			ps.println("121.33,34.88");
			ps.println("LINE_26");
			ps.println("121.11,34.55");
			ps.flush();
		} catch (IOException ex) {
			fail();
		}

		return fileToTest;
	}
}
