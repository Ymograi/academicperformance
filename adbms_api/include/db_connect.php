<?php
error_reporting(E_ALL); ini_set('display_errors', 1);
//echo "connect";
require_once 'config.php';
$mysqli=new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE) or die('Error connecting to database');
 
?>