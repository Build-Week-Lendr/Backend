package com.zero5nelsonm.lendr.repository;

import com.zero5nelsonm.lendr.model.Item;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

}
