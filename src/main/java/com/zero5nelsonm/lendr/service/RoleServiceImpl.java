package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Role;
import com.zero5nelsonm.lendr.repository.RoleRepository;
import com.zero5nelsonm.lendr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Loggable
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuditing userAuditing;

    @Override
    public Role findRoleById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
    }

    @Override
    public Role findByName(String name) {
        Role rr = roleRepository.findByNameIgnoreCase(name);

        if (rr != null) {
            return rr;
        } else {
            throw new ResourceNotFoundException(name);
        }
    }

    @Transactional
    @Override
    public Role save(Role role) {
        Role newRole = new Role();
        newRole.setName(role.getName());

        if (role.getUserroles()
                .size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through Role. See endpoint POST: users/user/{userid}/role/{roleid}");
        }

        return roleRepository.save(role);
    }
}
