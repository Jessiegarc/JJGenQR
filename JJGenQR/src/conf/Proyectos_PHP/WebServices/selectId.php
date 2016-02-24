<?php
$id =$_GET['valor'];
$con=mysql_connect("localhost","root","") or die("Sin conexion");
mysql_select_db("qr");
$sql="select NOMBREARTICULO,DESCRIPCIONARTICULO,IMAGENUNOARTICULO,IMAGENDOSARTICULO,IMAGENTRESARTICULO,CODIGOQRARTICULO from articulos where CODIGOQRARTICULO='$id'";

$rs=mysql_query($sql,$con);
while($row=mysql_fetch_object($rs)){
	$datos[]=$row;
}
echo json_encode($datos);
?>