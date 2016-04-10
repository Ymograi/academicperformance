<?php
error_reporting(E_ALL); ini_set('display_errors', 1);
//Contains the functions required to query and update the database
//echo "Functions";
require_once "config.php";
	//Function to add user to system
	function addUser($name,$username,$password,$email,$year,$prog,$mysqli)
	{
		$hash=hashSSHA($password);
		$encrypted_password=$hash["encrypt"];
		$salt=$hash["salt"];
		//$conn=new mysqli("localhost", "root", "", "adbms_api") or die("Error connecting to database.");
	    //echo "connection created";
		$stmt=$mysqli->prepare("insert into faculty_advisor values (?,?,?,?,?,?,?)");
		$stmt->bind_param("sssssss",$username,$name,$encrypted_password,$salt,$email,$year,$prog);
		$result=$stmt->execute();
		$stmt->close();

		//Check for successful store in DB
		if($result)
		{
			$stmt=$mysqli->prepare("select * from faculty_advisor where username=?");
			$stmt->bind_param("s",$username);
			$stmt->execute();
			$user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
		}
		else
			return false;
	}
	// function addStudent($name,$roll,$password,$email,$mysqli,$dept,$prog,$year)
	// {
	// 	$hash=hashSSHA($password);
	// 	$encrypted_password=$hash["encrypt"];
	// 	$salt=$hash["salt"];
	// 	//$conn=new mysqli("localhost", "root", "", "adbms_api") or die("Error connecting to database.");
	//     //echo "connection created";
	// 	$stmt=$mysqli->prepare("insert into student values (?,?,?,?,'',?,?,?)");
	// 	$stmt->bind_param("sssssss",$roll,$encrypted_password,$name,$email,$dept,$prog,$year);
	// 	$result=$stmt->execute();
	// 	$stmt->close();

	// 	//Check for successful store in DB
	// 	if($result)
	// 	{
	// 		$stmt=$mysqli->prepare("select * from student where username=?");
	// 		$stmt->bind_param("s",$username);
	// 		$stmt->execute();
	// 		$user = $stmt->get_result()->fetch_assoc();
 //            $stmt->close();
 //            return $user;
	// 	}
	// 	else
	// 		return false;
	// }

	//Function to validate user credentials for login
	function loginUser($username,$password,$mysqli,$type)
	{   //$conn=new mysqli("localhost", "root", "", "adbms_api") or die("Error connecting to database.");
	    //echo "connection created";
	    if($type=="Faculty Advisor")
	    {
	    	$stmt=$mysqli->prepare("select * from faculty_advisor where username=?");
	    }
	    else
	    {
	    	$stmt=$mysqli->prepare("select * from student where roll_no=?");
	    }
		//echo $stmt;
		$stmt->bind_param("s",$username);

		if($stmt->execute())
		{
			$user=$stmt->get_result()->fetch_assoc();
			$stmt->close();

			//verify the user's password
			$salt=$user['salt'];
			$encrypted_password=$user['password'];
			$hash=checkhashSSHA($salt,$password);//Generating the encrypted pasword from user's input
			if($encrypted_password==$hash)
			{
				//User credentials are valid
				return $user;
			}
		}
		else
		{
				return NULL;
		}
	}

	//Function to check existence of a user
	function checkUser($username,$mysqli)
	{	//$conn=new mysqli("localhost", "root", "", "adbms_api") or die("Error connecting to database.");
	    //echo "connection created";
		$stmt=$mysqli->prepare("select username from faculty_advisor where username=?");
		$stmt->bind_param("s",$username);
		$stmt->execute();
		$stmt->store_result();

		if($stmt->num_rows>0)
		{
			//User exists
			$stmt->close();
			return true;
		}
		else
		{
			$stmt->close();
			return false;
		}

	}

	//Function to encrypt password and return the salt and password
	function hashSSHA($password)
	{
		$salt=sha1(rand());
		$salt=substr($salt,0,10);
		$encrypt=base64_encode(sha1($password.$salt,true).$salt);
		$hash=array("salt"=>$salt,"encrypt"=>$encrypt);
		return $hash;
	}

	//Function to check is password matches
	function checkhashSSHA($salt,$password)
	{
		$hash=base64_encode(sha1($password.$salt,true).$salt);
		return $hash;
	}
?>
