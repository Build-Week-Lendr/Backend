package com.zero5nelsonm.lendr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zero5nelsonm.lendr.logging.Loggable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "User", description = "This is an actual user")
@Loggable
@Entity
@Table(name = "users")
public class User extends Auditable {

    @ApiModelProperty(name = "user id",
            value = "primary key for User",
            required = true,
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @ApiModelProperty(name = "User Name",
            value = "Actual user name for sign on",
            required = true,
            example = "Some Name")
    @Size(min = 2,
            max = 30,
            message = "User Name must be between 2 and 30 characters")
    @Column(nullable = false,
            unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 4, message = "Password must 4 or more characters")
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid format username@domain.toplevel")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<UserRoles> userroles = new ArrayList<>();

    public User() {
    }

    public User(String username,
                String password,
                String email,
                List<UserRoles> userRoles) {
        setUsername(username);
        setPassword(password);
        this.email = email;
        for (UserRoles ur : userRoles) {
            ur.setUser(this);
        }
        this.userroles = userRoles;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        if (username == null) { // this is possible when updating a user
            return null;
        } else {
            return username.toLowerCase();
        }
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getEmail() {
        if (email == null) { // this is possible when updating a user
            return null;
        } else {
            return email.toLowerCase();
        }
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNotEncrypt(String password) {
        this.password = password;
    }

    public List<UserRoles> getUserroles() {
        return userroles;
    }

    public void setUserroles(List<UserRoles> userroles) {
        this.userroles = userroles;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.userroles) {
            String myRole = "ROLE_" + r.getRole()
                    .getName()
                    .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }
}
