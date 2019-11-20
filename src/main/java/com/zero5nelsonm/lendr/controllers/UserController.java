package com.zero5nelsonm.lendr.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero5nelsonm.lendr.handlers.RestExceptionHandler;
import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.ErrorDetail;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Loggable
@Api(tags = {"UserEndpoints"})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private UserService userService;

    /**
     * GET:
     * http://localhost:2019/users/users/?page=1&size=1
     * http://localhost:2019/users/users/?sort=username,desc&sort=<field>,asc
     * http://localhost:2019/users/users
     * */
    @ApiOperation(
            value = "Returns all users with paging and sorting [Admin]",
            response = User.class,
            responseContainer = "List")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(
                            name = "page",
                            dataType = "integer",
                            paramType = "query",
                            value = "Results page you want to retrieve (1..N)"),
                    @ApiImplicitParam(
                            name = "size",
                            dataType = "integer",
                            paramType = "query",
                            value = "Number of records per page."),
                    @ApiImplicitParam(
                            name = "sort",
                            allowMultiple = true,
                            dataType = "string",
                            paramType = "query",
                            value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users", produces = {"application/json"})
    public ResponseEntity<?> listAllUsers(HttpServletRequest request,
                                          @PageableDefault(page = 0, size = 5) Pageable pageable) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        List<User> myUsers = userService.findAll(pageable);
        return new ResponseEntity<>(myUsers,
                HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/users/allusers
     * */
    @ApiOperation(
            value = "Returns all Users without paging or sorting [Admin]",
            response = User.class,
            responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/allusers", produces = {"application/json"})
    public ResponseEntity<?> reallyListAllUsers() {
        List<User> myUsers = userService.findAll(Pageable.unpaged());
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/users/user/{userid}
     * @param userid : long
     * */
    @ApiOperation(
            value = "Retrieve a user based of off user id [Admin]",
            response = User.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "User Found",
                            response = User.class),
                    @ApiResponse(
                            code = 404,
                            message = "User Not Found",
                            response = ErrorDetail.class)
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/user/{userid}", produces = {"application/json"})
    public ResponseEntity<?> getUserById(HttpServletRequest request,
                                         @ApiParam(value = "User id", required = true, example = "4")
                                         @PathVariable Long userid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findUserById(userid);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/users/user/name/{username}
     * @param username : String
     * */
    @ApiOperation(
            value = "Returns the user based off of user name [Admin]",
            response = User.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "User Found",
                            response = User.class),
                    @ApiResponse(
                            code = 404,
                            message = "User Not Found",
                            response = ErrorDetail.class)
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/user/name/{username}", produces = {"application/json"})
    public ResponseEntity<?> getUserByName(HttpServletRequest request,
                                           @ApiParam(value = "Username", required = true, example = "somename")
                                           @PathVariable String username) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(username);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    /**
     * POST
     * http://localhost:2019/users/user
     * @param newuser : User
     * */
    @ApiOperation(
            value = "Adds a user given in the request body [Admin]",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "User Created",
                            response = Void.class),
                    @ApiResponse(
                            code = 404,
                            message = "User Not Found",
                            response = ErrorDetail.class)
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/user", consumes = {"application/json"})
    public ResponseEntity<?> addNewUser(HttpServletRequest request,
                                        @Valid @RequestBody User newuser) throws URISyntaxException {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        newuser = userService.save(newuser);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newuser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    /**
     * PUT
     * http://localhost:2019/users/user/{userid}
     * @param userid : long
     * */
    @ApiOperation(
            value = "Updates a user [Admin]",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "User Found",
                            response = Void.class),
                    @ApiResponse(
                            code = 404,
                            message = "User Not Found",
                            response = ErrorDetail.class)
            })
    @PutMapping(value = "/user/{userid}", consumes = {"application/json"})
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                        @RequestBody User updateUser,
                                        @PathVariable long userid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        userService.update(updateUser, userid, request.isUserInRole("ADMIN"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE
     * http://localhost:2019/users/user/{userid}
     * @param userid : long
     * */
    @ApiOperation(
            value = "Deletes a user [Admin]",
            response = Void.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "User Found",
                            response = Void.class),
                    @ApiResponse(
                            code = 404,
                            message = "User Not Found",
                            response = ErrorDetail.class)
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/user/{userid}")
    public ResponseEntity<?> deleteUserById(HttpServletRequest request,
                                            @PathVariable long userid) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        userService.delete(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET
     * http://localhost:2019/users/getuserinfo
     * */
    @ApiOperation(
            value = "Returns user information for the authenticated user",
            response = User.class)
    @GetMapping(value = "/getuserinfo", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request, Authentication authentication) {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findByName(authentication.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}