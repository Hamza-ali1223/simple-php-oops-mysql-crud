package com.oopscrud;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserForm {

    private Long id;

    @NotBlank
    private String name = "";

    @NotBlank
    @Email
    private String email = "";

    // Modest rule for now: required, non-blank. A stricter format is a later team decision.
    @NotBlank
    private String mobile = "";

    public static UserForm from(User user) {
        UserForm form = new UserForm();
        form.id = user.id();
        form.name = user.name();
        form.email = user.email();
        form.mobile = user.mobile();
        return form;
    }

    User toUser() {
        return new User(id, name, email, mobile);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
