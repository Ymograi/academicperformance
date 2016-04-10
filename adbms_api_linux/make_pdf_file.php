<?php
$response=array("error"=>FALSE);
//mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
//error_reporting(E_ALL); ini_set('display_errors', 1);
//print_r($_POST);
require_once 'fpdf/fpdf.php';
require_once 'include/config.php';
require_once "PHPMailer/PHPMailerAutoload.php";
require_once 'include/db_functions.php';

if(isset($_POST["dept"])&&isset($_POST["prog"])&&isset($_POST["year"]))
{ $pdf=new FPDF();
	$pdf->Addpage();
	$dept=$_POST["dept"];
	$prog=$_POST["prog"];
	$year=$_POST["year"];
	$email=$_POST["email"];
	print_r($_POST);
	//  $dept="CSED";//$_POST["dept"];
	//  $prog="MCA";//$_POST["prog"];
	//  $year="2014";//$_POST["year"];
  //  $email="ymograi@gmail.com";
	$stmp="select roll_no,name,email,cgpa,department,programme,year from student where department='$dept' and programme='$prog' and year='$year' and valid=1 and cgpa IS NOT NULL ORDER BY cgpa DESC; ";
  $result=$mysqli->query($stmp);
	$info=$result->fetch_fields();
	$pdf->SetFont('Arial','B',10);
  $pdf->Cell(30,12,"Roll No",1);
	$pdf->Cell(40,12,"Name",1);
	$pdf->Cell(50,12,"E-mail",1);
	$pdf->Cell(20,12,"Cgpa",1);
	$pdf->Cell(23,12,"Department",1);
	$pdf->Cell(22,12,"Programme",1);
	$pdf->Cell(10,12,"Year",1);
	//$pdf->AddPage();
	//$pdf->SetFont('Arial','B',12);
	//print_r ($result->fetch_array(MYSQLI_NUM));
	for($i=0;$i<$result->num_rows;$i++)
		{ $val=$result->fetch_array(MYSQLI_NUM);
			$pdf->SetFont('Arial','',10);
			$pdf->ln();

			 $pdf->Cell(30,12,$val[0],1);
			$pdf->Cell(40,12,$val[1],1);
			$pdf->Cell(50,12,$val[2],1);
			$pdf->Cell(20,12,$val[3],1);
			$pdf->Cell(23,12,$val[4],1);
			$pdf->Cell(22,12,$val[5],1);
			$pdf->Cell(10,12,$val[6],1);
		}
$content=$pdf->Output('doc.pdf','F');
if($email!=""){
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
$mail->isHTML(TRUE);
$mail->Subject = "The Pdf Document of your class as requested";
$mail->Body = "<b>Pdf has been attached.</b><br>";
$mail->AltBody = "";
$mail->AddAddress($email,"Faculty Advisor");
$mail->SMTPKeepAlive = true;
$name=$dept.$prog.$year;
$mail->AddAttachment("doc.pdf",$name);
$mail->send();
}
$response["error"] = "FALSE";
echo json_encode($response);
}

else{
	$response['error']=TRUE;
	$response['error_msg']="Required parameters are missing.";
	echo json_encode($response);
}
?>
