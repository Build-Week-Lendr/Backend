package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.Role;

public interface RoleService {

    Role findRoleById(long id);

    Role findByName(String name);

    Role save(Role role);
}