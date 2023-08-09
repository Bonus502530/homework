<?php
// Include your database connection code here
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "myapp";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check if the 'id' parameter is set
if (isset($_POST['id'])) {
    $id = $_POST['id'];

    // Delete the record with the given id
    $sql = "DELETE FROM app WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $id);

    if ($stmt->execute()) {
        // Record deleted successfully
        echo "Record deleted successfully";
    } else {
        // Error deleting record
        echo "Error deleting record: " . $stmt->error;
    }

    $stmt->close();
} else {
    echo "ID parameter not provided";
}

$conn->close();
?>
