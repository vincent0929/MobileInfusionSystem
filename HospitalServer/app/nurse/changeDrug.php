<?php
	

	include_once '../conn.php';
	
	$drugCode = $_GET['drugCode'];
	$sql = "UPDATE infusion SET currCount=currCount+1 WHERE drugCode=$drugCode AND currCount<allCount";
	
	$result = mysql_query($sql,$conn);
	if(mysql_affected_rows()){
		$sq = "SELECT currCount,allCount FROM infusion WHERE drugCode=$drugCode";
		$res = mysql_query($sq,$conn);
		
		while ($row = mysql_fetch_array($res,MYSQL_ASSOC)){
			echo json_encode($row);
		}
		
		
		
		
		
	}else{
		echo 0;
	}
	
	