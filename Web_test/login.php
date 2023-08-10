<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = $_POST['user_password'];

    // 连接数据库（假设数据库连接信息在这里）

    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("連接失敗: " . $conn->connect_error);
    }


    // 查询用户信息
    $sql = "SELECT * FROM web WHERE username = '$username' AND user_password = '$password'";
    // 执行查询...

    if ($user_found) {
        session_start();
        $_SESSION['username'] = $username; // 将用户名存储在会话中
        header("Location: dashboard.php"); // 跳转到用户的仪表盘页面
        exit();
    } else {
        echo "Invalid username or password.";
    }

    // 关闭数据库连接
}
?>
