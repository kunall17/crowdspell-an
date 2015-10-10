package com.lambda.crowdspell.fxns;

/**
 * Created by Kunal on 7/16/2015.
 */
public class Tag {
  //  @Table(name = "crowdspell_tags")
    private static final long serialVersionUID = 1L;

  //  @Id
    private String name;

    public Tag() {

    }

    public Tag(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.toLowerCase();
        this.name = name;
    }

}
