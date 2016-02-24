<?php
require("conexion.php");
$fechahoravisita = $_POST['fechahoravisita'];
$iddispositivo = $_POST['iddispositivo'];
$query = " INSERT INTO historialvisitas(FECHAHORAVISITA, IDDISPOSITIVO) VALUES('$fechahoravisita' ,'$iddispositivo')";
mysqli_query($conexion,$query)or die(mysqli_error());
mysqli_close($conexion);
?> 