<?php
require_once 'include/db_functions.php';

//JSON response array
$response=array("error"=>FALSE);

if(isset($_POST['username'])&&isset($_POST['password'])&&isset($_POST['type']))
{
	//Get the POST parameters
	$username=$_POST['username'];
	$password=$_POST['password'];
	$type=$_POST['type'];
	//Try to login user
	$user=loginUser($username,$password,$mysqli,$type);

	if($user!=false)
	{
		$response["error"]=FALSE;
		$response["name"]=$user["name"];
		$response["username"]=$user["username"];
		$response["email"]=$user["email"];
		$response["type"]=$type;
		$response["prog"]=$user["programme"];
		$response["year"]=$user["year"];
		$response["dept"]=$user["department"];
		if($type=="Student")
		{
			$response["roll_no"]=$user["roll_no"];
			$response["valid"]=$user["valid"];
		}
		echo json_encode($response);
	}
	else
	{
		//Wrong credentials
		$response['error']=TRUE;
		$response['error_msg']="Incorrect login credentials.";
		echo json_encode($response);
	}
}
else
{
	$response["error"] = TRUE;
    $response["error_msg"] = "Required parameters username or password is missing!";
    echo json_encode($response);
}
?>
