package com.zero5nelsonm.lendr.controllers;

import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.Item;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.service.ItemService;
import com.zero5nelsonm.lendr.service.UserService;
import io.swagger.annotations.Api;
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

    @GetMapping(value = "/items", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserItems(HttpServletRequest request, Authentication authentication) {

        logger.trace(request.getMethod().toUpperCase() + "" + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        List<Item> itemList = itemService.findAllByUsername(u.getUsername());

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping(value = "/item", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewItem(HttpServletRequest request,
                                        Authentication authentication,
                                        @Valid @RequestBody Item newItem)
            throws URISyntaxException {

        logger.trace(request.getMethod().toUpperCase() + "" + request.getRequestURI() + " accessed");

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
}