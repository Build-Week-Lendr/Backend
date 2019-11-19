package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.User;

import java.util.List;

public interface ItemService {

    List<Item> findAllByUsername(String username);

    Item findItemByIdForUser(User user, long itemid);

    Item save(Item item);
}
