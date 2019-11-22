package com.zero5nelsonm.lendr.model;

import com.zero5nelsonm.lendr.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ItemTest {

    @MockBean
    private UserService userService;

    private User controlUser = new User();
    private String controlUserUsername = "user_TEST";
    
    private User testUser = new User();
    private String testUserUsername = "userdata_TEST";

    private Item controlItem = new Item();
    private long itemid = 40;
    private String controlItemName = "Control Name";
    private String controlItemDescription = "Control Description";
    private String controlItemLentTo = "Control Lent To";
    private String controlItemLentDate = "Control Date Lent";
    private String controlItemLendNote = "Control Lend Note";

    private List<ItemHistory> controlItemHistoryList = new ArrayList<>();
    private List<ItemHistory> testItemHistoryList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        controlUser = userService.findByName(controlUserUsername);
        testUser = userService.findByName(testUserUsername);

        controlItem = new Item(controlUser,
                controlItemName,
                controlItemDescription,
                controlItemLentTo,
                controlItemLentDate,
                controlItemLendNote);
        controlItem.setItemid(itemid);

        controlItemHistoryList.add(new ItemHistory(controlItem,
                "IH Lent To",
                "IH Lent Date",
                "IH Lend Notes",
                "IH Date Returned"));
        controlItemHistoryList.get(0).setItemhistoryid(50);
        controlItem.setItemhistories(controlItemHistoryList);

        testItemHistoryList.add(new ItemHistory(controlItem,
                "IH Lent To TEST",
                "IH Lent Date TEST",
                "IH Lend Notes TEST",
                "IH Date Returned TEST"));
        controlItemHistoryList.get(0).setItemhistoryid(50);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getItemid() {
        assertEquals(itemid, controlItem.getItemid());
    }

    @Test
    public void setItemid() {
        long newItemid = 41;

        assertEquals(itemid, controlItem.getItemid());

        controlItem.setItemid(newItemid);
        assertEquals(newItemid, controlItem.getItemid());

        controlItem.setItemid(itemid);
    }

    @Test
    public void getItemname() {
        assertEquals(controlItemName, controlItem.getItemname());
    }

    @Test
    public void setItemname() {
        String newItemName = "Test Item Name";

        assertEquals(controlItemName, controlItem.getItemname());

        controlItem.setItemname(newItemName);
        assertEquals(newItemName, controlItem.getItemname());

        controlItem.setItemname(controlItemName);
    }

    @Test
    public void getItemdescription() {
        assertEquals(controlItemDescription, controlItem.getItemdescription());
    }

    @Test
    public void setItemdescription() {
        String newItemDescription = "Test Item Description";

        assertEquals(controlItemDescription, controlItem.getItemdescription());

        controlItem.setItemdescription(newItemDescription);
        assertEquals(newItemDescription, controlItem.getItemdescription());

        controlItem.setItemdescription(controlItemDescription);
    }

    @Test
    public void getLentto() {
        assertEquals(controlItemLentTo, controlItem.getLentto());
    }

    @Test
    public void setLentto() {
        String newItemLentTo = "Test Item Lent To";
        
        assertEquals(controlItemLentTo, controlItem.getLentto());
        
        controlItem.setLentto(newItemLentTo);
        assertEquals(newItemLentTo, controlItem.getLentto());
        
        controlItem.setLentto(controlItemLentTo);
    }

    @Test
    public void getLentdate() {
        assertEquals(controlItemLentDate, controlItem.getLentdate());
    }

    @Test
    public void setLentdate() {
        String newItemLentDate = "Test Item Lent Date";
        
        assertEquals(controlItemLentDate, controlItem.getLentdate());
        
        controlItem.setLentdate(newItemLentDate);
        assertEquals(newItemLentDate, controlItem.getLentdate());
        
        controlItem.setLentdate(controlItemLentDate);
    }

    @Test
    public void getLendnotes() {
        assertEquals(controlItemLendNote, controlItem.getLendnotes());
    }

    @Test
    public void setLendnotes() {
        String newItemLendNote = "Test Item Lend Note";
        
        assertEquals(controlItemLendNote, controlItem.getLendnotes());
        
        controlItem.setLendnotes(newItemLendNote);
        assertEquals(newItemLendNote, controlItem.getLendnotes());
        
        controlItem.setLendnotes(controlItemLendNote);
    }

    @Test
    public void getUser() {
        assertEquals(controlUser, controlItem.getUser());
    }

    @Test
    public void setUser() {
        assertEquals(controlUser, controlItem.getUser());
        
        controlItem.setUser(testUser);
        assertEquals(testUser, controlItem.getUser());
        
        controlItem.setUser(controlUser);
    }

    @Test
    public void getItemhistories() {
        assertEquals(controlItemHistoryList, controlItem.getItemhistories());
    }

    @Test
    public void setItemhistories() {
        assertEquals(controlItemHistoryList, controlItem.getItemhistories());

        controlItem.setItemhistories(testItemHistoryList);
        assertEquals(testItemHistoryList, controlItem.getItemhistories());

        controlItem.setItemhistories(controlItemHistoryList);
    }
}