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
		+ "<tr>" 
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
			+ "<td colspan='6'><font color='#cd0a0a'>" + 暂无记录 + "</font></td>" 
		+ "</tr>";
	}
	tableStr = tableStr + "</table>";
	$("#tableAjax").html(tableStr);
}

function cleanValidators() {
	$('#fromValidator').text("");
	$('#endValidator').text("");
}
	
$(document).ready(function() {
		showDeviceList();
		
		$('#fromDate').val(moment().format('YYYY-MM-DD'));
		$('#endDate').val(moment().format('YYYY-MM-DD'));
		$('#fromTime').val(moment().format('hh:mm:ss'));
		$('#endTime').val(moment().format('hh:mm:ss'));
	});