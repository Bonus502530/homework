<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];

    // 连接数据库（假设数据库连接信息在这里）

    // 检查用户名是否已存在
    $sql_check = "SELECT * FROM users WHERE username = '$username'";
    // 执行查询...

    if ($username_exists) {
        echo "Username already exists.";
    } else {
        // 插入新用户
        $sql_insert = "INSERT INTO users (username, password) VALUES ('$username', '$password')";
        // 执行插入...

        echo "Registration successful!";
    }

    // 关闭数据库连接
}
?>
