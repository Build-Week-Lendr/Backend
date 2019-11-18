package com.zero5nelsonm.lendr.repository;

import com.zero5nelsonm.lendr.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
