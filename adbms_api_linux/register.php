<?php
error_reporting(E_ALL); ini_set('display_errors', 1);
//print_r($_POST);
//Registration endpoint to communiate between app and DB
//echo "here";
require_once 'include/db_functions.php';
//require_once 'include/db_connect.php';

//JSON response array
$response=array('error'=>FALSE);

if(isset($_POST['name'])&&isset($_POST['username'])&&isset($_POST['password'])&&isset($_POST['email'])&&isset($_POST['year'])
	&&isset($_POST['prog']))
{
	//echo "ifblock";
	//Get the POST parameters
	$name=$_POST['name'];
	$email=$_POST['email'];
	$password=$_POST['password'];
	$username=$_POST['username'];
	$year=$_POST['year'];
	$prog=$_POST['prog'];
	echo $username;
	//Check if user already exists
	if(checkUser($username,$mysqli))
	{
		$response['error']=TRUE;
		$response['error_msg']="User with username ".$username." already exists.";
		echo json_encode($response);
	}
	else
	{
		//Create new user
		$user=addUser($name,$username,$password,$email,$year,$prog,$mysqli);
		if($user)
		{
			$response['error']=FALSE;
			$response['name']=$name;
			$response['username']=$username;
			$response['email']=$email;
			echo json_encode($response);
		}
		else
		{
			//Data not stored
			$response['error']=TRUE;
			$response['error_msg']="Error in storing data.";
			echo json_encode($response);
		}
	}

}
else
{
	$response['error'] = TRUE;
    $response['error_msg'] = "Required parameters (name, username, email or password) is missing!";
    echo json_encode($response);
}
?>