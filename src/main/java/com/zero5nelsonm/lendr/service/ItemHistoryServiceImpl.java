package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.repository.ItemHistoryRepository;
import com.zero5nelsonm.lendr.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Loggable
@Service(value = "itemHistoryService")
public class ItemHistoryServiceImpl implements ItemHistoryService {

    @Autowired
    ItemHistoryRepository itemHistoryRepository;

    @Autowired
    ItemRepository itemRepository;

    private void CheckItemByIdForUserByIdExists(long itemid, long itemhistoryid, long userid) {
        if (itemRepository.checkItemByIdForUserByIdExists(itemid, userid).getCount() < 1) {
            throw new ResourceNotFoundException("ItemHistory id " + itemhistoryid + " not found!");
        }
    }

    @Override
    public ItemHistory findItemHistoryByIdForUser(User user, long itemhistoryid) {

        ItemHistory itemHistory = itemHistoryRepository.findById(itemhistoryid).orElseThrow(() ->
                new ResourceNotFoundException("ItemHistory id " + itemhistoryid + " not found!"));

        CheckItemByIdForUserByIdExists(itemHistory.getItem().getItemid(), itemhistoryid,  user.getUserid());

        return itemHistory;
    }

    @Override
    public ItemHistory save(ItemHistory itemHistory) {
        return itemHistoryRepository.save(itemHistory);
    }
}
