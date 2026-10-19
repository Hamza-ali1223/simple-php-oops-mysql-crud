-- Logical schema preserved from the legacy PHP/MySQL application (README.md).
-- SQLite types: TEXT replaces VARCHAR; INTEGER PRIMARY KEY AUTOINCREMENT replaces
-- INT UNSIGNED AUTO_INCREMENT PRIMARY KEY.
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    mobile TEXT NOT NULL
);
