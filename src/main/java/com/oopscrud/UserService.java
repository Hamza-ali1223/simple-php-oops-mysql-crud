package com.oopscrud;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
class UserService {

    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    List<User> findAll() {
        return repository.findAll();
    }

    User getExisting(long id) {
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    void create(User user) {
        repository.insert(user);
    }

    void update(User user) {
        if (!repository.update(user)) {
            throw new UserNotFoundException();
        }
    }

    void delete(long id) {
        if (!repository.delete(id)) {
            throw new UserNotFoundException();
        }
    }
}
