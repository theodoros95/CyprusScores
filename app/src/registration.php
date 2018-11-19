<?php

if (!key_exists('username', $_GET) || !key_exists('password', $_GET) || strlen($_GET['password']) != 32) {
    die("0");
}

$link = mysqli_connect('localhost', 'website_oyk', '1234', 'website_oyk');
$sql = "INSERT INTO verify_users (username, password) VALUES (?, ?)";

$stmt = mysqli_prepare($link, $sql);
mysqli_stmt_bind_param($stmt, "ss", $_GET['username'], $_GET['password']);

echo mysqli_stmt_execute($stmt) ? "1" : "0";

mysqli_stmt_close($stmt);
mysqli_close($link);