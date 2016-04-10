<?php
print_r($_POST);
$response=array("error"=>FALSE);
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
error_reporting(E_ALL); ini_set('display_errors', 1);

require_once 'include\config.php';
require_once 'include\db_functions.php';

if(isset($_POST['rolls'])&&isset($_POST['valid']))
{
	$roll=$_POST['rolls'];
	// $cgpa=$_POST['cgpa'];
	// $dept=$_POST['dept'];
	// $prog=$_POST['prog'];
	// $year=$_POST['year'];
	$valid=$_POST['valid'];
	$nos=sizeof($roll);
	$count=0;
	echo $nos;

	for($i=0;$i<$nos;$i++)
	{
		$stm="update student set valid=$valid[$i] where roll_no='$roll[$i]';";

		$result=$mysqli->query($stm);

		//echo $result;

		if($result && $valid[$i])
		{
			//echo "inside if";
			$count++;
		}
		else if(!$result)
		{
			$response["error"]=TRUE;
		}

	}
	if($response["error"]==TRUE)
		$response["err_msg"]="One or more entries could not be updated.";
	else
		$response["err_msg"]="Database updated.";
	//echo "Count is ".$count;
	$response["update_count"]=$count;
	$response["no_count"]=$nos-$count;
	echo json_encode($response);
}

else
{
	$response['error']=TRUE;
	$response['error_msg']="Required parameters are missing.";
	echo json_encode($response);
}
