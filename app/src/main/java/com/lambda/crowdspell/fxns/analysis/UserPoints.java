package com.lambda.crowdspell.fxns.analysis;

import com.lambda.crowdspell.fxns.User;
import com.lambda.crowdspell.fxns.WordSet;

/**
 * Created by Kunal on 7/16/2015.
 */
public class UserPoints {
//    @Table(name = "crowdspell_userpoints")
//    @NamedQueries(value = {
//            @NamedQuery(name = QueryKs.FIND_POINT_USER_SET, query = "select u from UserPoints u where u.wordSet=:wSet and u.user=:user"),
//            @NamedQuery(name = QueryKs.FINDTOPSCORERS, query = "select u from UserPoints u group by u.userId order by sum(u.points) desc limit 20") })


//    @Id
  //  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@ManyToOne
    //@JoinColumn(name = "userId")
    private User user;

//    @ManyToOne
 //   @JoinColumn(name = "setId")
    private WordSet wordSet;

   // @Column
    private Integer points;

  //  @Column
    private Boolean complete;

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
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

    public WordSet getWordSet() {
        return wordSet;
    }

    public void setWordSet(WordSet wordSet) {
        this.wordSet = wordSet;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }



}
