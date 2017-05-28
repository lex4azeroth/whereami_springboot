package com.almond.way.server.utils;

import com.almond.way.server.model.LaL;

public class GPSUtil {
	private static final double PI = 3.14159265358979324;
	private static final double X_PI = PI * 3000.0f / 180.0f;
	
	public static LaL BD2WGS(double lat, double lon) {
		LaL tmp = bd_decrypt(lat, lon);
		return gcj_decrypt(tmp.getLatitude(), tmp.getLongitude());
	}
	
	public static LaL WGS2BD(double lat, double lon) {
		LaL tmp = gcj_encrypt(lat, lon);
		return bd_encrypt(tmp.getLatitude(), tmp.getLongitude());
	}
	
	private static LaL getDelta(double lat, double lon) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        double a = 6378245.0f; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);

        return new LaL(dLon, dLat);
	}
	
	//WGS-84 to GCJ-02
    public static LaL gcj_encrypt(double wgsLat, double wgsLon) {
        if (outOfChina(wgsLat, wgsLon))
        	return new LaL(wgsLon, wgsLat);
//            return {'lat': wgsLat, 'lon': wgsLon};
 
        LaL d = getDelta(wgsLat, wgsLon);
        return new LaL(wgsLon + d.getLongitude(), wgsLat + d.getLatitude());
//        return {'lat' : wgsLat + d.lat,'lon' : wgsLon + d.lon};
    }
    
    //GCJ-02 to WGS-84
    public static LaL gcj_decrypt(double gcjLat, double gcjLon) {
        if (outOfChina(gcjLat, gcjLon))
        	return new LaL(gcjLon, gcjLat);
//            return {'lat': gcjLat, 'lon': gcjLon};
         
        LaL d = getDelta(gcjLat, gcjLon);
        return new LaL(gcjLon - d.getLongitude(), gcjLat - d.getLatitude());
//        return {'lat': gcjLat - d.lat, 'lon': gcjLon - d.lon};
    }
    
    //GCJ-02 to WGS-84 exactly
    public static LaL gcj_decrypt_exact(double gcjLat, double gcjLon) {
    	double initDelta = 0.01;
    	double threshold = 0.000000001;
    	double dLat = initDelta, dLon = initDelta;
    	double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
    	double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
    	double wgsLat, wgsLon, i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            LaL tmp = gcj_encrypt(wgsLat, wgsLon);
            dLat = tmp.getLatitude() - gcjLat;
            dLon = tmp.getLongitude() - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold)) {
            	break;
            }
 
            if (dLat > 0) {
            	pLat = wgsLat;
            } else {
            	mLat = wgsLat;
            }
            
            if (dLon > 0) {
            	pLon = wgsLon; 
            } else {
            	mLon = wgsLon;
            }
 
            if (++i > 10000) {
            	break;
            }
        }

        return new LaL(wgsLon, wgsLat);
//        return {'lat': wgsLat, 'lon': wgsLon};
    }
    
    //GCJ-02 to BD-09
    public static LaL bd_encrypt(double gcjLat, double gcjLon) {
    	double x = gcjLon, y = gcjLat;  
    	double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);  
    	double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);  
        double bdLon = z * Math.cos(theta) + 0.0065;  
        double bdLat = z * Math.sin(theta) + 0.006;
        return new LaL(bdLon, bdLat);
//        return {'lat' : bdLat,'lon' : bdLon};
    }
    
    //BD-09 to GCJ-02
    public static LaL bd_decrypt(double bdLat, double bdLon) {
    	double x = bdLon - 0.0065, y = bdLat - 0.006;  
    	double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);  
    	double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);  
    	double gcjLon = z * Math.cos(theta);  
    	double gcjLat = z * Math.sin(theta);
    	return new LaL(gcjLon, gcjLat);
//        return {'lat' : gcjLat, 'lon' : gcjLon};
    }
    
    //WGS-84 to Web mercator
    //mercatorLat -> y mercatorLon -> x
    public static LaL mercator_encrypt(double wgsLat, double wgsLon) {
    	double x = wgsLon * 20037508.34 / 180.0;
    	double y = Math.log(Math.tan((90.0 + wgsLat) * PI / 360.0)) / (PI / 180.0);
        y = y * 20037508.34 / 180.0;
        return new LaL(x, y);
        
        /*
        if ((Math.abs(wgsLon) > 180 || Math.abs(wgsLat) > 90))
            return null;
        var x = 6378137.0 * wgsLon * 0.017453292519943295;
        var a = wgsLat * 0.017453292519943295;
        var y = 3189068.5 * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a)));
        return {'lat' : y, 'lon' : x};
        //*/
    }
    
    // Web mercator to WGS-84
    // mercatorLat -> y mercatorLon -> x
    public static LaL mercator_decrypt(double mercatorLat, double mercatorLon) {
    	double x = mercatorLon / 20037508.34 * 180.0;
    	double y = mercatorLat / 20037508.34 * 180.0;
        y = 180 / PI * (2 * Math.atan(Math.exp(y * PI / 180.0)) - PI / 2);
        return new LaL(x, y);

        /*
        if (Math.abs(mercatorLon) < 180 && Math.abs(mercatorLat) < 90)
            return null;
        if ((Math.abs(mercatorLon) > 20037508.3427892) || (Math.abs(mercatorLat) > 20037508.3427892))
            return null;
        var a = mercatorLon / 6378137.0 * 57.295779513082323;
        var x = a - (Math.floor(((a + 180.0) / 360.0)) * 360.0);
        var y = (1.5707963267948966 - (2.0 * Math.atan(Math.exp((-1.0 * mercatorLat) / 6378137.0)))) * 57.295779513082323;
        return {'lat' : y, 'lon' : x};
        //*/
    }
	
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }
    
    private static double transformLon(double x, double y) {
        double  ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
    
    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    
    private static double distance(double latA, double lonA, double latB, double lonB) {
        double earthR = 6371000.0f;
        double x = Math.cos(latA * PI / 180.) * Math.cos(latB * PI / 180.) * Math.cos((lonA - lonB) * PI / 180);
        double y = Math.sin(latA * PI / 180.) * Math.sin(latB * PI / 180.);
        double s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }
}
