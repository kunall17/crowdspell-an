package com.lambda.crowdspell.fxns;

import java.sql.Date;

/**
 * Created by Kunal on 7/16/2015.
 */
public class Word {
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


}
