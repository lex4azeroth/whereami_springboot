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
	var tableStr = "<div class='dataList'><table width='99%'>";
	tableStr = tableStr + "<tr >" 
			+ "<td width='10%'>列车标志</td>"
			+ "<td width='10%'>左作业员</td>"
			+ "<td width='10%'>右作业员</td>"
			+ "<td width='10%'>出发车次</td>"
			+ "<td witdh='10%'>到达车次</td>"
			//            +"<td width='5%'><input id='checkAll' name='checkAll' type='checkbox' /></td>"  
			+ "<td width='20%'>作业开始时间</td>" 
			+ "<td width='20%'>作业结束时间</td>"
			// + "<td width='20%'>作业人</td>" 
			+ "<td width='10%'>股道</td>" + "</tr>";
	var len = data.length;
	for (var i = 0; i < len; i++) {
		tableStr = tableStr 
		+ "<tr>" 
			// + "<td style='display:none;'>" + data[i].guid + "</td>"
			//                +"<td><input class='check' id='checkOne' name='checkOne' type='checkbox' value='"+data[i].key+"' /></td>"  
			+ "<td>" + data[i].lcbz + "</td>" 
			+ "<td class='clickable' onclick='showMapLayer(this)'>" + data[i].zfj + "</td>" 
			+ "<td style='display:none;'>" + data[i].zequid + "</td>"
			+ "<td class='clickable' onclick='showMapLayer(this)'>" + data[i].yfj + "</td>" 
			+ "<td style='display:none;'>" + data[i].yequid + "</td>"
			+ "<td>" + data[i].cfcc + "</td>" 
			+ "<td>" + data[i].ddcc + "</td>"
			+ "<td>" + data[i].zykssj + "</td>" 
			+ "<td>" + data[i].zyjssj + "</td>" 
			// + "<td>" + data[i].zbya + "</td>" 
			+ "<td>" + data[i].gd + "</td>" 
		+ "</tr>";
	}
	if (len == 0) {
		tableStr = tableStr 
		+ "<tr style='text-align: center'>"
		+ "<td colspan='6'><font color='#cd0a0a'>暂无记录</font></td>" 
		+ "</tr>";
	}
	tableStr = tableStr + "</table></div>";
	$("#tableAjax").html(tableStr);
}

function cleanValidators() {
	$('#fromValidator').text("");
	$('#endValidator').text("");
}

function showMapLayer(obj) {
	var guid = obj.parentElement.childNodes[obj.cellIndex + 1].innerText;
	var zykssj = obj.parentElement.childNodes[7].innerText;
	var zyjssj = obj.parentElement.childNodes[8].innerText;
	var gd = obj.parentElement.childNodes[9].innerText;
	$("#myMap").addClass("modal");
	var container = "<span class='close' onclick='closeMapLayer()'>x</span><div id='container' class='float_map'></div>";
	$("#mapContainer").html(container);.23
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

function mappdevice() {
	$('#myModal').css("display", "block");
	$('#content').html(mappingTable);
	$('#modelbody').css("height", "70%");
	showDeviceList2();
}

function deviceMappingChoosen() {
	var equId = this.id;
	$.ajax({
		type: "GET",
        url: "equipment/" + equId,
		async: true,
		contentType: "*/*",
        cache:false,
		success: function(data) {
			var equName = data;
			showDeviceInfo(equId, equName);
		}
	});
}

function showDeviceInfo(equId, equName) {
	$('#equDetailId').text(equId);
	$('#equDetailName')[0].value = equName;
}

function doLogin() {
	// currentRoleIsAdmin = ($("#username")[0].value == $("#password")[0].value);
	// theOriginal
	$.ajax({
		type: "GET",
		url: "adminLogin/" + $("#username")[0].value + "/" + $("#password")[0].value, 
		async: false,
		cache: false,
		success: function(data) {
			currentRoleIsAdmin = data;
		}
	});

	if (currentRoleIsAdmin) {
		$("#originalBtn").css("visibility", "visible");
		$('#deviceMapping').css("visibility", "visible");
		$("#adminLoginButton").text("管理员登出");
		$("#myModal").css("display", "none");
		$('#whichRoute').append(theOriginal);
	} else {
		$('#errormessage')[0].innerText = "登录失败，账号或密码错误";
	}
}

function adminLogin() {

	if (!currentRoleIsAdmin) {
		$('#myModal').css("display", "block");
		$('#content').html(loginContent);
		// $('#content').css("height", "70%");
		// $('#content').css("float", "right");
		// $('#content').css("padding", "right");
		$('#modelbody').css("height", "40%");
		$("#username")[0].value="";
		$("#password")[0].value=""
	} else {
		$("#originalBtn").css("visibility", "hidden");
		$('#deviceMapping').css("visibility", "hidden");
		$("#adminLoginButton").text("管理员登入");
		$('#myModal').css("display", "none");
		// $('input[name=route]:eq[0]').attr("checked", "1");
		$('#theoriginalrout').remove();
		$('#theoptimizedroute').remove();
		$('#whichRoute').append(theOptimized);
		
		currentRoleIsAdmin = false;
	}
	// $('#myModal').css("display", "block"); 
}

function closeAdminLogin() {
	$('#myModal').css("display", "none"); 
}

function closeDeviceMapping() {
	$('#myModal2').css("display", "none");
}

function showDeviceList2() {
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
				var equId = devices[index].equipmentId;
				var id = devices[index].id;
				var equName = devices[index].equipmentName;
		    	li.setAttribute('class', 'device');
				li.setAttribute('id', equId);
				// li.setAttribute('equId', equId);
				// li.setAttribute('equName', equName);
		    	li.innerHTML = equId;
				li.onclick = deviceMappingChoosen;
		        $('#Content-Left-Mini').append(li);
		    }
		}
	});
}

function updateDeviceName() {
	var equId = $('#equDetailId')[0].innerText;
	var equName = $('#equDetailName')[0].value;
	// alert(equId + " + " + equName);

	$.ajax({
		type: "POST",
		url: "updatedevicename/" + equId + "/" + equName,
		async: true, 
		cache: false,
		sunccess: function(data) {
			alert("updated");
		}
	});
}

$(document).ready(function() {
		// showDeviceList();
		
		$('#fromDate').val(moment().format('YYYY-MM-DD'));
		$('#endDate').val(moment().format('YYYY-MM-DD'));
		$('#fromTime').val(moment().format('hh:mm:ss'));
		$('#endTime').val(moment().format('hh:mm:ss'));
		$('#lineNum').val(1);
		$('#originalBtn').css("visibility", "hidden");
		$('#deviceMapping').css("visibility", "hidden");
		// $('#theoriginal').css("visibility", "hidden");
		$("#adminLoginButton").text("管理员登入");
		$('#whichRoute').append(theOptimized);
		currentRoleIsAdmin = false;
		// $("adminLogoutButton").css("visibility", "hidden");
	});

var currentDeviceId;
var currentRoleIsAdmin;
var mappingTable = "<div class='mapping-top' id=''>设备配置管理</div><div id='Content-Left-Mini' class='deviceList'></div><div class='mapping-detail'><table id='deviceDetailTabel'><tr><td><label style='text-align=right'>设备编号：</label></td><td><label id='equDetailId' style='text-align=left'></lable></td></tr><tr><td><label style='text-align=right'>设备名称：</lable></td><td><input id='equDetailName' type='text' style='text-align=left'></input></td></tr><tr><td colspan='2' style='padding:10%'></td></tr><tr><td colspan='2'><button onclick='updateDeviceName()'>更新</button></td></tr></table></div>";
var loginContent = "<table id='loginbody'><tr><td colspan='2'>管理员登入</td></tr><tr><td colspan='2' style='padding:5%'></td></tr><tr><td><label>管理员账号</label></td><td><input id='username' type='text'></input></td></tr><tr><td><label>管理员账号</label></td><td><input id='password' type='password'></input></td></tr><tr><td colspan='2' style='padding:5%'></td></tr><tr><td colspan='2'><button onclick='doLogin()'>登入</button></td></tr><tr><td colspan='2'><label id='errormessage'></label></td></tr></table>"
var theOriginal = "<label id='theoriginalrout'><input name='route' type='radio' value='ihavebeenoriginal' />原始路径</label>";
var theOptimized = "<label id='theoptimizedroute'><input name='route' type='radio' value='ihavebeen' checked='true'/>优化路径</label>";
