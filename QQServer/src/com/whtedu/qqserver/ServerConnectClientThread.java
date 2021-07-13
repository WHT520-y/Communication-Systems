package com.whtedu.qqserver;

import com.whtedu.qqcommon.Message;
import com.whtedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 *@author 文帅帅
 *@version 1.0
 *  该类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{

    private Socket socket;
    private String userID; // socket跟那个客户端保持通信，userID来标识

    public ServerConnectClientThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    @Override
    public void run() { // 发送和接收消息
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    String onlineUser = ManageServerThread.getOnlineUser();
                    Message message1 = new Message();
                    message1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineUser);
                    oos.writeObject(message1);
                }else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    ManageServerThread.remove(message.getSender());
                    socket.close();
                    break;
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){ // 接收聊天内容的
                    String receiver = message.getReceiver();
                    // 判断用户在不下线
                    ServerConnectClientThread serverConnectClientThread = ManageServerThread.getServerConnectClientThread(receiver);
                    if (serverConnectClientThread != null){  // 如果用户在线
                        Socket receiverSocket = serverConnectClientThread.getSocket();
                        ObjectOutputStream receiverOOS = new ObjectOutputStream(receiverSocket.getOutputStream());
                        receiverOOS.writeObject(message);
                    }else {           // 如果用户不在线
                        addOffLine(receiver,message);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_GROUP)){
                    // 群发消息
                    String sender = message.getSender();
                    message.setMesType(MessageType.MESSAGE_COMM_MES);
                    HashMap<String, ServerConnectClientThread> hashMap = ManageServerThread.getHashMap();
                    Set<Map.Entry<String, ServerConnectClientThread>> entries = hashMap.entrySet();
                    for (Map.Entry<String, ServerConnectClientThread> entry : entries){
                        if (!entry.getKey().equals(sender)){
                            ServerConnectClientThread serverConnectClientThread = entry.getValue();
                            ObjectOutputStream oos = new ObjectOutputStream(
                                    serverConnectClientThread.getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE)){
                    String receiver = message.getReceiver();
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageServerThread.getServerConnectClientThread(receiver).getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void addOffLine(String userID, Message message){
        ConcurrentHashMap<String, ArrayList<Message>> cchm = QQServer.getConcurrentHashMap();
        if (cchm.size() == 0 || cchm.get(userID) == null){
            ArrayList<Message> messageArrayList = new ArrayList<>();
            messageArrayList.add(message);
            cchm.put(userID,messageArrayList);
        }else {
            ArrayList<Message> messageArrayList = cchm.get(userID);
            messageArrayList.add(message);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserID() {
        return userID;
    }
}
