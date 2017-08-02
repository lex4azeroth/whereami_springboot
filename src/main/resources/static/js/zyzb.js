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


function ajaxT() {
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
	
	var restURL = "showmethelist/" + fromDateTime + "/" + endDateTime;
	
	$.ajax({
		type: "GET",
		// url: "ihavebeenoriginal/7bbd793805f2ba1d/2017-04-08 04:51/2017-04-08 04:58/5",
        url: restURL,
		async: true,
		contentType: "application/json",
        cache:false,
		success : function(data) {
			createShowingTable(data);
		}
	});
}

function createShowingTable(data) {
	var tableStr = "<table width='99%'>";
	tableStr = tableStr + "<tr >" 
			+ "<td width='10%'>手持设备号</td>"
			+ "<td width='10%'>出发车次</td>"
			+ "<td witdh='10%'>到达车次</td>"
			//            +"<td width='5%'><input id='checkAll' name='checkAll' type='checkbox' /></td>"  
			+ "<td width='20%'>作业开始时间</td>" 
			+ "<td width='20%'>作业结束时间</td>"
			+ "<td width='20%'>作业人</td>" 
			+ "<td width='10%'>股道</td>" + "</tr>";
	var len = data.length;
	for (var i = 0; i < len; i++) {
		tableStr = tableStr 
		+ "<tr onclick='showMapLayer(this)'>" 
			+ "<td>" + data[i].guid + "</td>"
			//                +"<td><input class='check' id='checkOne' name='checkOne' type='checkbox' value='"+data[i].key+"' /></td>"  
			+ "<td>" + data[i].cfcc + "</td>" 
			+ "<td>" + data[i].ddcc + "</td>"
			+ "<td>" + data[i].zykssj + "</td>" 
			+ "<td>" + data[i].zyjssj + "</td>" 
			+ "<td>" + data[i].zbya + "</td>" 
			+ "<td>" + data[i].gd + "</td>" 
		+ "</tr>";
	}
	if (len == 0) {
		tableStr = tableStr 
		+ "<tr style='text-align: center'>"
		+ "<td colspan='6'><font color='#cd0a0a'>暂无记录</font></td>" 
		+ "</tr>";
	}
	tableStr = tableStr + "</table>";
	$("#tableAjax").html(tableStr);
}

function cleanValidators() {
	$('#fromValidator').text("");
	$('#endValidator').text("");
}

function showMapLayer(obj) {
	var guid = obj.childNodes[0].innerText;
	var zykssj = obj.childNodes[3].innerText;
	var zyjssj = obj.childNodes[4].innerText;
	var gd = obj.childNodes[6].innerText;
	$("#myMap").addClass("float_map");
	var container = "<div id='container' class='float_map'></div><div><a href='javascript:;' class='float_close' data-role='button' onclick='closeMapLayer()'>关闭</a></div>";
	$("#mapContainer").html(container);
	$("#myMap").css("display", "block");
	var whichRoute = $('#whichRoute input:radio:checked').val();
	var restURL = whichRoute + "/" + guid + "/" + zykssj + "/" + zyjssj + "/" + gd;
	whereami(restURL);
}

function closeMapLayer() {
	$("#myMap").css("display", "none");
}

function showRealMap() {
	var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
	var point = new BMap.Point(121.323258,31.284051);    // 创建点坐标
	map.centerAndZoom(point, 19);                     // 初始化地图,设置中心点坐标和地图级别。
	map.addControl(new BMap.NavigationControl());
	}

function whereami(restURL) {
	var lols = new Array();
	    $.ajax({
			type: "GET",
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

function showNoResultsError() {
	$("#container").html("<div class='float_error'>暂无记录</div>");
}
	
function showRouteWithLol(lols) {
	if (lols.length == 0) {
		showNoResultsError();
		return;
	}
	var clon = parseFloat(lols[0].longitude);
	var clat = parseFloat(lols[0].latitude);

	var map = new BMap.Map("container",{mapType: BMAP_SATELLITE_MAP});      //设置卫星图为底图
	var point = new BMap.Point(clon, clat);    // 创建点坐标
	map.centerAndZoom(point, 19);                     // 初始化地图,设置中心点坐标和地图级别。
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
	    polyLine.push(new BMap.Point(lon, lat));
	}

	var polyline = new BMap.Polyline(polyLine, {strokeColor:"yellow", strokeWeight:1, strokeOpacity:0.9});
	map.addOverlay(polyline);
	}
$(document).ready(function() {
		$('#fromDate').val(moment().format('YYYY-MM-DD'));
		$('#endDate').val(moment().format('YYYY-MM-DD'));
		$('#fromTime').val(moment().format('hh:mm:ss'));
		$('#endTime').val(moment().format('hh:mm:ss'));
	});
