package com.justinlee.drawmatic.User;

import android.content.Context;

import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.constants.Constants;

/**
 * Created by Wayne Chen on 2018/3/15.
 */

public class UserManager {
    private static final UserManager userInstance = new UserManager();

    private String mUserName;
    private String mUserId;
    private String mUserToken;

    public static UserManager getInstance() {
        return userInstance;
    }

    private UserManager() {

    }

    public String getUserName() {
        return Drawmatic.getAppContext().getSharedPreferences(Constants.UserData.SHAREPREF_USER_DATA_KEY, Context.MODE_PRIVATE)
                .getString(Constants.UserData.SHAREPREF_USER_NAME_KEY, null);
    }

    public String getUserId() {
        return Drawmatic.getAppContext().getSharedPreferences(Constants.UserData.SHAREPREF_USER_DATA_KEY, Context.MODE_PRIVATE)
                .getString(Constants.UserData.SHAREPREF_USER_ID_KEY, null);
    }

    public String getUserToken() {
        return mUserToken;
    }

    public void setUserName(String userName) {
        mUserName = userName;

        Drawmatic.getAppContext()
                .getSharedPreferences(Constants.UserData.SHAREPREF_USER_DATA_KEY, Context.MODE_PRIVATE)
                .edit()
                .putString(Constants.UserData.SHAREPREF_USER_NAME_KEY, userName)
                .commit();
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setUserToken(String userToken) {
        mUserToken = userToken;
    }
}

