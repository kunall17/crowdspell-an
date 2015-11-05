package com.lambda.crowdspell.fxns;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kunal on 7/12/2015.
 */
public class UserDetails implements Parcelable {


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.email);
        dest.writeString(this.platform);
    }

    protected UserDetails(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.email = in.readString();
        this.platform = in.readString();
    }

    public static final Parcelable.Creator<UserDetails> CREATOR = new Parcelable.Creator<UserDetails>() {
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }

        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };
}
