<?php
include "connect.php";
global $db;
include "base.php";

$path;
if(isset($_SERVER['PATH_INFO'])) {
    $path = $_SERVER["PATH_INFO"];
    $pathArr = explode("/", $path);
    array_shift($pathArr);
} else {
    $pathArr = [""];
}

$HTTPverb = $_SERVER["REQUEST_METHOD"];



switch($HTTPverb) {
    
    case "GET": 
        if(is_numeric($pathArr[0])) {
            // get 1 drink, with all ingredients
            $query = $db->query("SELECT * FROM drink WHERE id = '" . intval($pathArr[0]) . "'");
            if($query->num_rows > 0) {
                $drink = $query->fetch_assoc();

                $queryIngredients = $db->query("
                    SELECT
                        ingredient.id as id,
                        ingredient.name as name,
                        drink_ingredient.size as size,
                        drink_ingredient.unit as unit
                    FROM
                        drink_ingredient
                    INNER JOIN ingredient ON ingredient.id = drink_ingredient.	ingredient_id
                    WHERE 
                        drink_ingredient.drink_id = {$drink['id']}
                ");

                $drink["ingredients"] = $queryIngredients->fetch_all(MYSQLI_ASSOC);
                $result = [$drink];
            } else {
                $result = [];
            }
        } else {
            // get list of drinks, only names and ids 
            $query = $db->query("SELECT * FROM drink");
            if($query->num_rows > 0) {
                $result = $query->fetch_all(MYSQLI_ASSOC);
            } else {
                $result = [];
            }
        }

        send($result);

    break;
    default: 
        send("Not supported");
}






?>