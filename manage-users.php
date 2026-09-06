<?php
require_once "query.php";

$obj = new query();

$name = '';
$email = '';
$mobile = '';
$id = '';

//get user data for edit
if (isset($_GET['id']) && !empty($_GET['id'])) {
    $id = $obj->get_safe_str($_GET['id']);
    $conditionArr = array('id' => $id);
    $userData = $obj->getData('users', '*', $conditionArr);
    $name = $userData[0]['name'];
    $email = $userData[0]['email'];
    $mobile = $userData[0]['mobile'];
}

//Add new user
if (isset($_POST['submit'])) {
    $name = $obj->get_safe_str($_POST['name']);
    $email = $obj->get_safe_str($_POST['email']);
    $mobile = $obj->get_safe_str($_POST['mobile']);

    $dataArr = array(
        'name' => $name,
        'email' => $email,
        'mobile' => $mobile
    );

    //check if id is set for update or insert
    if($id == ''){
        $result = $obj->insertData('users', $dataArr);
    } else {
        $conditionArr = array('id' => $id);
        $result = $obj->updateData('users', $dataArr, $conditionArr);
    }

    //redirect to index.php after insertion
    header("Location: index.php");
    exit();
}
?>

<!doctype html>
<html lang="en-US">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Manage User - PHP Object Oriented Programming CRUD</title>
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
            <div class="card-header"><i class="fa fa-fw fa-plus-circle"></i> <strong><?php echo isset($_GET['id']) ? 'Edit' : 'Add'; ?> User</strong> <a href="index.php" class="float-right btn btn-dark btn-sm"><i class="fa fa-fw fa-globe"></i> Browse Users</a></div>
            <div class="card-body">
                <div class="col-sm-6">
                    <h5 class="card-title">Fields with <span class="text-danger">*</span> are mandatory!</h5>
                    <form method="post">
                        <div class="form-group">
                            <label>Name <span class="text-danger">*</span></label>
                            <input type="text" name="name" id="name" class="form-control" placeholder="Enter name" required value="<?php echo isset($name) ? $name : ''; ?>">
                        </div>
                        <div class="form-group">
                            <label>Email <span class="text-danger">*</span></label>
                            <input type="email" name="email" id="email" class="form-control" placeholder="Enter email" required value="<?php echo isset($email) ? $email : ''; ?>">
                        </div>
                        <div class="form-group">
                            <label>Mobile <span class="text-danger">*</span></label>
                            <input type="tel" class="tel form-control" name="mobile" id="mobile" placeholder="Enter mobile" required value="<?php echo isset($mobile) ? $mobile : ''; ?>">
                        </div>
                        <div class="form-group">
                            <button type="submit" name="submit" value="submit" id="submit" class="btn btn-primary"><i class="fa fa-fw fa-plus-circle"></i> <?php echo isset($_GET['id']) ? 'Update' : 'Add'; ?> User</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/jquery.caret/0.1/jquery.caret.js"></script>
</body>

</html>