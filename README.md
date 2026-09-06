# PHP Object-Oriented CRUD Application

A simple PHP and MySQL CRUD application built with object-oriented PHP. This beginner-friendly project manages users and demonstrates how to organize database connectivity and reusable query methods in separate classes.

## Features

- Add a user
- View all users
- Edit user details
- Delete a user
- Reusable database query methods
- Bootstrap-based user interface
- MySQL database connection using `mysqli`

## Technologies

- PHP 7+
- MySQL
- MySQLi
- HTML5
- Bootstrap 4
- Font Awesome

## Project Structure

| File | Purpose |
| --- | --- |
| `database.php` | Provides the reusable database connection class. |
| `query.php` | Extends the database class and contains generic select, insert, update, and delete methods. |
| `index.php` | Lists users and handles user deletion. |
| `manage-users.php` | Provides the add and edit user form and processes submissions. |
| `test.php` | Optional development file for testing query methods. |

## Database Setup

Create a MySQL database named `oops_crud`, then create the `users` table:

```sql
CREATE DATABASE oops_crud;

USE oops_crud;

CREATE TABLE users (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(150) NOT NULL,
	mobile VARCHAR(20) NOT NULL
);
```

The default connection settings in `database.php` are:

```text
Host: localhost
Username: root
Password: empty
Database: oops_crud
```

Update these values in `database.php` if your local MySQL configuration is different.

## Installation and Usage

1. Copy the project into your local PHP server directory, such as `htdocs` or `www`.
2. Start Apache and MySQL from XAMPP, WAMP, or another PHP development environment.
3. Create the database and `users` table using the SQL above.
4. Check the database credentials in `database.php`.
5. Open the project in a browser:

   ```text
   http://localhost/oops_crud/
   ```

6. Use **Add Users** to create records, then use **Edit** or **Delete** from the user list.

## How It Works

`query` extends `database`, so the application can reuse one connection method while keeping CRUD operations in a separate class. The main pages pass table names, fields, values, and conditions to methods such as `getData()`, `insertData()`, `updateData()`, and `deleteData()`.

This project is intended for learning PHP OOP, MySQL CRUD operations, class inheritance, form handling, and basic project organization. For production use, add prepared statements, stronger validation, escaped HTML output, CSRF protection, authentication, and environment-based database credentials.

## License

This project is available for educational and practice purposes.
