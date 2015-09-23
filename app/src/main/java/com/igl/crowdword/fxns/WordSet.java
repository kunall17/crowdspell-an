package com.igl.crowdword.fxns;

import android.os.Parcelable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal on 7/16/2015.
 */

public class WordSet  {
 /*
// @Table(name = "crowdspell_set")
// @NamedQueries(value = { // @NamedQuery(name = QueryKs.GET_NEW_SETS, query = "select w from WordSet w order by w.createdDate desc") })
*/




    private static final long serialVersionUID = 1L;

//    // @Id
  //  // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

  //  // @ManyToOne
  //  // @JoinColumn(name = "userId")
    private User user;

    // @Column
    private String name;

    // @Column
    private Integer difficultyLevel;

    // @Column
    private Date createdDate;

    // @Column
    private Date lastUpdate;

    // @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    // @JoinTable(name = "wordSets_tags")
    private List<Tag> tags;

    // @Transient
    private List<Word> words;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    // @Transient
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



}
