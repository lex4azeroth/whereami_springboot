package com.almond.way.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.ResourceUtils;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.model.Equipment;
import com.almond.way.server.model.LaL;
import com.almond.way.server.model.Line;

public class LaLUtil {

	private static final String COMMA = ",";
	private static final String GD_PATTERN = "^(XD|SD|XC|XB|SB|SC|XF|SF)\\d{2}$";
	private static final String GD_NOT_FOUND = "GD {%s} not found";
	
	protected static final String LAL_PROPERTIES_NOT_FOUND = "lals.properties not found";
	protected static final String NO_MORE_LINES = "No more lines for line number [%d]";
	
	private static final ThreadLocal<Map<String, Line>> gdLocal = new ThreadLocal<Map<String, Line>>() {
		@Override
		protected Map<String, Line> initialValue() {
			return new HashMap<>();
		}
	};

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
	
	public static Map<String, Line> readLines(File file) {
		if (gdLocal.get().size() != 0) {
			return gdLocal.get();
		}
		
		ConcurrentHashMap<String, Line> linesMap = new ConcurrentHashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String tempString = null;
			Line line = null;
			String lineName = "";
			while ((tempString = reader.readLine()) != null) {
				tempString = tempString.trim();
				if (tempString.matches(GD_PATTERN)) {
					if (!lineName.equals(tempString)) {
						lineName = tempString;
						
						if (line != null) {
							linesMap.putIfAbsent(line.getName(), line);
						}
						
						line = new Line();
						line.setName(lineName);
					}
				} else {
					String[] lal = tempString.split(COMMA);
					line.appendLal(new LaL(Double.valueOf(lal[0]), Double.valueOf(lal[1])));
				}
			}
			
			if (line != null) {
				linesMap.putIfAbsent(line.getName(), line);
			}
		}
		catch (IOException e) {
			throw new WhereAmIException(e);
		}
		
		gdLocal.set(linesMap);
		
		return gdLocal.get();
		
	}
	
	public static List<LaL> getLalPoints(String gd) {
		Map<String, Line> lines = null;
		try {
			lines = readLines(ResourceUtils.getFile("classpath:static/lals.properties"));
		} catch (FileNotFoundException e) {
			throw new WhereAmIException(LAL_PROPERTIES_NOT_FOUND, e);
		}
		
		List<LaL> lals = new ArrayList<>();
		
		if (lines.get(gd) == null) {
			throw new WhereAmIException(String.format(GD_NOT_FOUND, gd));
		}
		
		lals.addAll(lines.get(gd).getLals());
		
		return lals;
	}

	@Deprecated
	public static List<LaL> getLalPoints(int lineNum) {
		Map<String, Line> lines = null;
		// List<Line> lines = null;
		try {
			lines = readLines(ResourceUtils.getFile("classpath:static/lals.properties"));
		} catch (FileNotFoundException e) {
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
