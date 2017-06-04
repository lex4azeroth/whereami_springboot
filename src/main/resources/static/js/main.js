function showMap() {
var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
var point = new BMap.Point(121.323258,31.284051);    // 创建点坐标
map.centerAndZoom(point, 19);                     // 初始化地图,设置中心点坐标和地图级别。
map.addControl(new BMap.NavigationControl());
}

function showRouteWithLol(lols) {
	var clon = parseFloat(lols[0].longitude);
	var clat = parseFloat(lols[0].latitude);
//
//	//WGS-84 to GCJ-02
//	var att1 = GPS.gcj_encrypt(clat, clon);
//
//	//GCJ-02 to BD-09
//	var cbd = GPS.bd_encrypt(att1['lat'], att1['lon']);

	var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
	var point = new BMap.Point(clon, clat);    // 创建点坐标
	map.centerAndZoom(point, 18);                     // 初始化地图,设置中心点坐标和地图级别。
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

//	    //WGS-84 to GCJ-02
//	    var att1 = GPS.gcj_encrypt(lat, lon);
//
//	    //GCJ-02 to BD-09
//	    var bd = GPS.bd_encrypt(att1['lat'], att1['lon']);

	    polyLine.push(new BMap.Point(lon, lat));
	}

	var polyline = new BMap.Polyline(polyLine, {strokeColor:"red", strokeWeight:1, strokeOpacity:0.5});
	map.addOverlay(polyline);
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
map.centerAndZoom(point, 18);                     // 初始化地图,设置中心点坐标和地图级别。
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

    polyLine.push(new BMap.Point(bd['lon'], bd['lat']));
}

var polyline = new BMap.Polyline(polyLine, {strokeColor:"red", strokeWeight:1, strokeOpacity:0.5});
map.addOverlay(polyline);
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

function whereami() {
	cleanValidators();
	var fromDateTime = getFromDateTime();
	
	if (fromDateTime == undefined) {
		return;
	}
	var endDateTime = getEndDateTime();
	
	if (endDateTime == undefined) {
		return;
	}
	
	var from = moment(fromDateTime);
	var end = moment(endDateTime);
	
	if (from >= end) {
		$('#fromValidator').text("起始日期时间必须大于结束日期时间");
		$('#endValidator').text("起始日期时间必须大于结束日期时间");
		return;
	}
	
	var equipmentId = "7bbd793805f2ba1d";
	var lineNum = getLineNum();
	if (lineNum == undefined) {
		return;
	}

	var restURL = "ihavebeen/" + equipmentId + "/" + fromDateTime + "/" + endDateTime + "/" + lineNum;
	var lols = new Array();
	    $.ajax({
			type: "GET",
//			url: "ihavebeen/7bbd793805f2ba1d/2017-04-08 04:51/2017-04-08 04:58/5",
	        url: restURL,
			async: true,
			contentType: "application/json",
	        cache:false,
			success: function(data) {
	            lols = data;
	            showRouteWithLol(data);
			}
		});
	}

function whereamioriginal() {
	cleanValidators();
	var fromDateTime = getFromDateTime();
	
	if (fromDateTime == undefined) {
		return;
	}
	var endDateTime = getEndDateTime();
	
	if (endDateTime == undefined) {
		return;
	}
	
	var from = moment(fromDateTime);
	var end = moment(endDateTime);
	
	if (from >= end) {
		$('#fromValidator').text("起始日期时间必须大于结束日期时间");
		$('#endValidator').text("起始日期时间必须大于结束日期时间");
		return;
	}
	
	var equipmentId = "7bbd793805f2ba1d";
	var lineNum = getLineNum();

	if (lineNum == undefined) {
		return;
	}

	var restURL = "ihavebeenoriginal/" + equipmentId + "/" + fromDateTime + "/" + endDateTime + "/" + lineNum;
	var lols = new Array();
	    $.ajax({
			type: "GET",
			// url: "ihavebeenoriginal/7bbd793805f2ba1d/2017-04-08 04:51/2017-04-08 04:58/5",
	        url: restURL,
			async: true,
			contentType: "application/json",
	        cache:false,
			success: function(data) {
	            lols = data;
	            showRouteWithLol(data);
			}
		});
	}

function getFromDateTime() {
		var fromDate = $("#fromDate")[0].value;
		if (fromDate == "") {
			$('#fromValidator').text("日期不合法");
			return;
		}
		
		var fromTime = $("#fromTime")[0].value;
		if (fromTime == "") {
			$('#fromValidator').text("时间不合法");
			return;
		}
		
		return fromDate + " " + fromTime;
	}

function getEndDateTime() {
		var endDate = $("#endDate")[0].value;
		if (endDate == "") {
			$('#endValidator').text("日期不合法");
			return;
		}
		
		var endTime = $("#endTime")[0].value;
		if (endTime == "") {
			$('#endValidator').text("时间不合法");
			return;
		}
		return endDate + " " + endTime;
	}

function getLineNum() {
	var lineNum = $('#lineNum')[0].value;

	if (lineNum <= 0 || lineNum >= 26) {
		$('#lineNumValidator').text('轨道号有效范围1-25');
		return;
	}

	return lineNum;
}

function showDeviceList() {
	var devices = new Array();
    $.ajax({
		type: "GET",
        url: "equipments",
		async: true,
		contentType: "application/json",
        cache:false,
		success: function(data) {
			devices = data;
		    for (var index = 0; index < devices.length; index++) {
		    	var li = document.createElement('li');
		    	var id = devices[index].equipmentId;
		    	li.setAttribute('class', 'device');
		    	li.setAttribute('id', id);
		    	li.innerHTML = id;
		    	li.onclick = deviceChoosen;
		        $('#Content-Left').append(li);
		    }
		}
	});
}

function deviceChoosen() {
	currentDeviceId = this.id;
	$('#currentDevice').text("当前设备：" + currentDeviceId);
	showMap();
}

function cleanValidators() {
	$('#fromValidator').text("");
	$('#endValidator').text("");
	$('#lineNumValidator').text('');
}
	
$(document).ready(function() {
		showDeviceList();
		
		$('#fromDate').val(moment().format('YYYY-MM-DD'));
		$('#endDate').val(moment().format('YYYY-MM-DD'));
		$('#fromTime').val(moment().format('hh:mm:ss'));
		$('#endTime').val(moment().format('hh:mm:ss'));
		$('#lineNum').val(1);
	});

var currentDeviceId;