<?php
	
	include_once '../conn.php';
	
	$patientCode = $_GET['patientCode'];
	$drugCode = $_GET['drugCode'];
	
	$sql = "SELECT COUNT(*) as count FROM infusion WHERE patientCode=$patientCode AND drugCode=$drugCode";
	
	$result = mysql_query($sql,$conn);
	$res = array();
	while ($row = mysql_fetch_assoc($result)){
		
		if($row['count']){
			echo 1;
		}else{
			echo 0;
		}
	
	}
	
	$sql1 = "UPDATE infusion SET state=1 WHERE patientCode=$patientCode AND drugCode=$drugCode";
	
	mysql_query($sql1,$conn);
	
	/**
	 * 更新用户输液开始时间为护士确认的时刻
	 */
	$time = time();
	$sql2 = "UPDATE infusion SET startTime=$time WHERE patientCode=$patientCode AND drugCode=$drugCode";
	
	mysql_query($sql2,$conn);












