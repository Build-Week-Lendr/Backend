package com.zero5nelsonm.lendr.controllers;

import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.ErrorDetail;
import com.zero5nelsonm.lendr.model.ItemHistory;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.service.ItemHistoryService;
import com.zero5nelsonm.lendr.service.ItemService;
import com.zero5nelsonm.lendr.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
                            message = "ItemHistory id not found!",
                            response = ErrorDetail.class)
            })
    @GetMapping(value = "/{itemhistoryid}", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserItemHistory(HttpServletRequest request,
                                                       Authentication authentication,
                                                       @PathVariable long itemhistoryid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());

        ItemHistory itemHistory = itemHistoryService.findItemHistoryByIdForUser(u, itemhistoryid);

        return new ResponseEntity<>(itemHistory, HttpStatus.OK);
    }

    /**
     * PUT
     * http://localhost:2019/itemhistory/{itemhistoryid}
     * @param itemhistoryid : long
     * @param updateItemHistory : ItemHistory
     * */

    /**
     * DELETE
     * http://localhost:2019/itemhistory/{itemhistoryid}
     * @param itemhistoryid : long
     * */
}
