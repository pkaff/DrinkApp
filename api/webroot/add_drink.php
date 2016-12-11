<?php
include "connect.php";


function isOk($str) {
	return strlen($str) > 0;
}

if(isset($_POST['drink_name'])) {
	$p = $_POST;
	
	
	if(isOk($p['drink_name']) && isOk($p['drink_glass']) && is_array($p['ingredient_names']) && is_array($p['ingredient_quantities']) && is_array($p['ingredient_units']) && isOk($p['drink_description'])) {
		global $db;
		
		$statement = $db->prepare("INSERT INTO drink(name, glass, instructions) VALUES(?, ?, ?) ");
		$statement->bind_param("sss", strtolower($p['drink_name']), strtolower($p['drink_glass']), strtolower($p['drink_description']));
		$result = $statement->execute();
		
		$drinkId = $db->insert_id;
		
		for ($i = 0; $i < count($p['ingredient_names']); $i ++) {
			$name = $p['ingredient_names'][$i];
			$quantity = $p['ingredient_quantities'][$i];
			$unit = $p['ingredient_units'][$i];
			
			if(isOk($name) && isOk($quantity) && isOk($unit)) {
				$findIngredientStatement = $db->prepare("SELECT * FROM ingredient WHERE name = ?");
				$findIngredientStatement->bind_param("s", strtolower($name));
				$findIngredientStatement->execute();
				$result = $findIngredientStatement->store_result();
				$findIngredientStatement->close();
				
				if($result->num_rows >= 1) {
					$data = $result->fetch_array(MYSQLI_ASSOC);
					$ingredientID = $data['id'];
				} else {
					$statement = $db->prepare("INSERT INTO ingredient(name) VALUES(?) ");
					$statement->bind_param("s", strtolower($name));
					$result = $statement->execute();
					
					$ingredientID = $db->insert_id;
					$statement->close();
				}
				
				$statement = $db->prepare("INSERT INTO drink_ingredient(drink_id, ingredient_id, size, unit) VALUES(?, ?, ?, ?) ");
				$statement->bind_param("iiss", $drinkId, $ingredientID, strtolower($quantity), strtolower($unit));
				$result = $statement->execute();
				$statement->close();
				
				echo '<pre>Drink inserted</pre>';
			}
		}
	}
}
?>
<html>
	<head>
			<script
		  src="https://code.jquery.com/jquery-3.1.1.min.js"
		  integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
		  crossorigin="anonymous"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
		<style type="text/css">
			.container {
				width: 500px;
			}
			
			.fa-plus-square-o, .fa-minus-square-o{
				font-size: 30pt;
				cursor: pointer;
				color: #337ab7;
			}
			
			.fa-minus-square-o {
				margin-top: -2px;
			}
			
			
			.ingredient_container {
				margin: 40px 0;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				
				$("#jsClickMe").click(function(){
					$(".selected_ingredients").append('<div class="ingredient"><div class="row"> <div class="col-xs-6"><input type="text" class="form-control" name="ingredient_names[]" placeholder="Ingredient Name"> </div><div class="col-xs-2"><input type="text" class="form-control" name="ingredient_quantities[]" placeholder="Qty"> </div><div class="col-xs-2"><input type="text" class="form-control" name="ingredient_units[]" placeholder="Unit"> </div><div class="col-xs-2"><i class="fa fa-minus-square-o jsDeleteMe"></i> </div></div></div>');
					
					$(".jsDeleteMe").click(function(e, i) {
						$(e.target).parents(".ingredient").remove();
					});
				});
				
				$(".jsDeleteMe").click(function(e, i) {
					$(e.target).parents(".ingredient").remove();
				});
			});
		</script>
	</head>
	<body>
		<div class="container">
			<h1>Add drink</h1>
			
			<form method="post">
				<input type="text" class="form-control" name="drink_name" placeholder="Drink name">
				<p></p><p></p>
				<strong>Choose glass</strong>
				<select class="form-control" name="drink_glass">
					<option>margarita</option>
					<option>highball</option>
					<option>margarita/martini</option>
					<option>champagne</option>
				</select>
				
				
				<div class="ingredient_container">
					<i class="fa fa-plus-square-o" id="jsClickMe"></i>
					<div class="selected_ingredients">
						<div class="ingredient">
							<div class="row">
							  <div class="col-xs-6">
								<input type="text" class="form-control" name="ingredient_names[]" placeholder="Ingredient Name">
							  </div>
							  <div class="col-xs-2">
								<input type="text" class="form-control" name="ingredient_quantities[]" placeholder="Qty">
							  </div>
							  <div class="col-xs-2">
								<input type="text" class="form-control" name="ingredient_units[]" placeholder="Unit">
							  </div>
							  <div class="col-xs-2">
								<i class="fa fa-minus-square-o jsDeleteMe"></i>
							  </div>
							</div>
						</div>
					</div>
				</div>
				
				
				<p></p><p></p>
				<strong>Description</strong>
				<textarea name="drink_description" class="form-control"></textarea>
				
				<p></p><p></p>
				<input type="submit" class="btn btn-lg btn-primary btn-block" value="Add!">
			</form>
		</div>		
	</body>
</html>