package com.igl.crowdword.fxns.analysis;

import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.WordSet;

/**
 * Created by Kunal on 7/16/2015.
 */
public class WordSetVote {

    //@Table(name = "crowdspell_setVotes")


  //  @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @ManyToOne
  //  @JoinColumn(name = "userId")
    private User user;

//    @ManyToOne
  //  @JoinColumn(name = "setId")
    private WordSet wordSet;

    public WordSet getWordSet() {
        return wordSet;
    }

    public void setWordSet(WordSet wordSet) {
        this.wordSet = wordSet;
    }

    // Can be -1,0,+1
    //@Column
    private Integer score;


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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public WordSetVote() {

    }

    public WordSetVote(User user, WordSet wordSet) {
        super();
        this.user = user;
        this.wordSet = wordSet;
    }

}
