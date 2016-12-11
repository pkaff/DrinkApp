<meta charset="utf-8">
<?php

if(isset($_POST['auteh'])) {
    if($_POST['auteh'] == "danielharensvans") {
        setcookie("auth", "345g6g4363465g", time()+3600 * 24 * 30);
        header("Location: .");
    }
}


if(!isset($_COOKIE['auth']) || $_COOKIE['auth'] != "345g6g4363465g") {
    
    ?>
<form method="post">
    <input type="text" name="auteh">
    <input type="submit" value="Send">
</form>
    <?php
} else {
    ?>
<a href="t5gew0y5gespm/DanielsDrinks2.apk">Ladda ned här</a>
    <?php
}

?>