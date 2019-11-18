package com.zero5nelsonm.lendr.repository;

import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.view.JustTheCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
