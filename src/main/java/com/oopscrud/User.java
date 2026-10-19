package com.oopscrud;

public record User(Long id, String name, String email, String mobile) {

    public User(String name, String email, String mobile) {
        this(null, name, email, mobile);
    }
}
