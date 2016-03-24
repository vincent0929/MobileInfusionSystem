<?php
	

	require_once 'conn.php';
	$id = intval($_GET['id']);
	
	$sql = "SELECT * FROM patient WHERE id = $id";
	
	$result = mysql_query($sql,$conn);
	while ($row = mysql_fetch_assoc($result)){
		echo (json_encode($row));
	}
	

	