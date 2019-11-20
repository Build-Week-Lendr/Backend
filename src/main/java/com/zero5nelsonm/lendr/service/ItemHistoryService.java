package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;

public interface ItemHistoryService {

    ItemHistory findItemHistoryByIdForUser(User user, long itemhistoryid);

    ItemHistory save(ItemHistory itemHistory);

    ItemHistory update(ItemHistory updateItemHistory, long itemhistoryid, User user);
}
