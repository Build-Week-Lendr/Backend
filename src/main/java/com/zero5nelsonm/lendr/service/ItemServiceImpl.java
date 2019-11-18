package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.repository.ItemRepository;
import com.zero5nelsonm.lendr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Loggable
@Service(value = "itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Item> findAllByUsername(String username) {

        return itemRepository.findAllByUser_Username(username.toLowerCase());
    }

    @Override
    public Item save(Item item) {

        if (itemRepository.checkItemNameForUserExists(item.getUser().getUserid(), item.getItemname()).getCount() > 0) {
            throw new ResourceFoundException(item.getItemname() + " already exists!");
        }

        return itemRepository.save(item);
    }
}
