<?php
$user = $_GET["username"];
$pass = $_GET["password"];
$host = $_GET["host"];
$db_name = $_GET["db_name"];
$sql = $_GET["statement"]

$conn = mysql_connect($host,$user,$pass);
mysql_select_db($db_name, $conn);

mysql_query("SET character_set_results=utf8", $conn);
mb_language('uni'); 
mb_internal_encoding('UTF-8');

mysql_query("SET NAMES 'utf8'",$conn);
mysql_query("SET CHARACTER SET 'utf8'",$conn);

$output = array();
$query=mysql_query($sql, $conn);
while($result=mysql_fetch_assoc($query)){
    $output[]=$result;
}
print(json_encode($output));
 
mysql_close();
?>
