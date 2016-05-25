<?php
$id =$_GET['valor'];
$con=mysql_connect("localhost","root","MIAjjqr2016") or die("Sin conexion");
mysql_select_db("qr");
$sql="select NOMBREMUSEO,FECHAFUNDACIONMUSEO,FUNDADORMUSEO,HISTORIAMUSEO,IDMUSEO from museo where IDMUSEO='$id'";
$rs=mysql_query($sql,$con);
while($row=mysql_fetch_object($rs)){
	$datos[]=$row;
}
echo json_encode($datos);
?>