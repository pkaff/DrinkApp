<?php
include "connect.php";



if(isset($_POST['drink_name'])) {
	echo '<pre>';
	print_r($_POST);
	echo '</pre>';
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
			
			<form mehtod="post">
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