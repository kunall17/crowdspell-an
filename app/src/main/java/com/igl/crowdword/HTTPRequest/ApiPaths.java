package com.igl.crowdword.HTTPRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MAHE on 9/5/2015.
 */
public class ApiPaths {
    public static final String USERS = "users";
    public static final String SETS = "sets";
    public static final String SCORES = "scores";
    public static final String FAVOURITES = "favourites";
    public static final String PARAM_ID = "id";
    public static final String SORT_TOP = "top";
    public static final String SORT_NEW = "new";
    public static final String LOGIN = "login";
    public static final String FILTER = "filter";

    public static final String WP_APP_KEY = "89d1382b-244d-4712-ab4a-d05f9854d8d7";
    public static final String ANDROID_APP_KEY = "4b08dee3-c8ec-40a0-99d1-4aee47c0772a";

    public static final List<String> APP_KEYS = Arrays.asList(new String[]{
            WP_APP_KEY, ANDROID_APP_KEY});

    public static final String APP_AUTH_KEY = "app-id";
}
