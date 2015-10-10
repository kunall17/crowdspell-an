package com.lambda.crowdspell.fxns;

import java.util.Date;

/**
 * Created by Kunal on 7/12/2015.
 */
public class User {

    
    private static final long serialVersionUID = 1L;

    private Long id;

  //  @Column(name = "username", unique = true)
    private String username;

//    @Column(name = "token", unique = true)
    private String token;

//    @Column
    private Date joiningDate;

    private UserDetails details;

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

   // @Column(name = "password")
    private String password;

//    @Column(name = "salt")
    private String salt;

  //  @Column(name = "authProvider")
    private String authenticationProvider;

    public String getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(String authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void saveToShare(User dd ,UserDetails ud){

    }
}
