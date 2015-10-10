package com.lambda.crowdspell.fxns.analysis;

import com.lambda.crowdspell.fxns.Word;

import java.util.List;

/**
 * Created by Kunal on 7/16/2015.
 */
public class SetScoreCarrier {

    private String userToken;
    private List<Word> words;
    private long setId;
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public List<Word> getWords() {
        return words;
    }
    public void setWords(List<Word> words) {
        this.words = words;
    }
    public long getSetId() {
        return setId;
    }
    public void setSetId(long setId) {
        this.setId = setId;
    }


}
