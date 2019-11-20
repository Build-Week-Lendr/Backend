package com.zero5nelsonm.lendr.controllers;

import com.zero5nelsonm.lendr.logging.Loggable;
import com.zero5nelsonm.lendr.model.ErrorDetail;
import com.zero5nelsonm.lendr.model.User;
import com.zero5nelsonm.lendr.model.UserMinimum;
import com.zero5nelsonm.lendr.model.UserRoles;
import com.zero5nelsonm.lendr.service.RoleService;
import com.zero5nelsonm.lendr.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Loggable
@RestController
@Api(tags = {"OpenEndpoint"})
public class OpenController {
    private static final Logger logger = LoggerFactory.getLogger(OpenController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private String getPort(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getServerName()
                .equalsIgnoreCase("localhost")) {
            return ":" + httpServletRequest.getLocalPort();
        } else {
            return "";
        }
    }

    /**
     * POST
     * http://localhost:2019/createnewuser
     * http://localhost:2019/createnewuser?returninfo=false
     * @param newminuser : UserMinimum
     * */
    @ApiOperation(
            value = "Creates a new user",
            response = User.class)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "User Created",
                            response = User.class,
                            responseHeaders = @ResponseHeader(
                                    name = "Location",
                                    description = "Returns userid for newly created user in the header",
                                    response = Void.class
                            )),
                    @ApiResponse(
                            code = 400,
                            message = "Username already exists",
                            response = ErrorDetail.class),
                    @ApiResponse(
                            code = 400,
                            message = "Email is already associated with a username",
                            response = ErrorDetail.class)
            })
    @ApiParam(
            name = "returninfo",
            defaultValue = "true",
            type = "boolean",
            required = false,
            value = "Will return nothing if set equal to false",
            example = "BASEURL/createnewuser?returninfo=false")
    @PostMapping(value = "/createnewuser", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewUser(HttpServletRequest httpServletRequest,
                                        @RequestParam(defaultValue = "true") boolean returninfo,
                                        @Valid @RequestBody UserMinimum newminuser) throws URISyntaxException {
        logger.trace(
                httpServletRequest.getMethod().toUpperCase() + " " + httpServletRequest.getRequestURI() + " accessed");

        // Create the user
        User newuser = new User();

        newuser.setUsername(newminuser.getUsername());
        newuser.setPassword(newminuser.getPassword());
        newuser.setEmail(newminuser.getEmail());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        newRoles.add(new UserRoles(newuser, roleService.findByName("user")));
        newuser.setUserroles(newRoles);

        newuser = userService.save(newuser);

        // Set the location header for the newly created resource - to another controller!
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(httpServletRequest.getServerName() + ":" + httpServletRequest.getLocalPort() + "/users/user/{userId}")
                .buildAndExpand(newuser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        String theToken = "";
        if (returninfo) {
            // Return the access token
            RestTemplate restTemplate = new RestTemplate();
            String requestURI = "http://" + httpServletRequest.getServerName() + getPort(httpServletRequest) + "/login";

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(acceptableMediaTypes);
            headers.setBasicAuth(System.getenv("OAUTHCLIENTID"),
                    System.getenv("OAUTHCLIENTSECRET"));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type",
                    "password");
            map.add("scope",
                    "read write trust");
            map.add("username",
                    newminuser.getUsername());
            map.add("password",
                    newminuser.getPassword());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                    headers);

            theToken = restTemplate.postForObject(requestURI,
                    request,
                    String.class);
        } else {
            // nothing;
        }
        return new ResponseEntity<>(theToken,
                responseHeaders,
                HttpStatus.CREATED);
    }

    @ApiIgnore
    @GetMapping("favicon.ico")
    void returnNoFavicon() {
        logger.trace("favicon.ico endpoint accessed!");
    }
}
