package com.almond.way.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.almond.way.server.model.LaL;
import com.almond.way.server.model.Line;

import sun.swing.StringUIClientPropertyKey;

public class LaLUtil {

	private static final String COMMA = ",";
	private static final String LINE_PREFIX = "LINE_";

	public static double getDistance(LaL from, LaL to) {
		double R = 6378137;
		double latFrom = from.getLatitude() * Math.PI / 180.0;
		double latTo = to.getLatitude() * Math.PI / 180.0;
		double a = latFrom - latTo;
		double b = (from.getLongitude() - to.getLongitude()) * Math.PI / 180.0;
		double sa2 = Math.sin(a / 2.0);
		double sb2 = Math.sin(b / 2.0);
		double d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latFrom) * Math.cos(latTo) * sb2 * sb2));
		return d;
	}

	public static List<Line> readLines(File file) {
		List<Line> lines = new ArrayList<>();
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			Line line = null;
			String lineName = "";
			while ((tempString = reader.readLine()) != null) {
				if (tempString.startsWith(LINE_PREFIX)) {
					if (!lineName.equals(tempString.trim())) {
						lineName = tempString.trim();

						if (line != null) {
							lines.add(line);
						}

						line = new Line();
						line.setName(lineName);
					}
				} else {
					String[] lal = tempString.trim().split(COMMA);
					line.appendLal(new LaL(Double.valueOf(lal[0]), Double.valueOf(lal[1])));
				}
			}

			if (line != null) {
				lines.add(line);
			}

			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return lines;

	}
}
