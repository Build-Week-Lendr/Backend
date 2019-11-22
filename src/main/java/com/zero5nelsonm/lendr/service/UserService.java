package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<User> findAll(Pageable pageable);

    User findUserById(long id);

    User findByName(String name);

    void delete(long id);

    User save(User user);

    User update(User user, long id, boolean isAdmin);
}