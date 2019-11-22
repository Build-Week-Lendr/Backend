package com.zero5nelsonm.lendr.service;

import com.zero5nelsonm.lendr.LendrApplication;
import com.zero5nelsonm.lendr.exceptions.ResourceFoundException;
import com.zero5nelsonm.lendr.exceptions.ResourceNotFoundException;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LendrApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemServiceImplUnitTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemHistoryService itemHistoryService;

    private User user = new User();
    private User userdata = new User();
    private String item6Name = "Table Saw" + "_TEST";
    private String item8Name = "Chop Saw" + "_TEST";
    private String newItemName = "NewItem" + "_TEST";


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        user = userService.findByName("user_TEST");
        userdata = userService.findByName("userdata_TEST");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void A_findAllByUsername() {
        assertEquals(2, itemService.findAllByUsername(user.getUsername()).size());
    }

    @Test
    public void B_findItemByIdForUser_ItemReturnsCorrectly() {
        assertEquals(item8Name, itemService.findItemByIdForUser(user, 8).getItemname());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void BB_findItemByIdForUser_ItemIdNotBelongingToUser() {
        assertEquals(item6Name, itemService.findItemByIdForUser(userdata, 6).getItemname());
        itemService.findItemByIdForUser(user, 6);
    }

    @Test
    public void C_itemHasBeenReturned() {
        Item item = itemService.findItemByIdForUser(user, 8);

        ItemHistory newItemHistory = new ItemHistory(
                item,
                item.getLentto(),
                item.getLentdate(),
                item.getLendnotes(),
                null);

        assertEquals("Bob", itemHistoryService.findItemHistoryByIdForUser(user, 10).getLentto());
        assertEquals("Allen", item.getLentto());

        itemService.itemHasBeenReturned(item, newItemHistory);

        assertNull(itemService.findItemByIdForUser(user, 8).getLentto());
        assertEquals("Allen", itemHistoryService.findItemHistoryByIdForUser(user, 12).getLentto());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void D_save_ItemNameRequiredSoWillNotSave() {
        Item item = new Item(user,
                null,
                null,
                null, null,
                null);

        itemService.save(item);
    }

    @Test(expected = ResourceFoundException.class)
    public void DD_save_ItemNameAlreadyExists() {
        Item item = new Item(user,
                item8Name,
                null,
                null, null,
                null);

        itemService.save(item);
    }

    @Test
    public void DDD_save_ItemSavesProperly() {
        Item item = new Item(user,
                newItemName,
                null,
                null, null,
                null);

        Item savedItem = itemService.save(item);
        assertEquals(newItemName, itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());
    }

    @Test
    public void E_update_ItemUpdatesProperly() {
        Item item = new Item(user,
                newItemName + "1",
                null,
                null,
                null,
                null);

        Item savedItem = itemService.save(item);
        assertEquals(newItemName + "1", itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());
        assertNull(itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemdescription());
        assertNull(itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentto());
        assertNull(itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentdate());
        assertNull(itemService.findItemByIdForUser(user, savedItem.getItemid()).getLendnotes());

        String updateItemname = "test name";
        String updateDescription = "test description";
        String updateLentto = "test person";
        String updateLentDate = "test date";
        String updateLendNotes = "test notes";
        item.setItemname(updateItemname);
        item.setItemdescription(updateDescription);
        item.setLentto(updateLentto);
        item.setLentdate(updateLentDate);
        item.setLendnotes(updateLendNotes);

        itemService.update(item, savedItem.getItemid(), user);

        assertEquals(updateItemname, itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());
        assertEquals(updateDescription, itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemdescription());
        assertEquals(updateLentto, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentto());
        assertEquals(updateLentDate, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentdate());
        assertEquals(updateLendNotes, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLendnotes());

        item.setItemname(null);
        item.setItemdescription(null);
        item.setLentto(null);
        item.setLentdate(null);
        item.setLendnotes(null);

        itemService.update(item, savedItem.getItemid(), user);
        assertEquals(updateItemname, itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());
        assertEquals(updateDescription, itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemdescription());
        assertEquals(updateLentto, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentto());
        assertEquals(updateLentDate, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLentdate());
        assertEquals(updateLendNotes, itemService.findItemByIdForUser(user, savedItem.getItemid()).getLendnotes());
    }

    @Test(expected = ResourceFoundException.class)
    public void EE_update_ItemNameAlreadyExists() {
        Item item = new Item(user,
                newItemName + "2",
                null,
                null, null,
                null);

        Item savedItem = itemService.save(item);
        assertEquals(newItemName + "2", itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());

        item.setItemname(item8Name);

        itemService.update(item, savedItem.getItemid(), user);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void F_delete_ItemDeletesProperly() {
        Item item = new Item(user,
                newItemName + "3",
                null,
                null, null,
                null);

        Item savedItem = itemService.save(item);
        assertEquals(newItemName + "3", itemService.findItemByIdForUser(user, savedItem.getItemid()).getItemname());

        itemService.delete(user, savedItem.getItemid());
        itemService.delete(user, savedItem.getItemid());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void FF_delete_ItemFromAnotherUserCannotBeDeleted() {
        itemService.delete(user, 6);
    }
}