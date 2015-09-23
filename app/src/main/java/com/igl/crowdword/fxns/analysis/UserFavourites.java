package com.igl.crowdword.fxns.analysis;

/**
 * Created by MAHE on 9/5/2015.
 */
public class UserFavourites {

    private String userToken;
    private long setId;
    private boolean like;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
