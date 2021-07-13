package com.whtedu.qqclientserver;

import java.util.HashMap;

/*
 *@author 文帅帅
 *@version 1.0
 *  管理用户的线程
 */
public class ManageClientThread {

    // key 为用户id ， value 为线程
    public static HashMap<String,ClientConnectServerThread> hashMap
            = new HashMap<>();

    public static void addClientConnectServerThread(String userID, ClientConnectServerThread clientConnectServerThread){
        hashMap.put(userID,clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hashMap.get(userID);
    }

}
