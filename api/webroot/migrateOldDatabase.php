<?php


if(isset($_POST['row'])) {
    include "connect.php";

    $row = $_POST['row'];

    $items = explode(",", $row);
    $name = trim(strtolower($items[1]));
    $glass = trim(strtolower($items[3]));
    $ingredients = $items[4];
    $instructions = $items[5];

    $ingredientsItems = explode("|", $ingredients);
    $ingredients = array();
    foreach($ingredientsItems as $ingredient) {
        // int-name
        $ingredients[] = array(
            "size" => substr($ingredient,0, strpos($ingredient,"-")),
            "name" => strtolower(trim(substr($ingredient, strpos($ingredient,"-")+1))),
            "unit" => "cl"
        );
    }

    global $db;
    $db->query("INSERT INTO drink(name,glass,instructions) VALUES('{$name}','{$glass}','{$instructions}')");
    $drinkID = $db->insert_id;

    foreach($ingredients as $ingredient) {
        $checkIngredientInDB = $db->query("SELECT * FROM ingredient WHERE name = '{$ingredient['name']}'");
        if($checkIngredientInDB->num_rows == 0) {
            $db->query("INSERT INTO ingredient(name) VALUES('{$ingredient['name']}')");
            $ingredientID = $db->insert_id;
        } else {
            $row = $checkIngredientInDB->fetch_assoc();
            $ingredientID = $row['id'];
        }

        

        $db->query("INSERT INTO drink_ingredient(drink_id,ingredient_id,size, unit) VALUES('{$drinkID}','{$ingredientID}','{$ingredient['size']}','cl')");  
    }

}

?>


<form method="post">
    <input type="text" name="row">
    <input type="submit">
</form>