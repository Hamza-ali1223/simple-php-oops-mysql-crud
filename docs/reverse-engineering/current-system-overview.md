# Current PHP CRUD System Overview

This document describes the existing PHP/MySQL application as it exists in the repository today. It is intended as reverse-engineering evidence for a future Java migration. It does not describe the future Java system, and no behaviour has been changed.

## Current technology stack

The table below records only technologies that are present in the repository code or README.

| Area | Current technology |
|---|---|
| Backend language | PHP 7+ |
| Database | MySQL |
| Database API | MySQLi |
| Frontend | HTML5 |
| UI styling | Bootstrap 4 (Bootstrap 4.2.1 is referenced in the page markup) |
| Icons | Font Awesome (Font Awesome 5.6.3 is referenced in the page markup) |

## Current file responsibilities

| File | Current responsibility |
|---|---|
| `index.php` | Lists all users in a Bootstrap table and handles user deletion when a `type=delete` request is received. |
| `manage-users.php` | Displays the add/edit user form. Loads an existing user when an `id` is supplied, and processes both insert (new user) and update (existing user) submissions. |
| `query.php` | Defines the `query` class, which extends `database`. Contains reusable methods for input cleaning (`get_safe_str`), selecting (`getData`), inserting (`insertData`), updating (`updateData`), and deleting (`deleteData`) records. |
| `database.php` | Defines the `database` class with a protected `connect()` method that creates and returns a MySQLi connection using hard-coded local settings. |
| `test.php` | A manual/development example that instantiates `query` and calls `getData('employee')`. It is not an automated test suite. |
| `README.md` | Documents the project purpose, features, technology list, database schema, installation steps, and a short “How It Works” explanation. |

## Current database model

According to `README.md`:

- Database: `oops_crud`
- Table: `users`

| Field | Purpose / type from current schema |
|---|---|
| `id` | Unsigned auto-increment primary key (`INT UNSIGNED AUTO_INCREMENT PRIMARY KEY`). |
| `name` | User name (`VARCHAR(100) NOT NULL`). |
| `email` | User email (`VARCHAR(150) NOT NULL`). |
| `mobile` | User mobile number (`VARCHAR(20) NOT NULL`). |

## Current user workflows

### 1. View users

1. A browser requests `index.php`.
2. `index.php` requires `query.php`, creates a `query` object, and calls `getData('users', '*', null, null, 'id', 'DESC')`.
3. `query::getData()` builds a `SELECT * FROM users ORDER BY id DESC` statement and executes it through the MySQLi connection returned by `database::connect()`.
4. If rows are returned, `index.php` renders them in an HTML table with Sr#, Name, Email, Mobile, and Action columns. Each row includes an Edit link and a Delete link.
5. If no rows are returned, `index.php` displays “No Records Found!”.

### 2. Add a user

1. From the user list in `index.php`, the user follows the “Add Users” link to `manage-users.php` (without an `id` parameter).
2. `manage-users.php` initialises empty `name`, `email`, and `mobile` variables. Because no `id` is present, the form header shows “Add User” and the submit button shows “Add User”.
3. The user fills in the Name, Email, and Mobile fields and submits the form. The form posts back to the same page (`method="post"` with no explicit `action`).
4. On POST, `manage-users.php` cleans each value with `get_safe_str()`, builds a data array, and—because `$id` is empty—calls `insertData('users', $dataArr)`.
5. `query::insertData()` builds an `INSERT INTO users (...) VALUES (...)` statement and executes it.
6. After the insert call, `manage-users.php` sends a `Location: index.php` header and exits, returning the user to the list.

### 3. Edit a user

1. From the user list, the user follows an Edit link of the form `manage-users.php?id=<id>`.
2. `manage-users.php` cleans the `id` with `get_safe_str()` and calls `getData('users', '*', array('id' => $id))`.
3. The returned row is used to pre-fill the `name`, `email`, and `mobile` form fields. Because an `id` is present, the header shows “Edit User” and the submit button shows “Update User”.
4. When the form is submitted, `manage-users.php` cleans the POSTed values and—because `$id` is not empty—calls `updateData('users', $dataArr, array('id' => $id))`.
5. `query::updateData()` builds an `UPDATE users SET ... WHERE id = ...` statement and executes it.
6. The page then redirects to `index.php`.

### 4. Delete a user

1. From the user list, the user follows a Delete link of the form `index.php?id=<id>&type=delete`. This is a GET request.
2. `index.php` checks that `$_GET['type']` equals `delete` and that `$_GET['id']` is set.
3. It cleans the `id` with `get_safe_str()` and calls `deleteData('users', array('id' => $id))`.
4. `query::deleteData()` builds a `DELETE FROM users WHERE id = ...` statement and executes it.
5. After the delete call, `index.php` sends a `Location: index.php` header and exits, returning the browser to the user list.

## Observed limitations and technical-debt observations

The following observations are recorded as evidence only. They have not been fixed in this documentation task and should be reviewed during later delta analysis.

- **Database credentials in source code.** `database.php` contains hard-coded connection values (`localhost`, `root`, an empty password, and `oops_crud`). This should be reviewed before any production use.
- **SQL construction and input handling.** `query.php` builds SQL by concatenating strings. `get_safe_str()` applies `trim()` and `mysqli_real_escape_string()`, but table names, field names, condition keys, ordering fields, and limit values are not parameterised. The current implementation appears to rely on escaping rather than prepared statements, and input validation appears minimal. This should be reviewed for injection safety and validation quality.
- **Output escaping.** `index.php` echoes user-supplied database values (`name`, `email`, `mobile`) directly into HTML without an output-escaping function such as `htmlspecialchars()`. This should be reviewed.
- **Delete via GET.** Deletion is initiated through a GET link (`index.php?id=...&type=delete`). Because the action changes server state, this should be reviewed; state-changing operations are typically expected to use POST.
- **CSRF protection.** The add/edit form in `manage-users.php` does not contain a CSRF token, and no CSRF handling is documented in the current code. CSRF protection is not documented as part of the current implementation.
- **Automated tests.** There is no automated test suite. `test.php` is a manual example that calls `getData('employee')` and prints the result; it is not an automated test suite.
- **Table-name inconsistency.** `README.md` documents `users` as the main table and `index.php`/`manage-users.php` use `users`, but `test.php` contains an uncommented call to `getData('employee')`. This inconsistency should be checked during later analysis.

## Boundary between current and future systems

- This document describes the current PHP/MySQL system.
- Java migration design is out of scope for this issue.
- Any Java architecture, framework choice, or migration plan must be handled later through delta-analysis and forward-engineering issues.
