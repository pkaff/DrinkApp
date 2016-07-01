<?php

function send($mixed) {
    header("Content-Type: text/json");
    echo json_encode($mixed);
    die();
}

?>