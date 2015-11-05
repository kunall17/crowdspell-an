package com.lambda.crowdspell.fxns;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Kunal on 7/12/2015.
 */
public class User implements Parcelable {

    
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.username);
        dest.writeString(this.token);
        dest.writeLong(joiningDate != null ? joiningDate.getTime() : -1);
        dest.writeParcelable(this.details, flags);
        dest.writeString(this.password);
        dest.writeString(this.salt);
        dest.writeString(this.authenticationProvider);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.username = in.readString();
        this.token = in.readString();
        long tmpJoiningDate = in.readLong();
        this.joiningDate = tmpJoiningDate == -1 ? null : new Date(tmpJoiningDate);
        this.details = in.readParcelable(UserDetails.class.getClassLoader());
        this.password = in.readString();
        this.salt = in.readString();
        this.authenticationProvider = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
