<?php
require_once "query.php";

$obj = new query();

//get all users
$result = $obj->getData('users', '*', null, null, 'id', 'DESC');

//delete user
if(isset($_GET['type']) && $_GET['type'] == 'delete'){
    if(isset($_GET['id'])){
        $id = $obj->get_safe_str($_GET['id']);
        $conditionArr = array('id' => $id);
        $result = $obj->deleteData('users', $conditionArr);

        //redirect to index.php after deletion
        header("Location: index.php");
        exit();
    }
}

?>
<!doctype html>
<html lang="en-US">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>User Listing - PHP Object Oriented Programming CRUD</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css">
    <style>
        .container {
            margin-top: 100px;
        }
    </style>
</head>

<body>
    <div class="container">
        <div class="card">
            <div class="card-header">
                <i class="fa fa-fw fa-globe"></i>
                <strong>Browse User</strong>
                <a href="manage-users.php" class="float-right btn btn-dark btn-sm"><i class="fa fa-fw fa-plus-circle"></i> Add Users</a>
            </div>
        </div>
        <hr>
        <div>
            <table class="table table-striped table-bordered">
                <thead>
                    <tr class="bg-primary text-white">
                        <th>Sr#</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Mobile</th>
                        <th class="text-center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <?php
                    if (isset($result[0]) && !empty($result)) {
                        $id = 1;
                        foreach ($result as $row) { ?>
                            <tr>
                                <td><?php echo $id; ?></td>
                                <td><?php echo $row['name']; ?></td>
                                <td><?php echo $row['email']; ?></td>
                                <td><?php echo $row['mobile']; ?></td>
                                <td align="center">
                                    <a href="manage-users.php?id=<?php echo $row['id']; ?>" class="text-primary"><i class="fa fa-fw fa-edit"></i> Edit</a> |
                                    <a href="index.php?id=<?php echo $row['id']; ?>&type=delete" class="text-danger"><i class="fa fa-fw fa-trash"></i> Delete</a>
                                </td>
                            </tr>
                        <?php
                            $id++;
                        }
                    } else { ?>
                        <tr>
                            <td colspan="6" align="center">No Records Found!</td>
                        </tr>
                    <?php } ?>
                </tbody>
            </table>
        </div>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/jquery.caret/0.1/jquery.caret.js"></script>
</body>

</html>