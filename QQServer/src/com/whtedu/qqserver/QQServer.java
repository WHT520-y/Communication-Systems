package com.whtedu.qqserver;

import com.whtedu.qqcommon.Message;
import com.whtedu.qqcommon.MessageType;
import com.whtedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/*
 *@author 文帅帅
 *@version 1.0
 */
public class QQServer {

    private ServerSocket serverSocket = null;
    private static HashMap<String, User> validUsers = new HashMap<>();
    private NewsThread newsThread = new NewsThread();
    private static ConcurrentHashMap<String, ArrayList<Message>> concurrentHashMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<Message>> getConcurrentHashMap() {
        return concurrentHashMap;
    }

    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
    }

    public static void main(String[] args) {
        new QQServer();
    }

    private boolean checkUser(User user){
        User u = validUsers.get(user.getUserID());
        if (u != null && user.getPassward().equals(u.getPassward()))
            return true;
        return false;
    }

    public QQServer() {
        try {
            newsThread.start(); // 启动推送新闻的线程
            serverSocket = new ServerSocket(9999);
            while (true){
                System.out.println("服务端正在9999端口监听...");
                Socket socket = serverSocket.accept();
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();
                Message message = new Message();
                if (checkUser(user)){
                    System.out.println("服务端和客户端" + user.getUserID() + "保持通信，读取数据....");
                    message.setMesType(MessageType.MASSAGE_LOGIN_SUCCEED);
                    ObjectOutputStream oos =
                            new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, user.getUserID());
                    serverConnectClientThread.start();
                    ManageServerThread.addServerConnectClientThread(user.getUserID(),serverConnectClientThread);
                    // 判断是否有离线消息，有的话，就发送
                    ArrayList<Message> messageArrayList = concurrentHashMap.get(user.getUserID());
                    if (messageArrayList != null){ // 说明有离线消息
                        for (int i = 0; i < messageArrayList.size(); i++) {
                            ObjectOutputStream oos1 =
                                    new ObjectOutputStream(socket.getOutputStream());
                            Message offLineMessage = messageArrayList.get(i);
                            oos1.writeObject(offLineMessage);
                        }
                        concurrentHashMap.remove(user.getUserID()); // 发送完离线消息后将它移除
                    }
                }else { // 登陆失败
                    message.setMesType(MessageType.MASSAGE_LOGIN_FALSE);
                    ObjectOutputStream oos =
                            new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    socket.close();
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
