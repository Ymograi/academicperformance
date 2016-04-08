<?php
 error_reporting(E_ALL); ini_set('display_errors', 1);
/**
 * Database config variables
 */
define("DB_HOST", "localhost");
define("DB_USER", "root");
define("DB_PASSWORD", "");
define("DB_DATABASE","api_calculator");
$mysqli=new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE) or die('Error connecting to database');
//echo "config";
?>
