package com.whtedu.qqserver;

import com.whtedu.qqcommon.Message;
import com.whtedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 *@author 文帅帅
 *@version 1.0
 * 推送新闻
 */
public class NewsThread extends Thread{
    private Scanner sc = new Scanner(System.in);
    @Override
    public void run() {
        System.out.println("新输入你要推送的消息:");
        String news = sc.next();
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender("新闻热点");
        message.setContent(news);
        HashMap<String, ServerConnectClientThread> hashMap = ManageServerThread.getHashMap();
        Set<Map.Entry<String, ServerConnectClientThread>> entries = hashMap.entrySet();
        for (Map.Entry<String, ServerConnectClientThread> entry : entries){
            try {
                ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                oos.writeObject(message);
                System.out.println("推送新闻成功..");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
