package com.oopscrud;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class UserRepository {

    private static final RowMapper<User> MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"));

    private final JdbcClient jdbc;

    UserRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    List<User> findAll() {
        return jdbc.sql("SELECT id, name, email, mobile FROM users ORDER BY id DESC")
                .query(MAPPER)
                .list();
    }

    Optional<User> findById(long id) {
        return jdbc.sql("SELECT id, name, email, mobile FROM users WHERE id = ?")
                .param(id)
                .query(MAPPER)
                .optional();
    }

    void insert(User user) {
        jdbc.sql("INSERT INTO users (name, email, mobile) VALUES (?, ?, ?)")
                .params(user.name(), user.email(), user.mobile())
                .update();
    }

    boolean update(User user) {
        return jdbc.sql("UPDATE users SET name = ?, email = ?, mobile = ? WHERE id = ?")
                .params(user.name(), user.email(), user.mobile(), user.id())
                .update() > 0;
    }

    boolean delete(long id) {
        return jdbc.sql("DELETE FROM users WHERE id = ?")
                .param(id)
                .update() > 0;
    }
}
