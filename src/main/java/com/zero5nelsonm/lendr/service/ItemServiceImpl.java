package com.zero5nelsonm.lendr.service;

import com.sun.istack.Nullable;
import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.ItemHistory;
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

    private void CheckItemByIdForUserByIdExists(long itemid, long userid) {
        if (itemRepository.checkItemByIdForUserByIdExists(itemid, userid).getCount() < 1) {
            throw new ResourceNotFoundException("Itemid " + itemid + " does not exist for that user!");
        }
    }

    private void CheckItemByNameForUserByIdExists(Item item, long userid) {
        if (itemRepository.checkItemByNameForUserByIdExists(item.getItemname(), userid).getCount() > 0) {
            throw new ResourceFoundException("Itemname " + item.getItemname() + " already exists!");
        }
    }

    @Override
    public List<Item> findAllByUsername(String username) {

        return itemRepository.findAllByUser_Username(username.toLowerCase());
    }

    @Override
    public Item findItemByIdForUser(User user, long itemid) {

        CheckItemByIdForUserByIdExists(itemid, user.getUserid());

        return itemRepository.findItemByItemid(itemid);
    }

    @Override
    public Item save(Item item) {

        CheckItemByNameForUserByIdExists(item, item.getUser().getUserid());

        return itemRepository.save(item);
    }

    @Override
    public Item update(Item updateItem, long itemid, User user) {

        CheckItemByIdForUserByIdExists(itemid, user.getUserid());

        Item existingItem = itemRepository.findItemByItemid(itemid);

        if (updateItem.getItemname() != null) {
            CheckItemByNameForUserByIdExists(updateItem, user.getUserid());
            existingItem.setItemname(updateItem.getItemname());
        }

        if (updateItem.getItemdescription() != null) {
            existingItem.setItemdescription(updateItem.getItemdescription());
        }

        if (updateItem.getLentto() != null) {
            existingItem.setLentto(updateItem.getLentto());
        }

        if (updateItem.getLentdate() != null) {
            existingItem.setLentdate(updateItem.getLentdate());
        }

        if (updateItem.getLendnotes() != null) {
            existingItem.setLendnotes(updateItem.getLendnotes());
        }

        return itemRepository.save(existingItem);
    }

    @Override
    public void delete(User user, long itemid) {

        CheckItemByIdForUserByIdExists(itemid, user.getUserid());

        itemRepository.deleteById(itemid);
    }

    @Override
    public Item itemHasBeenReturned(Item item, ItemHistory newItemHistory) {
        item.setLentto(null);
        item.setLentdate(null);
        item.setLendnotes(null);
        itemRepository.save(item);
        itemHistoryRepository.save(newItemHistory);

        return itemRepository.findItemByItemid(item.getItemid());
    }
}
