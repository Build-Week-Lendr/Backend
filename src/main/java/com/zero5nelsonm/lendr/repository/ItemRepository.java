package com.zero5nelsonm.lendr.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.view.JustTheCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    List<Item> findAllByUser_Username(String username);

    @Query(value = "SELECT COUNT(*) as count FROM items WHERE userid = :userid AND itemname = :itemname",
            nativeQuery = true)
    JustTheCount checkItemByNameForUserByIdExists(String itemname, long userid);

    @Query(value = "SELECT COUNT(*) as count FROM items WHERE userid = :userid AND itemid = :itemid",
            nativeQuery = true)
    JustTheCount checkItemByIdForUserByIdExists(long itemid, long userid);

    @Query(value = "SELECT * FROM items WHERE itemid = :itemid", nativeQuery = true)
    Item findItemByItemid(long itemid);
}
