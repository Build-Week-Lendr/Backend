package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.*;
import com.zero5nelsonm.lendr.repository.RoleRepository;
import com.zero5nelsonm.lendr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Loggable
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserAuditing userAuditing;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<User> findAll(Pageable pageable)
    {
        List<User> list = new ArrayList<>();
        userRepository.findAll(pageable)
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    public User findUserById(long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
        userRepository.deleteById(id);
    }

    @Override
    public User findByName(String name) {
        User uu = userRepository.findByUsername(name.toLowerCase());
        if (uu == null) {
            throw new ResourceNotFoundException("User name " + name + " not found!");
        }
        return uu;
    }

    @Transactional
    @Override
    public User save(User user) {

        if (userRepository.findByUsername(user.getUsername().toLowerCase()) != null) {
            throw new ResourceFoundException(user.getUsername() + " is already taken!");
        }

        if (userRepository.findByEmail(user.getEmail().toLowerCase()) != null) {
            throw new ResourceFoundException(user.getEmail() + " is already associated with a username");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername()
                .toLowerCase());
        newUser.setPasswordNotEncrypt(user.getPassword()); // Password is already hashed using BCrypt
        newUser.setEmail(user.getEmail()
                .toLowerCase());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserroles()) {
            long id = ur.getRole()
                    .getRoleid();
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
            newRoles.add(new UserRoles(newUser,
                    role));
        }
        newUser.setUserroles(newRoles);
        newUser.setEmail(user.getEmail());

        for (Item i : user.getUseritems()) {
            newUser.getUseritems()
                    .add(new Item(
                            newUser,
                            i.getItemname(),
                            i.getItemdescription(),
                            i.getLentto(),
                            i.getLentdate(),
                            i.getLendnotes()));
        }

        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public User update(User user, long id, boolean isAdmin) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        User authenticatedUser = userRepository.findByUsername(authentication.getName());

        if (id == authenticatedUser.getUserid() || isAdmin) {
            User currentUser = findUserById(id);

            if (user.getUsername() != null) {
                currentUser.setUsername(user.getUsername()
                        .toLowerCase());
            }

            if (user.getPassword() != null) {
                currentUser.setPasswordNotEncrypt(user.getPassword());
            }

            if (user.getEmail() != null) {
                currentUser.setEmail(user.getEmail()
                        .toLowerCase());
            }

            if (user.getUserroles()
                    .size() > 0) {
                throw new ResourceFoundException("User Roles are not updated through User. See endpoint POST: users/user/{userid}/role/{roleid}");
            }

            return userRepository.save(currentUser);
        } else {
            throw new ResourceNotFoundException(id + " Not current user");
        }
    }
}
