<?php

	require_once 'conn.php';
	
	$patientCode = $_GET['patientCode'];
	$sql = "SELECT drugName FROM infusion_drugs,infusion WHERE patientCode=$patientCode AND infusion.`drugcode` = infusion_drugs.`drugcode`";
	
	$result = mysql_query($sql,$conn);
	while ($row = mysql_fetch_assoc($result)){
		echo $row['drugName'].',';
	}
	
	
	echo "###";
	
	$sql1 = "SELECT currCount,allCount FROM infusion WHERE patientCode=$patientCode";
	$res = mysql_query($sql1,$conn);
	while ($rows = mysql_fetch_assoc($res)){
		$data['currCount']=$rows['currCount'];
		$data['allCount']=$rows['allCount'];
		
	}
	echo(json_encode($data));
	echo "###";
	
	$sql2 = "SELECT CONCAT(startTime,',',remainTime) AS mytime FROM infusion WHERE patientCode = $patientCode";
	$timeres = mysql_query($sql2,$conn);
	while ($rows = mysql_fetch_assoc($timeres)){
		echo $rows['mytime'];
	}
	
	
	
	