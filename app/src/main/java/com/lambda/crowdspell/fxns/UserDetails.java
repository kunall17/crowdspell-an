package com.lambda.crowdspell.fxns;

/**
 * Created by Kunal on 7/12/2015.
 */
public class UserDetails {


//@Table(name = "crowdspell_userDetails")

    private static final long serialVersionUID = 1L;

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

  //  @Column(name="email")
    private String email;

//    @Column(name="platform")
    private String platform;

    public void setPlatform(String platform) {
        this.platform= platform;
    }
    public void setEmail(String email) {
        this.email= email;
    }


    public String getEmail() {
        return email;
    }

    public String getPlatform() {
        return platform;
    }

    public UserDetails() {
    }
}
