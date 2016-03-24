<?php

	/**
	 * 数据库初始化
	 */
	$conn = mysql_connect (SAE_MYSQL_HOST_M . ':' . SAE_MYSQL_PORT, SAE_MYSQL_USER, SAE_MYSQL_PASS );
	mysql_select_db ( SAE_MYSQL_DB, $conn );

	mysql_query("set names 'UTF-8'");


