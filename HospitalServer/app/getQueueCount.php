<?php

	require_once './conn.php';
	$patientCode = $_REQUEST['patientCode'];
	// $seatId = $_REQUEST['seatId'];
	
	$sql = "SELECT COUNT(DISTINCT patientCode) as mycount FROM infusion WHERE patientCode<$patientCode AND state=0";
	
	$result = mysql_query($sql,$conn);
	
	while ($row = mysql_fetch_array($result)){
		echo($row['mycount']);

		// $sqlupdate = "UPDATE infusion SET seatId=$seatId WHERE patientCode=$patientCode";
		// mysql_query($sqlupdate,$conn);
		// return ;
	}


	
	return 0;
	
	
	