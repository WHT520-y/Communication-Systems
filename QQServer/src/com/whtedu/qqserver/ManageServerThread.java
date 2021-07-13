package com.whtedu.qqserver;

import java.util.HashMap;
import java.util.Set;

/*
 *@author 文帅帅
 *@version 1.0
 */
public class ManageServerThread {
    private static HashMap<String,ServerConnectClientThread> hashMap =
            new HashMap<>();

    public static void addServerConnectClientThread(String userID, ServerConnectClientThread serverConnectClientThread){
        hashMap.put(userID,serverConnectClientThread);
    }

    public static ServerConnectClientThread getServerConnectClientThread(String userID){
        return hashMap.get(userID);
    }

    public static void remove(String userID){
        hashMap.remove(userID);
    }

    public static String getOnlineUser(){
        Set<String> users = hashMap.keySet();
        String str = "";
        for (String user: users){
            str += user + " ";
        }
        return str;
    }

    public static HashMap<String, ServerConnectClientThread> getHashMap() {
        return hashMap;
    }
}
