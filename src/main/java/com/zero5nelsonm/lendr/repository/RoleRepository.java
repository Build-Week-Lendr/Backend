package com.zero5nelsonm.lendr.repository;

import com.zero5nelsonm.lendr.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByNameIgnoreCase(String name);
}
