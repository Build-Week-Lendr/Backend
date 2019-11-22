package com.zero5nelsonm.lendr.controllers;

import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.ErrorDetail;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.service.ItemHistoryService;
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

@RestController
@RequestMapping("/itemhistory")
@Loggable
@Api(tags = {"ItemHistoryEndpoints"})
public class ItemHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ItemHistoryController.class);

    @Autowired
    ItemService itemService;

    @Autowired
    ItemHistoryService itemHistoryService;

    @Autowired
    UserService userService;

    /**
     * GET
     * http://localhost:2019/itemhistory/item/{itemid}
     * @param itemid : long
     * */
    @ApiOperation(
            value = "Returns all ItemHistory for an Item based off of itemid",
            response = ItemHistory.class,
            responseContainer = "List")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "ItemHistory Found",
                            response = ItemHistory.class,
                            responseContainer = "List"),
                    @ApiResponse(
                            code = 404,
                            message = "Item id {itemid} not found!",
                            response = ErrorDetail.class)
            })
    @GetMapping(value = "/item/{itemid}", produces = {"application/json"})
    public ResponseEntity<?> getItemhistoriesForItemByItemId(HttpServletRequest request,
                                                         Authentication authentication,
                                                         @PathVariable long itemid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        Item item = itemService.findItemByIdForUser(u, itemid);

        return new ResponseEntity<>(item.getItemhistories(), HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/itemhistory/{itemhistoryid}
     * @param itemhistoryid : long
     * */
    @ApiOperation(
            value = "Returns an ItemHistory based off of itemhistoryid",
            response = ItemHistory.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "ItemHistory Found",
                            response = ItemHistory.class),
                    @ApiResponse(
                            code = 404,
                            message = "ItemHistory id {itemhistoryid} not found!",
                            response = ErrorDetail.class)
            })
    @GetMapping(value = "/{itemhistoryid}", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserItemHistoryById(HttpServletRequest request,
                                                       Authentication authentication,
                                                       @PathVariable long itemhistoryid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        ItemHistory itemHistory = itemHistoryService.findItemHistoryByIdForUser(u, itemhistoryid);

        return new ResponseEntity<>(itemHistory, HttpStatus.OK);
    }

    /**
     * POST
     * http://localhost:2019/itemhistory/item/{itemid}
     * @param itemid : long
     * @param newItemHistory : ItemHistory
     * */
    @ApiOperation(
            value = "Adds a new ItemHistory based off of itemid",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "ItemHistory Created",
                            response = Void.class,
                            responseHeaders = @ResponseHeader(
                                    name = "Location",
                                    description = "Returns itemhistoryid for newly created ItemHistory in the header",
                                    response = Void.class
                            )),
                    @ApiResponse(
                            code = 404,
                            message = "Item id {itemid} not found!",
                            response = ErrorDetail.class)
            })
    @PostMapping(value = "/item/{itemid}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewItemHistory(HttpServletRequest request,
                                               Authentication authentication,
                                               @Valid @RequestBody ItemHistory newItemHistory,
                                               @PathVariable long itemid) throws URISyntaxException {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        Item item = itemService.findItemByIdForUser(u, itemid);

        newItemHistory.setItem(item);

        ItemHistory createdItemHistory = itemHistoryService.save(newItemHistory);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(request.getServerName() + ":" + request.getLocalPort() + "/itemhistory/{itemhistoryid}")
                .buildAndExpand(createdItemHistory.getItemhistoryid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    /**
     * PUT
     * http://localhost:2019/itemhistory/{itemhistoryid}
     * @param itemhistoryid : long
     * @param updateItemHistory : ItemHistory
     * */
    @ApiOperation(
            value = "Updates an ItemHistory",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "ItemHistory Updated",
                            response = Void.class),
                    @ApiResponse(
                            code = 404,
                            message = "ItemHistory id {itemhistoryid} not found!",
                            response = ErrorDetail.class)
            })
    @PutMapping(value = "/{itemhistoryid}", consumes = {"application/json"})
    public ResponseEntity<?> updateItemHistoryById(HttpServletRequest request,
                                        Authentication authentication,
                                        @Valid @RequestBody ItemHistory updateItemHistory,
                                        @PathVariable long itemhistoryid) throws URISyntaxException {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        itemHistoryService.update(updateItemHistory, itemhistoryid, u);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE
     * http://localhost:2019/itemhistory/{itemhistoryid}
     * @param itemhistoryid : long
     * */
    @ApiOperation(
            value = "Deletes an ItemHistory based off of itemhistoryid",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "ItemHistory Deleted",
                            response = Void.class),
                    @ApiResponse(
                            code = 404,
                            message = "ItemHistory id {itemhistoryid} not found!",
                            response = ErrorDetail.class)
            })
    @DeleteMapping(value = "/{itemhistoryid}")
    public ResponseEntity<?> deleteItemHistoryById(HttpServletRequest request,
                                            Authentication authentication,
                                            @PathVariable long itemhistoryid) {

        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        itemHistoryService.delete(u, itemhistoryid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
