<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {

        $name = $_POST['name'];
        $gender = $_POST['gender'];
        $age = $_POST['age'];
        $height = $_POST['height'];
        $weight = $_POST['weight'];
        $bmi = $_POST['bmi'];
        $bmr = $_POST['bmr'];

        

        $link = mysqli_connect("localhost", "root", "", "myapp") or die("Error with MySQL connection" . mysqli_error($link));
        $link -> set_charset("utf8");
    
        $query = "INSERT INTO app (name, gender, age, height, weight, bmi, bmr) VALUES ('$name', '$gender', '$age', '$height', '$weight', '$bmi', '$bmr')";


        if (mysqli_query($link, $query)) {
            echo "Data inserted successfully";
        } else {
            echo "Error inserting data: " . mysqli_error($link);
        }

        $link -> close();
    }
?>
