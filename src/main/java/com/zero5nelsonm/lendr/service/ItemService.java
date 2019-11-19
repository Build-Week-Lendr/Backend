package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;

import java.util.List;

public interface ItemService {

    List<Item> findAllByUsername(String username);

    Item findItemByIdForUser(User user, long itemid);

    Item save(Item item);

    Item update(Item updateItem, long itemid, User user);

    void delete(User user, long itemid);

    Item itemHasBeenReturned(Item item, ItemHistory newItemHistory);
}
