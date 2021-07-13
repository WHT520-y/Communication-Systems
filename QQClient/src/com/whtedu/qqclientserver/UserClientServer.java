package com.whtedu.qqclientserver;

import com.whtedu.qqcommon.Message;
import com.whtedu.qqcommon.MessageType;
import com.whtedu.qqcommon.User;
import com.whtedu.utils_.StreamUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/*
 *@author 文帅帅
 *@version 1.0
 * 该类完成用户登陆验证，注册等功能
 */
public class UserClientServer {

    private User user; // 因为可能在别的地方要用到User对象，因此要设置属性
    // 因为socket可能在其他地方也要使用到，也要做成属性
    private Socket socket;

    // 发送文件(这里指定传入音频)
    public void sendFile(String userID, String path){
        Message message = new Message();
        message.setSender(user.getUserID());
        message.setReceiver(userID);
        message.setMesType(MessageType.MESSAGE_FILE);
        // 先从磁盘中拿到
        try {

            byte[] bytes = StreamUtils.streamToByteArray(
                    new BufferedInputStream(new FileInputStream(path)));
            message.setBytes(bytes);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 群发消息
     public void groupMessage(String userID, String mesContent){
         Message message = new Message();
         message.setMesType(MessageType.MESSAGE_GROUP);
         message.setSender(user.getUserID());
         message.setContent(mesContent);
         try {
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             oos.writeObject(message);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
    // 私聊
    public void privateChat(String userID){
        System.out.println("请输入你要发送给" + userID + "的消息:");
        Scanner sc = new Scanner(System.in);
        String mesContent = sc.next();
        Message message = new Message();
        message.setSender(user.getUserID());
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setContent(mesContent);
        message.setReceiver(userID);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageExit(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserID());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向服务器请求在线列表
    public void getOnlineFriend(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        try {
//            ObjectOutputStream oos =
//                            new ObjectOutputStream(ManageClientThread.getClientConnectServerThread(
//                            user.getUserID()).getSocket().getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理用户名和密码[以对象的形式传输]
    public boolean checkUser(String userID, String password){
        boolean b = false;
        user = new User(userID, password);
        ObjectOutputStream oos = null;
        try {
            socket = new Socket(InetAddress.getByName("192.168.0.103"), 9999);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user); // 向服务端发送对象

            // 接受来自服务端的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)ois.readObject();

            if (message.getMesType().equals(MessageType.MASSAGE_LOGIN_SUCCEED)){ // 登陆成功

                //创建一个和服务器保持通信的线程
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ManageClientThread.addClientConnectServerThread(userID, clientConnectServerThread);
                b = true;
            }else { // 登陆失败
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                if (oos != null)
//                    oos.close();  // 关了会有问题
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return b;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
