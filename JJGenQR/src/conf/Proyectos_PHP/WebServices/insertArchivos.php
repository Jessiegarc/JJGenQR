<?php
require("conexion.php");
$fechavisitadispositivo = $_POST['fechavisitadispositivo'];
$iddispositivo = $_POST['iddispositivo'];
$query = " INSERT INTO historialdispositivos(FECHAVISITADISPOSITIVO, IDDISPOSITIVO) VALUES('$fechavisitadispositivo' ,'$iddispositivo')";
mysqli_query($conexion,$query)or die(mysqli_error());
mysqli_close($conexion);
?> 