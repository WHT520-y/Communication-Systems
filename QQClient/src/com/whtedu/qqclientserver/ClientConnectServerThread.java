package com.whtedu.qqclientserver;

import com.whtedu.qqcommon.Message;
import com.whtedu.qqcommon.MessageType;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

/*
 *@author 文帅帅
 *@version 1.0
 * 与服务器链接的线程
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                // 拿到 Message 之后,判断消息类型
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    String content = message.getContent();
                    // 规定消息内容为 100 200 300 即用户名是用空格隔开的
                    String[] s = content.split(" ");
                    System.out.println("在线用户为：");
                    for (int i = 0; i < s.length; i++) {
                        System.out.println(s[i]);
                    }
                    System.out.println("用户数量为：" + ManageClientThread.hashMap.size());
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println(message.getSender() + ":" + message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE)){
                    System.out.println(message.getSender() + "发来一个文件，请输入你要存储的路径：");
                    System.out.println("字节大小 " + message.getBytes().length);
                    Scanner sc = new Scanner(System.in);
                    String path = sc.next();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                    bos.write(message.getBytes());
                    bos.flush();
                    bos.close();
                    System.out.println("文件传输完毕");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 接受服务器发送过来的消息
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
