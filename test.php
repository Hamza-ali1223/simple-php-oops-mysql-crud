<?php

require_once "query.php";

$obj = new query();

// $conditionArr = array(
//     'id' => 3,
//     'name' => 'User 3',
//     'email' => 'user3@example.com',
//     'mobile' => '1234567890',
//     'salary' => 50000
// );

// $array = array('id' => 2);

// $updateCon = array(
//     'name' => 'Updated User 2',
//     'email' => 'updateduser2@example.com'
// );


$result = $obj->getData('employee');
// $result = $obj->insertData('employee', $conditionArr);
// $result = $obj->deleteData('employee', $array);
// $result = $obj->updateData('employee', $updateCon, $array); 
echo "<pre>";
print_r($result);
echo "</pre>";