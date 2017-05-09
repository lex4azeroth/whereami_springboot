var GPS = {
    PI : 3.14159265358979324,
    x_pi : 3.14159265358979324 * 3000.0 / 180.0,
    delta : function (lat, lon) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        var a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        var ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        var dLat = this.transformLat(lon - 105.0, lat - 35.0);
        var dLon = this.transformLon(lon - 105.0, lat - 35.0);
        var radLat = lat / 180.0 * this.PI;
        var magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        var sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * this.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * this.PI);
        return {'lat': dLat, 'lon': dLon};
    },
     
    //WGS-84 to GCJ-02
    gcj_encrypt : function (wgsLat, wgsLon) {
        if (this.outOfChina(wgsLat, wgsLon))
            return {'lat': wgsLat, 'lon': wgsLon};
 
        var d = this.delta(wgsLat, wgsLon);
        return {'lat' : wgsLat + d.lat,'lon' : wgsLon + d.lon};
    },
    //GCJ-02 to WGS-84
    gcj_decrypt : function (gcjLat, gcjLon) {
        if (this.outOfChina(gcjLat, gcjLon))
            return {'lat': gcjLat, 'lon': gcjLon};
         
        var d = this.delta(gcjLat, gcjLon);
        return {'lat': gcjLat - d.lat, 'lon': gcjLon - d.lon};
    },
    //GCJ-02 to WGS-84 exactly
    gcj_decrypt_exact : function (gcjLat, gcjLon) {
        var initDelta = 0.01;
        var threshold = 0.000000001;
        var dLat = initDelta, dLon = initDelta;
        var mLat = gcjLat - dLat, mLon = gcjLon - dLon;
        var pLat = gcjLat + dLat, pLon = gcjLon + dLon;
        var wgsLat, wgsLon, i = 0;
        while (1) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            var tmp = this.gcj_encrypt(wgsLat, wgsLon)
            dLat = tmp.lat - gcjLat;
            dLon = tmp.lon - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;
 
            if (dLat > 0) pLat = wgsLat; else mLat = wgsLat;
            if (dLon > 0) pLon = wgsLon; else mLon = wgsLon;
 
            if (++i > 10000) break;
        }
        //console.log(i);
        return {'lat': wgsLat, 'lon': wgsLon};
    },
    //GCJ-02 to BD-09
    bd_encrypt : function (gcjLat, gcjLon) {
        var x = gcjLon, y = gcjLat;  
        var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * this.x_pi);  
        var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * this.x_pi);  
        bdLon = z * Math.cos(theta) + 0.0065;  
        bdLat = z * Math.sin(theta) + 0.006; 
        return {'lat' : bdLat,'lon' : bdLon};
    },
    //BD-09 to GCJ-02
    bd_decrypt : function (bdLat, bdLon) {
        var x = bdLon - 0.0065, y = bdLat - 0.006;  
        var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * this.x_pi);  
        var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * this.x_pi);  
        var gcjLon = z * Math.cos(theta);  
        var gcjLat = z * Math.sin(theta);
        return {'lat' : gcjLat, 'lon' : gcjLon};
    },
    //WGS-84 to Web mercator
    //mercatorLat -> y mercatorLon -> x
    mercator_encrypt : function(wgsLat, wgsLon) {
        var x = wgsLon * 20037508.34 / 180.;
        var y = Math.log(Math.tan((90. + wgsLat) * this.PI / 360.)) / (this.PI / 180.);
        y = y * 20037508.34 / 180.;
        return {'lat' : y, 'lon' : x};
        /*
        if ((Math.abs(wgsLon) > 180 || Math.abs(wgsLat) > 90))
            return null;
        var x = 6378137.0 * wgsLon * 0.017453292519943295;
        var a = wgsLat * 0.017453292519943295;
        var y = 3189068.5 * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a)));
        return {'lat' : y, 'lon' : x};
        //*/
    },
    // Web mercator to WGS-84
    // mercatorLat -> y mercatorLon -> x
    mercator_decrypt : function(mercatorLat, mercatorLon) {
        var x = mercatorLon / 20037508.34 * 180.;
        var y = mercatorLat / 20037508.34 * 180.;
        y = 180 / this.PI * (2 * Math.atan(Math.exp(y * this.PI / 180.)) - this.PI / 2);
        return {'lat' : y, 'lon' : x};
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
    },
    // two point's distance
    distance : function (latA, lonA, latB, lonB) {
        var earthR = 6371000.;
        var x = Math.cos(latA * this.PI / 180.) * Math.cos(latB * this.PI / 180.) * Math.cos((lonA - lonB) * this.PI / 180);
        var y = Math.sin(latA * this.PI / 180.) * Math.sin(latB * this.PI / 180.);
        var s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        var alpha = Math.acos(s);
        var distance = alpha * earthR;
        return distance;
    },
    outOfChina : function (lat, lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    },
    transformLat : function (x, y) {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * this.PI) + 40.0 * Math.sin(y / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * this.PI) + 320 * Math.sin(y * this.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    },
    transformLon : function (x, y) {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * this.PI) + 40.0 * Math.sin(x / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * this.PI) + 300.0 * Math.sin(x / 30.0 * this.PI)) * 2.0 / 3.0;
        return ret;
    }
};

function showMap() {
var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
var point = new BMap.Point(121.323258,31.284051);    // 创建点坐标
map.centerAndZoom(point, 19);                     // 初始化地图,设置中心点坐标和地图级别。

//var startpoint = new BMap.Point(121.329492, 31.284172);
//var endpoint = new BMap.Point(121.321731,31.283431);
//var walking = new BMap.WalkingRoute(map, {renderOptions:{map: map, autoViewport: true}});
//walking.search(startpoint, endpoint);

var polyline = new BMap.Polyline([
//   new BMap.Point(121.329492,31.284172),
//   new BMap.Point(121.326043,31.285406),
//   new BMap.Point(121.320725,31.286394),
//   new BMap.Point(121.315478,31.288245),
//   new BMap.Point(121.30592,31.288863),
//   new BMap.Point(121.308867,31.28627),
//   new BMap.Point(121.315478,31.284851),
//   new BMap.Point(121.321443,31.282691),
//   new BMap.Point(121.331504,31.280839)

  new BMap.Point(121.323258,31.284051),
  new BMap.Point(121.323456,31.284008),
  new BMap.Point(121.323707,31.283958),
  new BMap.Point(121.323856,31.283939),
  new BMap.Point(121.324035,31.2839),
  new BMap.Point(121.324309,31.28385)
], {strokeColor:"red", strokeWeight:1, strokeOpacity:0.5});
map.addOverlay(polyline);

// map.addControl(new BMap.MapTypeControl());
map.addControl(new BMap.NavigationControl());
// map.addControl(new BMap.ScaleControl());
// map.addControl(new BMap.OverviewMapControl());    
// map.enableScrollWheelZoom();                  // 启用滚轮放大缩小。
//map.enableKeyboard();                         // 启用键盘操作。  
// map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
var marker = new BMap.Marker(point);
map.addOverlay(marker);               // 将标注添加到地图中
marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
map.addOverlay(marker); 
}

function showMapWithLol(lols) {
var clon = parseFloat(lols[0].longitude);
var clat = parseFloat(lols[0].latitude);

//WGS-84 to GCJ-02
var att1 = GPS.gcj_encrypt(clat, clon);

//GCJ-02 to BD-09
var cbd = GPS.bd_encrypt(att1['lat'], att1['lon']);

var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
var point = new BMap.Point(cbd['lon'], cbd['lat']);    // 创建点坐标
// var point = new BMap.Point(clon, clat);
// var point = new BMap.Point(att1['lon'], att1['lat']);
map.centerAndZoom(point, 18);                     // 初始化地图,设置中心点坐标和地图级别。

//var startpoint = new BMap.Point(121.329492, 31.284172);
//var endpoint = new BMap.Point(121.321731,31.283431);
//var walking = new BMap.WalkingRoute(map, {renderOptions:{map: map, autoViewport: true}});
//walking.search(startpoint, endpoint);

// map.addControl(new BMap.MapTypeControl());
map.addControl(new BMap.NavigationControl());
map.enableScrollWheelZoom();                  // 启用滚轮放大缩小。
// map.enableKeyboard();                         // 启用键盘操作。  
// map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
var marker = new BMap.Marker(point);
// map.addOverlay(marker);               // 将标注添加到地图中
marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
map.addOverlay(marker); 

var polyLine = new Array();

for (var i = 0; i < lols.length - 1; i++) {
    var lon = parseFloat(lols[i].longitude);
    var lat = parseFloat(lols[i].latitude);

    //WGS-84 to GCJ-02
    var att1 = GPS.gcj_encrypt(lat, lon);

    //GCJ-02 to BD-09
    var bd = GPS.bd_encrypt(att1['lat'], att1['lon']);
    // var fixedLon = bd['lon'].toFixed(3);
    // var fixedLat = bd['lat'].toFixed(5);
    // polyLine.push(new BMap.Point(fixedLon, fixedLat));
    polyLine.push(new BMap.Point(bd['lon'], bd['lat']));

    // polyLine.push(new BMap.Point(lols[i].longitude, lols[i].latitude));

}

var polyline = new BMap.Polyline(polyLine, {strokeColor:"red", strokeWeight:1, strokeOpacity:0.5});
map.addOverlay(polyline);

//     var lon = parseFloat(lols[0].longitude);
//     var lat = parseFloat(lols[0].latitude);

//     //WGS-84 to GCJ-02
//     var att1 = GPS.gcj_encrypt(lat, lon);

//     //GCJ-02 to BD-09
//     var bd = GPS.bd_encrypt(att1['lat'], att1['lon']);
//     var pointA = new BMap.Point(bd['lon'], bd['lat']);

//     lon = parseFloat(lols[lols.length - 1].longitude);
//     lat = parseFloat(lols[lols.length - 1].latitude);

//     //WGS-84 to GCJ-02
//     att1 = GPS.gcj_encrypt(lat, lon);

//     //GCJ-02 to BD-09
//     bd = GPS.bd_encrypt(att1['lat'], att1['lon']);
//     var pointB = new BMap.Point(bd['lon'], bd['lat']);

//     // var walking = new BMap.WalkingRoute(map, {renderOptions:{map: map, autoViewport: true}});
//     // walking.search(startpoint, endpoint);
// var polyline = new BMap.Polyline([pointA,pointB], {strokeColor:"blue", strokeWeight:1, strokeOpacity:0.5});
// map.addOverlay(polyline);
}

function postData() {
    // alert("${home}");
    
    var jsondata = 
        [
            {"androidID":"androidID4test","date":"2016-12-29 09:12:51","latitude":"31.216138333333333","longitude":"121.49172166666668"},
            {"androidID":"androidID4test_1","date":"2016-12-29 09:12:48","latitude":"31.216138333333333","longitude":"121.49172166666668"},
            {"androidID":"androidID4test","date":"2016-12-29 09:12:46","latitude":"31.216138333333333","longitude":"121.49172166666668"},
            {"androidID":"androidID4test_2","date":"2016-12-29 09:12:44","latitude":"31.216138333333333","longitude":"121.49172166666668"}]
    ;

    var jsondata2 = {

        "androidID":"7bbd793805f2ba1d",
        "date":"2016-12-29 09:12:51",
        "latitude":"31.216138333333333",
        "longitude":"121.49172166666668"
        
    }

    alert(jsondata2)
    $.ajax({
		type: "POST",
		url: "mylocation",
        data:JSON.stringify(jsondata),
		async: true,
		contentType: "application/json",
        cache:false,
		success: function(data) {
			alert(data);
		}
	});
}

function selectData() {
var lols = new Array();
    $.ajax({
		type: "GET",
		url: "ihavebeen",
        // data:JSON.stringify(jsondata),
		async: true,
		contentType: "application/json",
        cache:false,
		success: function(data) {
            lols = data;
            showMapWithLol(data);
			// alert(data);
		}
	});
}

function selectDatad7() {
var lols = new Array();
    $.ajax({
		type: "GET",
		url: "ihavebeen",
        // data:JSON.stringify(jsondata),
		async: true,
		contentType: "application/json",
        cache:false,
		success: function(data) {
            lols = data;
            showMapWithLol(data);
			// alert(data);
		}
	});
}

function selectDatad8() {
var lols = new Array();
    $.ajax({
		type: "GET",
		url: "ihavebeen2",
        // data:JSON.stringify(jsondata),
		async: true,
		contentType: "application/json",
        cache:false,
		success: function(data) {
            lols = data;
            showMapWithLol(data);
			// alert(data);
		}
	});
}

function decode() {
    var lons = new Array(); 

    lons = [
121.323474,
121.323613,
121.323757,
121.323927,
121.324121,
121.324345,
121.324516,
121.324686,
121.324853,
121.324987,
121.325167,
121.325342,
121.325508,
121.325679,
121.325814,
121.325949,
121.326092,
121.326371,
121.326537,
121.326721,
121.32691,
121.327049,
121.32722,
121.32739,
121.327566,
121.327754,
121.327934,
121.328091,
121.328266,
121.328396,
121.3285,
121.328635,
121.328819,
121.328989,
121.329156,
121.329313,
121.329483,
121.329659,
121.329816,
121.329982,
121.330144

    ]

    var lats = new Array();
    lats = [
31.284043
,31.284016
,31.283997
,31.283958
,31.28392
,31.283889
,31.283854
,31.283827
,31.283788
,31.283769
,31.283738
,31.283704
,31.283673
,31.283638
,31.283611
,31.283588
,31.283565
,31.283507
,31.28348
,31.283441
,31.283414
,31.283383
,31.283349
,31.283318
,31.283287
,31.283248
,31.28321
,31.283183
,31.283156
,31.283129
,31.28311
,31.283083
,31.283044
,31.283013
,31.282986
,31.282951
,31.28292
,31.282893
,31.282859
,31.282824
,31.282801

    ]
    ;
// var att1 = GPS.gcj_encrypt(clat, clon);

// //GCJ-02 to BD-09
// var cbd = GPS.bd_encrypt(att1['lat'], att1['lon']);
var output = "";
var li;
for (var index = 0; index < lons.length; index++) {
// DB-09 -> GCJ-02
var dcdb = GPS.bd_decrypt(lats[index], lons[index]);
// GCJ-02 to WGS-84
var dcdb2 = GPS.gcj_decrypt(dcdb['lat'], dcdb['lon']);
// alert(dcdb);
output += dcdb2['lon'] + "," + dcdb2['lat'] + "\n";
 
li="<li><a>"+ dcdb2['lon'] + "," + dcdb2['lat'] + "\n" + "</a></li>";
$("#listView").append(li)
}

//alert(output);
//  var li;

// for (var index=0; index < output.length; index++) {
//  li="<li><a>"+ output.get(index)+ "</a></li>";
// $("#listView").append(li)

// }

var output2 = "";
for (var index = 0; index < lons.length; index++) {
// DB-09 -> GCJ-02
var dcdb = GPS.bd_decrypt(lats[index], lons[index]);
// GCJ-02 to WGS-84
var dcdb2 = GPS.gcj_decrypt_exact(dcdb['lat'], dcdb['lon']);
// alert(dcdb);
output2 += dcdb2['lon'] + "," + dcdb2['lat'] + "\n";
}

//alert(output2);

}