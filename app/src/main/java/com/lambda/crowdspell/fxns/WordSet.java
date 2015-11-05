package com.lambda.crowdspell.fxns;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal on 7/16/2015.
 */

public class WordSet implements Parcelable {
 /*
// @Table(name = "crowdspell_set")
// @NamedQueries(value = { // @NamedQuery(name = QueryKs.GET_NEW_SETS, query = "select w from WordSet w order by w.createdDate desc") })
*/


    private static final long serialVersionUID = 1L;
    private Long id;
    private User user;
    private String name;
    private Integer difficultyLevel;
    private Date createdDate;
    private Date lastUpdate;
    private List<Tag> tags;
    private List<Word> words;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Tag> getTags() {
        return tags;
    }


    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (tags == null) {
            this.tags = new ArrayList<Tag>();
        }
        tags.add(tag);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.name);
        dest.writeValue(this.difficultyLevel);

        if (createdDate != null) dest.writeLong(Long.valueOf(this.createdDate.getTime()));
        else dest.writeLong(0);

        if (lastUpdate != null) dest.writeLong(Long.valueOf(this.lastUpdate.getTime()));
        else dest.writeLong(0);
        dest.writeTypedList(tags);
        dest.writeTypedList(words);
        dest.writeList(this.words);
        dest.writeString(this.userToken);
    }

    public WordSet() {
    }

    protected WordSet(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
        this.difficultyLevel = (Integer) in.readValue(Integer.class.getClassLoader());


        Long a = in.readLong();
        if (a != 0) this.createdDate = Date.valueOf(String.valueOf(in.readLong()));
        a = in.readLong();
        if (a != 0) this.lastUpdate = Date.valueOf(String.valueOf(in.readLong()));
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.words = new ArrayList<Word>();
        in.readList(this.words, List.class.getClassLoader());
        this.userToken = in.readString();
    }

    public static final Parcelable.Creator<WordSet> CREATOR = new Parcelable.Creator<WordSet>() {
        public WordSet createFromParcel(Parcel source) {
            return new WordSet(source);
        }

        public WordSet[] newArray(int size) {
            return new WordSet[size];
        }
    };
}
