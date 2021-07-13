package com.whtedu.qqcommon;

import java.io.Serializable;
import java.util.ArrayList;

/*
 *@author 文帅帅
 *@version 1.0
 *  表示用户
 */
public class User implements Serializable { // 要用对象流处理进行网络传输，因此要实现这个接口
    private static final long serialVersionUID = 1L;
    private String userID;
    private String passward;

    public User(String userID, String passward) {
        this.userID = userID;
        this.passward = passward;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}
