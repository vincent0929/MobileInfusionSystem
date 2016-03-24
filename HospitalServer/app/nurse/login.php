<?php

	require_once '../conn.php';
	
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	
	$sql = "SELECT id FROM nurse WHERE username = '$username' AND password = '$password'";
	
	$result = mysql_query($sql,$conn);
	while ($row = mysql_fetch_assoc($result)){
		echo $row['id'];
		return ;
	}
	echo 0;
	
	