package com.oopscrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserCrudIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    JdbcClient jdbc;

    private long insertUser(String name, String email, String mobile) {
        jdbc.sql("INSERT INTO users (name, email, mobile) VALUES (?, ?, ?)")
                .params(name, email, mobile)
                .update();
        return jdbc.sql("SELECT id FROM users WHERE email = ?")
                .param(email)
                .query(Long.class)
                .single();
    }

    private int countByEmail(String email) {
        return jdbc.sql("SELECT count(*) FROM users WHERE email = ?")
                .param(email)
                .query(Integer.class)
                .single();
    }

    @Test
    void listShowsEmptyStateWhenNoUsersExist() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No users found")));
    }

    @Test
    void createValidUserRedirectsAndPersists() throws Exception {
        mvc.perform(post("/users")
                        .param("name", "Asiya Khan")
                        .param("email", "asiya@example.com")
                        .param("mobile", "03001234567"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        assertThat(countByEmail("asiya@example.com")).isEqualTo(1);
    }

    @Test
    void createInvalidUserRedisplaysFormWithErrorsAndPersistsNothing() throws Exception {
        mvc.perform(post("/users")
                        .param("name", "")
                        .param("email", "not-an-email")
                        .param("mobile", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("field-error")))
                .andExpect(content().string(containsString("value=\"not-an-email\"")));

        assertThat(jdbc.sql("SELECT count(*) FROM users").query(Integer.class).single()).isZero();
    }

    @Test
    void listShowsExistingUsers() throws Exception {
        insertUser("Izaan", "izaan@example.com", "03001112222");
        insertUser("Rafay", "rafay@example.com", "03003334444");

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Izaan")))
                .andExpect(content().string(containsString("Rafay")));
    }

    @Test
    void createFormRendersBlank() throws Exception {
        mvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add user")));
    }

    @Test
    void editFormRendersExistingUser() throws Exception {
        long id = insertUser("Editable", "editable@example.com", "03001230000");

        mvc.perform(get("/users/" + id + "/edit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("value=\"Editable\"")));
    }

    @Test
    void editFormWithUnknownIdReturnsNotFound() throws Exception {
        mvc.perform(get("/users/999/edit"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }

    @Test
    void updateValidUserPersistsChanges() throws Exception {
        long id = insertUser("Old Name", "old@example.com", "03005556666");

        mvc.perform(post("/users/" + id)
                        .param("id", String.valueOf(id))
                        .param("name", "New Name")
                        .param("email", "new@example.com")
                        .param("mobile", "03009998888"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        String name = jdbc.sql("SELECT name FROM users WHERE id = ?")
                .param(id).query(String.class).single();
        assertThat(name).isEqualTo("New Name");
        assertThat(countByEmail("old@example.com")).isZero();
        assertThat(countByEmail("new@example.com")).isEqualTo(1);
    }

    @Test
    void updateInvalidUserRedisplaysFormAndKeepsOldData() throws Exception {
        long id = insertUser("Original Name", "original@example.com", "03001110000");

        mvc.perform(post("/users/" + id)
                        .param("id", String.valueOf(id))
                        .param("name", "")
                        .param("email", "broken-email")
                        .param("mobile", "03001110000"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("field-error")));

        assertThat(countByEmail("original@example.com")).isEqualTo(1);
        assertThat(countByEmail("broken-email")).isZero();
    }

    @Test
    void deleteRemovesExistingUser() throws Exception {
        long id = insertUser("Deletable", "delete-me@example.com", "03007776666");

        mvc.perform(post("/users/" + id + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        assertThat(countByEmail("delete-me@example.com")).isZero();
    }

    @Test
    void deleteUnknownIdReturnsNotFound() throws Exception {
        mvc.perform(post("/users/999/delete"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }
}
