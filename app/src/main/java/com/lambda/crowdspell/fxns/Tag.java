package com.lambda.crowdspell.fxns;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kunal on 7/16/2015.
 */
public class Tag implements Parcelable {
  //  @Table(name = "crowdspell_tags")
    private static final long serialVersionUID = 1L;

  //  @Id
    private String name;

    public Tag() {

    }

    public Tag(String name) {
        setName(name);
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.toLowerCase();
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(serialVersionUID);

    }
    public Tag(Parcel input){
        name = input.readString();
    }
}
