package com.gongshe.model;

import com.gongshe.GongSheApp;
import com.gongshe.R;

public class GongSheConstant {
    public static final String BASE_URL = "http://192.168.1.7:8080";

    public static final String PATH_USER = "/user";
    public static final String PATH_REGISTER = "/register";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_FIND_FRIENDS = "/findFriends";
    public static final String PATH_FIND_USERLIST_BY_PHONE = "/findUserListByPhone";
    public static final String PATH_ADD_FRIEND_BY_PHONE = "/addFriendByPhone";
    public static final String PATH_ADD_FRIEND_BY_ID = "/addFriendById";

    public static final String PATH_GROUP = "/group";
    public static final String PATH_MY_GROUP = "/findCreatedGroups";
    public static final String PATH_BELONG_GROUP = "/findBelongToGroups";
    public static final String PATH_GROUP_CREATE = "/create";
    public static final String PATH_GROUP_GET_ALL_MEMBER = "/getGroupAllMember";
    public static final String PATH_GROUP_DELETE_MEMBER = "/deleteMember";

    public static final String PATH_POST = "/post";
    public static final String PATH_POST_CREATE = "/create";
    public static final String PATH_POST_ALL_IN_GROUP = "/findAllInGroup";
    public static final String PATH_POST_REPLY = "/reply";
    public static final String PATH_POST_FINDALLOFSAMETITLE = "/findAllOfSameTitle";
    public static final String PATH_POST_FINDRECENTPOSTS = "/findRecentPosts";
    public static final String PATH_POST_FINDALLINVOLVED = "/findInvolved";

    public static final String RESULT_AUTH_ERROR = "auth_error";
    public static final String RESULT_OK = "ok";
    public static final String RESULT_FAIL = "fail";

    public final static Group ALL_INVOLVED_GROUP;
    public final static Group ALL_AT_ME_GROUP;
    public final static Group ALL_ACTIVITY_GROUP;
    static {
        ALL_ACTIVITY_GROUP = new Group();
        ALL_ACTIVITY_GROUP.setId(-1);
        ALL_ACTIVITY_GROUP.setName(GongSheApp.getInstance()
                                             .getApplicationContext()
                                             .getString(R.string.btn_activities));
        ALL_AT_ME_GROUP = new Group();
        ALL_AT_ME_GROUP.setId(-2);
        ALL_AT_ME_GROUP.setName(GongSheApp.getInstance()
                                        .getApplicationContext()
                                        .getString(R.string.menu_all_involved_me));
        ALL_INVOLVED_GROUP = new Group();
        ALL_INVOLVED_GROUP.setId(-3);
        ALL_INVOLVED_GROUP.setName(GongSheApp.getInstance()
                                            .getApplicationContext()
                                            .getString(R.string.menu_all_mention_me));
    }
}
