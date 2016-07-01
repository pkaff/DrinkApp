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
        $add = "";
        if(is_numeric($pathArr[0])) {
            $add = "WHERE id = '" . intval($pathArr[0]) . "'";
        }
        $query = $db->query("SELECT * FROM drink".$add);
        if($query->num_rows > 0) {
            $result = $query->fetch_all(MYSQLI_ASSOC);

            foreach($result as $key => $drink) {
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
                $result[$key]["ingredients"] = $queryIngredients->fetch_all(MYSQLI_ASSOC);
            }


        } else {
            $result = [];
        }
        

        send($result);

    break;
    default: 
        send("Not supported");
}






?>