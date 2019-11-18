package com.zero5nelsonm.lendr;

import com.zero5nelsonm.lendr.model.Role;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.model.UserRoles;
import com.zero5nelsonm.lendr.service.RoleService;
import com.zero5nelsonm.lendr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;


    @Override
    public void run(String[] args) throws Exception {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);

        // admin, data, user
        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(),
                r1));
        admins.add(new UserRoles(new User(),
                r2));
        admins.add(new UserRoles(new User(),
                r3));
        User u1 = new User(System.getenv("LENDR_ADMIN_USERNAME"),
                System.getenv("LENDR_ADMIN_PASSWORD"),
                System.getenv("LENDR_ADMIN_EMAIL"),
                admins);
        userService.save(u1);

        // data, user
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(),
                r3));
        datas.add(new UserRoles(new User(),
                r2));
        User u2 = new User(System.getenv("LENDR_DATA_USERNAME"),
                System.getenv("LENDR_DATA_PASSWORD"),
                System.getenv("LENDR_DATA_EMAIL"),
                datas);
        userService.save(u2);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u3 = new User(System.getenv("LENDR_USER_USERNAME"),
                System.getenv("LENDR_USER_PASSWORD"),
                System.getenv("LENDR_USER_EMAIL"),
                users);
        userService.save(u3);
    }
}