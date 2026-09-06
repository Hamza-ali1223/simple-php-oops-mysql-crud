<?php
require_once "database.php";

/* 
SELECT $field FROM $table WHERE $condition LIKE $like ORDER BY $order_by_field $order_by_type LIMIT $limit;

$from -> * or name, email, phone, etc.
$table -> users, products, orders, etc.
$condition -> id = 1, name = 'John', etc.
$like -> the value to search for
$order_by_field -> field name
$order_by_type -> asc or desc
$limit -> number of records to retrieve
*/

class query extends database
{
    // get safe string from user input
    public function get_safe_str($str){
        if($str != ''){
            $str = trim($str);
            return mysqli_real_escape_string($this->connect(), $str);
        }
    }

    public function getData($table, $field = '*', $condition = null, $like = null, $order_by_field = null, $order_by_type = 'DESC', $limit = null)
    {
        $sql = "SELECT $field FROM $table";

        if ($condition) {
            $conditionArr = [];
            foreach ($condition as $key => $value) {
                $conditionArr[] = "$key = '$value'";
            }
            $sql .= " WHERE " . implode(' AND ', $conditionArr);
        }
        if ($like) {
            $sql .= " LIKE $like";
        }
        if ($order_by_field) {
            $sql .= " ORDER BY $order_by_field";
            if ($order_by_type) {
                $sql .= " $order_by_type";
            }
        }
        if ($limit !== null) {
            $sql .= " LIMIT $limit";
        }

        $result = $this->connect()->query($sql);

        if ($result->num_rows > 0) {
            $array = array();
            while ($row = $result->fetch_assoc()) {
                $array[] = $row;
            }
            return $array;
        } else {
            return false;
        }
    }

    public function insertData($table, $data)
    {
        $fields = implode(", ", array_keys($data));
        $values = implode("', '", array_values($data));

        $sql = "INSERT INTO $table ($fields) VALUES ('$values')";
        $result = $this->connect()->query($sql);
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function deleteData($table, $condition)
    {
        $conditionArr = [];
        foreach ($condition as $key => $value) {
            $conditionArr[] = "$key = '$value'";
        }

        $sql = "DELETE FROM $table WHERE " . implode(' AND ', $conditionArr);

        $result = $this->connect()->query($sql);
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function updateData($table, $data, $condition)
    {
        $updateArr = [];
        foreach ($data as $key => $value) {
            $updateArr[] = "$key = '$value'";
        }

        $conditionArr = [];
        foreach ($condition as $key => $value) {
            $conditionArr[] = "$key = '$value'";
        }

        $sql = "UPDATE $table SET " . implode(', ', $updateArr) . " WHERE " . implode(' AND ', $conditionArr);

        $result = $this->connect()->query($sql);
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
}
