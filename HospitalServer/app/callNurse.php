<?php

	require_once '../api/PushMsg.php';
	
	if(!isset($_GET['seatId'])){
		return ;
	}
	
	$seatid =  $_GET['seatId'];//座位号
	$patientname = $_GET['patientname'];//姓名
	$nurseId = $_GET['nurseId'];
	
	$state = $_GET['state'];
	$content = "Hello";
	if ($state=='1'){
		$content = "需要换药了";
	}elseif ($state=='2'){
		$content = "需要其他服务";
	}elseif ($state=='3'){
		$content = "需要拔针了";
	}
	
	$ss = new PushMsgClass();
	$ss->pushMsg($patientname, $seatid , $nurseId , $content);
	
	