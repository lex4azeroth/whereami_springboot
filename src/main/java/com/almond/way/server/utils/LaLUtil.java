package com.almond.way.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.model.LaL;
import com.almond.way.server.model.Line;

public class LaLUtil {

	private static final String COMMA = ",";
	private static final String LINE_PREFIX = "LINE_";
	
	protected static final String LAL_PROPERTIES_NOT_FOUND = "lals.properties not found";
	protected static final String NO_MORE_LINES = "No more lines for line number [%d]";

	public static DeviceLoL makeMockLoL(int id, double latitude, double longitude) {
		DeviceLoL mockLoL = new DeviceLoL();
		mockLoL.setId(id);
		mockLoL.setLongitude(String.valueOf(longitude));
		mockLoL.setLatitude(String.valueOf(latitude));
		return mockLoL;
	}
	
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

	public static List<LaL> getLalPoints(int lineNum) {
		List<Line> lines = null;
		try {
			lines = readLines(ResourceUtils.getFile("classpath:static/lals.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new WhereAmIException(LAL_PROPERTIES_NOT_FOUND, e);
		}
		
		if (lineNum > lines.size()) {
			throw new WhereAmIException(String.format(NO_MORE_LINES, lineNum));
		}
		
		List<LaL> lals = new ArrayList<>();
		if (lineNum == lines.size()) {
			lals.addAll(lines.get(lines.size() - 1).getLals());
			return lals;
		} else if (lineNum == 1) {
			lals.addAll(lines.get(0).getLals());
			return lals;
		} else {
			Line north = lines.get(lineNum - 1);
			Line south = lines.get(lineNum - 2);
			
			lals.addAll(north.getLals());
			lals.addAll(south.getLals());
			
			return lals;
		}
	}
}
