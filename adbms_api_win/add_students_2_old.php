<?php
//mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
//print_r($_POST);

require_once 'include/config.php';
require_once 'include/db_functions.php';

if(isset($_POST['name'])&&isset($_POST['roll'])&&isset($_POST['email'])
	&&isset($_POST['dept'])&&isset($_POST['prog'])&&isset($_POST['year']))
{
	$name=$_POST['name'];
	$roll=$_POST['roll'];
	$email=$_POST['email'];
	$dept=$_POST['dept'];
	$prog=$_POST['prog'];
	$year=$_POST['year'];
	$nos=sizeof($roll);
	//echo "inside if";


	$response=array("error"=>FALSE);

	$count=0;//Successful additions
	$duplicate=0;//Duplicate count


	for($i=0;$i<$nos;$i++)
	{
		$roll_1=(string)$roll[$i];

		$hash=hashSSHA($roll_1);
		$password_1=$hash["encrypt"];
		$salt=$hash["salt"];

		$name_1=(string)$name[$i];
		$email_1=(string)$email[$i];
		echo $roll[$i];

		$stm="insert into student values ('$roll_1','$password_1','$salt','$name_1','$email_1',NULL,'$dept','$prog',$year,0);";

		//echo $stm;

		$result=$mysqli->query($stm);


		//echo $result;

		if($result)
		{
			//echo "Inside result";
			//Run email script
			$count++;

				// for($i=0;$i<$nos;$i++)
				// {
				// 	$headers="";
				// 	$headers .= 'Content-type: text/html;charset=iso-8859-1' . "rn";
				// 	$headers .= 'From: System Admin <noreply@bloodbank247365.com>' . "rn";
				// 	$body ="<html>
				// 	<p>Dear '$name[$i]',<br/><br/>With respect to your request for Account Removal from the database.<a href='http://localhost/Bloodbank/Donor/donor_deletion_code.php'>Click here</a>  and enter the code given below.<br><br> CODE : $code <br><br>We hope you are in best of your health and we will like to hear back from you.<br>Your Blood can save lives!!! Be a Blood Donor !!! <br><br><b><u><h3>Five minutes of your time + 350 ml. of your blood = One life saved.</h3>
				// 	<img src=http://dksf.org.np/wp-content/uploads/2013/12/blood-donation-banner.jpg align=center>";
				// 	mail($_POST['$email[$i]'],"Account Removal\n",$body,$headers);
				// }


			// $response["error"]=FALSE;
			// $response['error_msg']="Database updated Successfully. ";
			// $response["nos"]=$nos;
			// echo json_encode($response);

		}

		else if($mysqli->errno)
		{	$error=(int)$mysqli->errno;
			echo $error;
			echo "Duplicate";
			$response['Duplicate'.$duplicate]=$roll_1;
			$duplicate++;
		}
}

// 	}
	$response["duplicate"]=$duplicate;
	$response["error"]=FALSE;
	$response['error_msg']="Database updated Successfully. ";
	$response["nos"]=$count;
	echo json_encode($response);
}
else
	{
		$response['error']=TRUE;
		$response['error_msg']="Required parameters are missing.";
		echo json_encode($response);
	}
// ?>
