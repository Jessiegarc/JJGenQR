<?php
$servidor = "localhost";
$user = "root";
$pass = "MIAjjqr2016";
$bd = "qr";
//PHP 5.5
$conexion = mysqli_connect($servidor, $user, $pass, $bd);
if(mysqli_connect_errno()){
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
?> 