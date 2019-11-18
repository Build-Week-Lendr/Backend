package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAllByUsername(String username);

    Item save(Item item);
}
