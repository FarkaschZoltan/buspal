<?php
$user = $_GET["username"];
$pass = $_GET["password"];
$host = $_GET["host"];
$db_name = $_GET["db_name"];
$sql = $_GET["statement"]

$conn = pg_connect("host=$host dbname=$db_name user=$user password=$pass");

$output = array();
$query = pg_query($sql, $conn);
while($result = pg_fetch_assoc($query)){
    $output[]=$result;
}
print(json_encode($output));
 
mysql_close();
?>
