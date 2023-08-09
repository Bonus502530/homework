<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        // 打印接收到的 POST 数据，用于调试
        print_r($_POST);
    
        $id = $_POST['id'];
        $name = $_POST['name'];
        $gender = $_POST['gender'];
        $age = $_POST['age'];
        $height = $_POST['height'];
        $weight = $_POST['weight'];
        $bmi = $_POST['bmi'];
        $bmr = $_POST['bmr'];
    
        // ... 其他操作
            // 数据库连接信息
    $host = "localhost";
    $username = "root";
    $password = "";
    $database = "myapp";

    // 创建数据库连接
    $link = mysqli_connect($host, $username, $password, $database);
    if (!$link) {
        die("Error with MySQL connection: " . mysqli_connect_error());
    }

    // 设置字符集
    $link->set_charset("utf8");

    // 构造更新查询
    $updateQuery = "UPDATE app 
                    SET name='$name', gender='$gender', age='$age', height='$height', weight='$weight', bmi='$bmi', bmr='$bmr'
                    WHERE id='$id'";

    // 执行更新查询
    $result = $link->query($updateQuery);

    // 检查更新是否成功
    if ($result) {
        echo "Data updated successfully.";
    } else {
        echo "Error updating data: " . mysqli_error($link);
    }

    // 关闭数据库连接
    $link->close();
    }
    // 获取 POST 请求中的数据
?>
