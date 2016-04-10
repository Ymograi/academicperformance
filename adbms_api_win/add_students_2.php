<?php
//mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
//error_reporting(E_ALL); ini_set('display_errors', 1);
//print_r($_POST);
require_once "PHPMailer/PHPMailerAutoload.php";
require_once "include/config.php";
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
	$duplicate=0;


	for($i=0;$i<$nos;$i++)
	{
		$roll_1=(string)$roll[$i];

		$hash=hashSSHA($roll_1);
		$password_1=$hash["encrypt"];
		$salt=$hash["salt"];

		$name_1=(string)$name[$i];
		$email_1=(string)$email[$i];
		//echo $roll[$i];

		$stm="insert into student values ('$roll_1','$password_1','$salt','$name_1','$email_1',NULL,'$dept','$prog',$year,0);";

		//echo $stm;

		$result=$mysqli->query($stm);


		//echo $result;

		if($result)
		{
			//echo "Inside result";
			//Run email script
			//require_once "PHPMailer\PHPMailerAutoload.php";
			$mail = new PHPMailer;
			$mail->SMTPDebug = -3; //Enable SMTP debugging if <0 will show each and everything happening.
			$mail->isSMTP();//Set PHPMailer to use SMTP
			$mail->Host = "smtp.gmail.com";//Set SMTP host name
			$mail->SMTPAuth = true;//Set this to true if SMTP host requires authentication to send email
			$mail->Username = "bloodbank247365@gmail.com";//Provide username and password
			$mail->Password = "bloodbank123";
			$mail->SMTPSecure = "tls";//If SMTP requires TLS encryption then set it
			$mail->Port = 587;//Set TCP port to connect to Mail Details
			$mail->From = "nitcadmin@nitc.ac.in";
			$mail->FromName = "Nitc Admin";
			$mail->addAddress($email_1,$name_1);
			$mail->isHTML(true);
			$mail->Subject = "Request to Enter CGPA";
			$mail->Body = "<b>Hello ".$name_1." You are requested to download the android app from the given link:<a href=</a> and login. The username and password are given below.</b><br><h3>Username: ".$roll_1." <br>Password: ".$roll_1."</h3>";
			$mail->AltBody = "";
			//$a=$mail->send();
			// if(!$mail->send())
			// {
			//     //echo "Mailer Error: " . $mail->ErrorInfo;
			// }
			// else
			// {
			//     //echo "Message has been sent successfully";
			// }
			$count++;
		}
		else
		{	$error=(int)$mysqli->errno;
			//echo $error;
			//echo "Duplicate";
			$response['Duplicate'.$duplicate]=$roll_1;
			$duplicate++;
		}

	}
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
