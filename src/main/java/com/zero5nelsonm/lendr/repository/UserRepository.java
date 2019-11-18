package com.zero5nelsonm.lendr.repository;

import com.zero5nelsonm.lendr.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
