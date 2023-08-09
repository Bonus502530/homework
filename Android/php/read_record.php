<?php
    $ip = $_SERVER['REMOTE_ADDR'];
    $link = mysqli_connect("localhost", "root", "", "myapp");
    $link -> set_charset("utf8");


    $result = $link -> query("SELECT  `id`, `name`, `bmr`  FROM `app`");

    $output = array();
    while($row = $result -> fetch_assoc()) {
        $id = $row["id"];
        $name = $row["name"];
        $bmr = $row["bmr"];
        $output[] = array("id" => $id, "name" => $name, "bmr" => $bmr);
    }

    echo json_encode($output);
    $link -> close();

?>
