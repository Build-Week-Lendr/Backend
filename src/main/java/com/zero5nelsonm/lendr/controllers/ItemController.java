package com.zero5nelsonm.lendr.controllers;

import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.*;
import com.zero5nelsonm.lendr.service.ItemService;
import com.zero5nelsonm.lendr.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/items")
@Loggable
@Api(tags = {"ItemEndpoints"})
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    /**
     * GET
     * http://localhost:2019/items/items
     * http://localhost:2019/items/items?returnitemhistory=true
     * */
    @ApiOperation(
            value = "Returns all Items for an authenticated user",
            response = Item.class,
            responseContainer = "List")
    @ApiParam(
            name = "returnitemhistory",
            defaultValue = "false",
            type = "boolean",
            required = false,
            value = "Will return Items with their ItemHistory if set to true," +
                    "otherwise itemhistories field is returned as an empty List",
            example = "BASEURL/items/items?returnitemhistory=true")
    @GetMapping(value = "/items", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserItems(HttpServletRequest request,
                                                 Authentication authentication,
                                                 @RequestParam(defaultValue = "false") boolean returnitemhistory) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        List<Item> itemList = itemService.findAllByUsername(u.getUsername());

        List<ItemNoHistory> itemNoHistories = new ArrayList<>();
        if (!returnitemhistory) {
            for (Item i : itemList) {
                itemNoHistories.add(
                        new ItemNoHistory(
                                i.getItemid(),
                                i.getItemname(),
                                i.getItemdescription(),
                                i.getLentto(),
                                i.getLentdate(),
                                i.getLendnotes())
                );
            }
            return new ResponseEntity<>(itemNoHistories, HttpStatus.OK);
        }
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/items/item/{itemid}
     * @param itemid : long
     * */
    @ApiOperation(
            value = "Returns an Item based off of itemid",
            response = Item.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Item Found",
                            response = Item.class),
                    @ApiResponse(
                            code = 404,
                            message = "Item id {itemid} not found!",
                            response = ErrorDetail.class)
            })
    @ApiParam(
            name = "beingreturned",
            defaultValue = "false",
            type = "boolean",
            required = false,
            value = "If set to true, the Item will have its field's [lentto, lentdate, lendnotes] placed " +
                    "into a new ItemHistory and will be reset to null. The reset Item with it's new history " +
                    "will be returned.",
            example = "BASEURL/items/item/8?beingreturned=true")
    @GetMapping(value = "/item/{itemid}", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserItem(HttpServletRequest request,
                                                Authentication authentication,
                                                @PathVariable long itemid,
                                                @RequestParam(defaultValue = "false") boolean beingreturned) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        Item item = itemService.findItemByIdForUser(u, itemid);

        ItemHistory newItemHistory = new ItemHistory();
        if (beingreturned) {
            newItemHistory.setItem(item);
            newItemHistory.setLentto(item.getLentto());
            newItemHistory.setLentdate(item.getLentdate());
            newItemHistory.setLendnotes(item.getLendnotes());

            String pattern = "MM-dd-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            newItemHistory.setDatereturned(date);

            item = itemService.itemHasBeenReturned(item, newItemHistory);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * POST
     * http://localhost:2019/items/item
     * @param newItem : Item
     * */
    @ApiOperation(
            value = "Adds a new Item for the authenticated user",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "Item Created",
                            response = Void.class,
                            responseHeaders = @ResponseHeader(
                                    name = "Location",
                                    description = "Returns itemid for newly created item in the header",
                                    response = Void.class
                            )),
                    @ApiResponse(
                            code = 400,
                            message = "Item name {itemname} already exists!",
                            response = ErrorDetail.class)
            })
    @PostMapping(value = "/item", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewItem(HttpServletRequest request,
                                        Authentication authentication,
                                        @Valid @RequestBody Item newItem) throws URISyntaxException {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        newItem.setUser(u);

        Item createdItem = itemService.save(newItem);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(request.getServerName() + ":" + request.getLocalPort() + "/items/item/{itemid}")
                .buildAndExpand(createdItem.getItemid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    /**
     * PUT
     * http://localhost:2019/items/item
     * @param updateItem : Item
     * @param itemid : long
     * */
    @ApiOperation(
            value = "Adds a new item for the authenticated user",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Item Updated",
                            response = Void.class),
                    @ApiResponse(
                            code = 400,
                            message = "Item name {itemname} already exists!",
                            response = ErrorDetail.class),
                    @ApiResponse(
                            code = 400,
                            message = "Item id {itemid} not found!",
                            response = ErrorDetail.class)
            })
    @PutMapping(value = "/item/{itemid}", consumes = {"application/json"})
    public ResponseEntity<?> updateItem(HttpServletRequest request,
                                        Authentication authentication,
                                        @Valid @RequestBody Item updateItem,
                                        @PathVariable long itemid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        itemService.update(updateItem, itemid, u);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE
     * http://localhost:2019/items/item
     * @param itemid : long
     * */
    @ApiOperation(
            value = "Adds a new item for the authenticated user",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Item Deleted",
                            response = Void.class),
                    @ApiResponse(
                            code = 400,
                            message = "Item id {itemid} not found!",
                            response = ErrorDetail.class)
            })
    @DeleteMapping(value = "/item/{itemid}")
    public ResponseEntity<?> deleteItemById(HttpServletRequest request,
                                            Authentication authentication,
                                            @PathVariable long itemid) {

        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        itemService.delete(u, itemid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
