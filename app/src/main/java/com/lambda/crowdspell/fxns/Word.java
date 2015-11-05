package com.lambda.crowdspell.fxns;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by Kunal on 7/16/2015.
 */
public class Word implements Parcelable {
    //@Table(name = "crowdspell_words")
    // @NamedQuery(name=QueryKs.WORDS_FOR_SET, query="select w from Word w where w.wordSet=:wordSet")

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column
    private String originalValue;

    //  @Column
    private Date createdDate;

    //    @Column
    private Date lastUpdate;

    //    @ManyToOne
    //  @JoinColumn(name = "setId")
    private WordSet wordSet;
    //
    //   @Column
    private String hint;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    //  @Transient
    private String userToken;

    //    @Transient
    private int chancesTaken;

    public int getChancesTaken() {
        return chancesTaken;
    }

    public void setChancesTaken(int chancesTaken) {
        this.chancesTaken = chancesTaken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public WordSet getWordSet() {
        return wordSet;
    }

    public void setWordSet(WordSet wordSet) {
        this.wordSet = wordSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
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
        dest.writeString(this.originalValue);


        if (createdDate!= null) dest.writeLong(this.createdDate.getTime());
        else dest.writeLong(0);
        if (lastUpdate != null) dest.writeLong(this.lastUpdate.getTime());
        else dest.writeLong(0);
        dest.writeParcelable(this.wordSet, 0);
        dest.writeString(this.hint);
        dest.writeString(this.userToken);
        dest.writeInt(this.chancesTaken);
    }

    public Word() {
    }

    protected Word(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.originalValue = in.readString();

       Long a = in.readLong();
        if(a!=0) this.createdDate = Date.valueOf(String.valueOf(a));

        a = in.readLong();
        if(a!=0) this.lastUpdate = Date.valueOf(String.valueOf(a));
        this.wordSet = in.readParcelable(WordSet.class.getClassLoader());
        this.hint = in.readString();
        this.userToken = in.readString();
        this.chancesTaken = in.readInt();
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
}
