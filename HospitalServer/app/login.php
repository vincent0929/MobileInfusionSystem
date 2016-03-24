<?php
	


	require_once 'conn.php';
	
	$barcode = intval($_GET['codeNumber']);
	
	$sql = "SELECT * FROM patient WHERE barcode = '$barcode'";
	
	
	$result = mysql_query($sql,$conn);
	$res = array();
	while ($row = mysql_fetch_assoc($result)){
		echo(json_encode($row));
	}
	
	
	
	
	
	
	
	
	
