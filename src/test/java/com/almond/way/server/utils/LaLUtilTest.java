package com.almond.way.server.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.junit.Ignore;
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
	@Ignore
	public void testReadLaLs() {
		Map<String, Line> lines = LaLUtil.readLines(prepareLalsForTest());
		int expectedLines = 3;
		assertEquals(expectedLines, lines.size());
		
		LaL lal = new LaL(121.21234,34.23231);
		assertEquals(lal, lines.get("XF11").getLals().get(0));
		
		
		lal = new LaL(121.23222,34.23222);
		assertEquals(lal, lines.get("SD01").getLals().get(0));
		lal = new LaL(121.33,34.88);
		assertEquals(lal, lines.get("SD01").getLals().get(1));
		
		lal = new LaL(121.11,34.55);
		assertEquals(lal, lines.get("SB02").getLals().get(0));
		
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

		try (PrintStream ps = new PrintStream(new FileOutputStream(fileToTest))){
			ps.println("XF11");
			ps.println("121.21234,34.23231");
			ps.println("SD01");
			ps.println("121.23222,34.23222");
			ps.println("121.33,34.88");
			ps.println("SB02");
			ps.println("121.11,34.55");
			ps.flush();
		} catch (IOException ex) {
			fail();
		}

		return fileToTest;
	}
}
