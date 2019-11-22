package com.zero5nelsonm.lendr;

import com.zero5nelsonm.lendr.model.*;
import com.zero5nelsonm.lendr.service.ItemHistoryService;
import com.zero5nelsonm.lendr.service.ItemService;
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

    @Autowired
    ItemService itemService;

    @Autowired
    ItemHistoryService itemHistoryService;

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
        User u1 = new User("admin_TEST",
                "admin",
                "admin@testing.com",
                admins);
        userService.save(u1);

        // data, user
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(),
                r3));
        datas.add(new UserRoles(new User(),
                r2));
        User u2 = new User("userdata_TEST",
                "userdata",
                "userdata@testing.com",
                datas);
        u2.getUseritems()
                .add(new Item(u2, "Table Saw" + "_TEST",
                        "Dewalt Table Saw",
                        "Herbert",
                        "November 11, 2019",
                        ""));
        userService.save(u2);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u3 = new User("user_TEST",
                "user",
                "user@testing.com",
                users);
        u3.getUseritems()
                .add(new Item(u3, "Chop Saw" + "_TEST",
                        "Dewalt Chop Saw",
                        "Allen",
                        "November 21, 2019",
                        null));
        u3.getUseritems()
                .add(new Item(u3, "Drill" + "_TEST",
                        "Dewalt Drill",
                        "Kyle",
                        "November 21, 2019",
                        null));
        User user3 = userService.save(u3);

        Item u3Item0 = itemService.findItemByIdForUser(user3, user3.getUseritems().get(0).getItemid());
        itemHistoryService.save(new ItemHistory(u3Item0,
                "Bob",
                "April 15, 2019",
                null,
                "May 21, 2019"));

        Item u3Item1 = itemService.findItemByIdForUser(user3, user3.getUseritems().get(1).getItemid());
        itemHistoryService.save(new ItemHistory(u3Item1,
                "Terry",
                "April 19, 2019",
                null,
                "April 27, 2019"));
    }
}