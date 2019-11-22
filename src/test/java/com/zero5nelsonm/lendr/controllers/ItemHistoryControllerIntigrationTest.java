package com.zero5nelsonm.lendr.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemHistoryControllerIntigrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @WithUserDetails("user_TEST")
    @Test
    public void A_getItemhistoriesForItemByItemId() throws Exception {
        this.mockMvc.perform(get("/itemhistory/item/{itemid}", 9))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"itemhistoryid\":11,\"lentto\":\"Bob\",\"lentdate\":\"April 15, 2019\",\"lendnotes\":null,\"datereturned\":\"May 21, 2019\"}]"));
    }

    @WithUserDetails("userdata_TEST")
    @Test
    public void AA_getItemhistoriesForItemByItemId_ForItemNotBelongingToUser() throws Exception {
        this.mockMvc.perform(get("/itemhistory/item/{itemid}", 9))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithUserDetails("user_TEST")
    @Test
    public void B_getCurrentUserItemHistoryById() throws Exception {
        this.mockMvc.perform(get("/itemhistory/{itemhistoryid}", 11))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"itemhistoryid\":11,\"lentto\":\"Bob\",\"lentdate\":\"April 15, 2019\",\"lendnotes\":null,\"datereturned\":\"May 21, 2019\"}"));
    }

    @WithUserDetails("userdata_TEST")
    @Test
    public void BB_getCurrentUserItemHistoryById_ForItemHistoryNotBelongingToUser() throws Exception {
        this.mockMvc.perform(get("/itemhistory/{itemhistoryid}", 11))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithUserDetails("user_TEST")
    @Test
    public void C_addNewItemHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/itemhistory/item/{itemid}", 9)
                        .content("{\"lentto\":\"Gary\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .header()
                        .exists("location"));

    }

    @WithUserDetails("userdata_TEST")
    @Test
    public void CC_addNewItemHistory_ForItemNotBelongingToUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/itemhistory/item/{itemid}", 9)
                        .content("{\"lentto\":\"Gary\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @WithUserDetails("user_TEST")
    @Test
    public void D_updateItemHistoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/itemhistory/{itemhistoryid}", 11)
                        .content("{\"lentto\":\"Amber\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("userdata_TEST")
    @Test
    public void DD_updateItemHistoryById_ForItemHistoryNotBelongingToUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/itemhistory/{itemhistoryid}", 11)
                        .content("{\"lentto\":\"Amber\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithUserDetails("user_TEST")
    @Test
    public void E_deleteItemHistoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/itemhistory/{itemhistoryid}", 11))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @WithUserDetails("user_TEST")
    @Test
    public void EE_deleteItemHistoryById_ForItemHistoryNotBelongingToUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/itemhistory/{itemhistoryid}", 8))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}