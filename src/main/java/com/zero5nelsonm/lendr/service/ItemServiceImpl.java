package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.repository.ItemHistoryRepository;
import com.zero5nelsonm.lendr.repository.ItemRepository;
import com.zero5nelsonm.lendr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Loggable
@Service(value = "itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemHistoryRepository itemHistoryRepository;

    @Override
    public List<Item> findAllByUsername(String username) {

        return itemRepository.findAllByUser_Username(username.toLowerCase());
    }

    @Override
    public Item findItemByIdForUser(User user, long itemid) {

        if (itemRepository.checkItemByIdForUserByIdExists(itemid, user.getUserid()).getCount() < 1) {
            throw new ResourceNotFoundException("Item ID " + itemid + " does not exist for that user!");
        }

        return itemRepository.findItemByItemid(itemid);
    }

    @Override
    public Item save(Item item) {

        if (itemRepository.checkItemByNameForUserByIdExists(item.getItemname(), item.getUser().getUserid()).getCount() > 0) {
            throw new ResourceFoundException(item.getItemname() + " already exists!");
        }

        return itemRepository.save(item);
    }
}
