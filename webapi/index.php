<?php
if(isset($_POST["username"])){
    $user = $_POST["username"];
    $pass = $_POST["password"];
    $host = $_POST["host"];
    $db_name = $_POST["database"];
    $sql = $_POST["statement"];
}
else{
    $user = $_GET["username"];
    $pass = $_GET["password"];
    $host = $_GET["host"];
    $db_name = $_GET["database"];
    $sql = $_GET["statement"];
}

$credits = "host=".$host." dbname=".$db_name." user=".$user." password=".$pass;

$conn = pg_connect($credits);

$output = array();
$query = pg_query($conn, $sql);
while($result = pg_fetch_assoc($query)){
    $output[]=$result;
}
print(json_encode($output));
 
pg_close();
?>