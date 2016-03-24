<?php

	include_once 'conn.php';
	
	$patientCode = $_GET['patientCode'];
	$seatId = $_GET['seatId'];
	$sql = "UPDATE infusion SET seatId=$seatId WHERE patientCode=$patientCode";
	$result = mysql_query($sql,$conn);
	
	
	/**
	 * 获取到护士id号
	 */
	$sql1 = "SELECT nurseId FROM infusion WHERE  patientCode=$patientCode";
	$res = mysql_query($sql1,$conn);
	$data =array();
	while ($row=mysql_fetch_array($res)){
		echo $row['nurseId'];
	}
	