<?php
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
//print_r($_POST);

require_once 'include\config.php';
require_once 'include\db_functions.php';

$response=array("error"=>FALSE);

if(isset($_POST["roll_no"])&&isset($_POST["cgpa"]))
{
	$roll_no=$_POST["roll_no"];
	$cgpa=$_POST["cgpa"];


	$stm="update student set cgpa=$cgpa where roll_no='$roll_no';";

	//echo $stm;

	$result=$mysqli->query($stm);

	//echo $result;

	if($result)
	{
		$response["error_msg"]="Database updated successfully.";
		$response["roll_no"]=$roll_no;
		$response["cgpa"]=$cgpa;
		echo json_encode($response);
	}
	else
	{
		$response["error_msg"]="Database could not be updated.";
		$response["error"]=TRUE;
		echo json_encode($response);
	}
}
else
{
	$response['error']=TRUE;
	$response['error_msg']="Required parameters are missing.";
	echo json_encode($response);
}
