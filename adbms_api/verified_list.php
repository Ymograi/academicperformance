<?php
$response=array("error"=>FALSE);
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
error_reporting(E_ALL); ini_set('display_errors', 1);
//print_r($_POST);

require_once 'include\config.php';
require_once 'include\db_functions.php';

if(isset($_POST["dept"])&&isset($_POST["prog"])&&isset($_POST["year"]))
{
	$dept=$_POST["dept"];
	$prog=$_POST["prog"];
	$year=$_POST["year"];


	$stm="select * from student where department='$dept' and programme='$prog' and year='$year' and valid=1 and cgpa IS NOT NULL ORDER BY cgpa DESC; ";

	//echo $stm;

	$result=$mysqli->query($stm);

	if(!$result)
	{
		$response['error']=TRUE;
		$response['error_msg']="Error in fetching data.";
		echo json_encode($response);
	}

	else if($result->num_rows>0)
	{
		//$students=$result->fetch_assoc();
		$json = mysqli_fetch_all ($result, MYSQLI_ASSOC);
		$response["students"]=json_encode($json);
		$response["nos"]=$result->num_rows;
		$response["error_msg"]="Fetched ".$result->num_rows." rows.";
		echo json_encode($response);
	}
	else
	{
		$response["error_msg"]="There are no such students who have not been validated.";
		$response["nos"]=0;
		echo json_encode($response);
	}
}

else
{
	$response['error']=TRUE;
	$response['error_msg']="Required parameters are missing.";
	echo json_encode($response);
}
