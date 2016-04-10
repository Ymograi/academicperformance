<?php
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
error_reporting(E_ALL); ini_set('display_errors', 1);
//print_r($_POST);

require_once 'include\config.php';
require_once 'include\db_functions.php';

if(isset($_POST['dept'])&&isset($_POST['prog'])&&isset($_POST['year']))
{  $response;
	//$name=$_POST['name'];
	//$roll=$_POST['roll'];
	//$email=$_POST['email'];
	$response=array();
	$dept=$_POST['dept'];
	$prog=$_POST['prog'];
	$year=$_POST['year'];
	// $dept="CSED";
	// $prog="MCA";
	// $year=2014;
	$nos=0;
	$stmval="select count(*) from student where valid=1 and department='$dept' and programme='$prog' and year=$year;";
  	$nos=$mysqli->query($stmval);
	$row=$nos->fetch_array(MYSQLI_NUM);
	$nos=$row[0];
	echo "There are ".$nos." Valid Students.";
	$stmtot="select count(*) from student;";
	 $total=$mysqli->query($stmtot);
	 $row=$total->fetch_array(MYSQLI_NUM);
	 $total=$row[0];
	 echo "<br>There are total of ".$total." Students.";
	$stm="select SUM(CGPA) from student where valid=1 and department='$dept' and programme='$prog' and year=$year;";
	$cgpa=$mysqli->query($stm);
	$row=$cgpa->fetch_array(MYSQLI_NUM);
	$cgpa=$row[0];
	echo "The Average Cgpa is ".$cgpa/$nos.".<br>";
	$response["API"]=$cgpa/$nos;
	$response["AP"]=2*$cgpa/$nos;
	$response["VALID"]=$nos;
	$response["INVALID"]=$total-$nos;

	$stm2="update academic_performance set api=$cgpa/$nos where department='$dept' and programme='$prog' and year=$year;";
	$result=$mysqli->query($stm2);

	$stm3="update academic_performance set ap=2*$cgpa/$nos where department='$dept' and programme='$prog' and year=$year;";
	$result=$mysqli->query($stm3);

	print_r($response);
	echo json_encode($response);
}
	?>
