<?php


	include_once '../conn.php';
	$id = $_GET['id'];
	
	$sql = "SELECT id,patientCode,drugCode,seatId FROM infusion WHERE state=0 and seatId IS NOT NULL AND nurseId =$id";
	
	$result = mysql_query($sql,$conn);
	$res = array();
	while ($row = mysql_fetch_assoc($result)){
		$res[] = json_encode($row);
		
	}

	foreach ($res as $value){
		echo $value."#";
	}

	
	//echo(json_encode($res));
	
