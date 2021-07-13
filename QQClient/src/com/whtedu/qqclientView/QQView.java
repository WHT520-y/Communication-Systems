package com.whtedu.qqclientView;

import com.whtedu.qqclientserver.UserClientServer;

import java.util.Scanner;

/*
 *@author 文帅帅
 *@version 1.0
 * 客户端菜单界面
 */
public class QQView {
    private boolean loop = true;
    private Scanner sc = new Scanner(System.in);
    private UserClientServer userClientServer = new UserClientServer();

    public void mainView(){
        while (loop){
            System.out.println("==================欢迎登录网络通信系统================");
            System.out.println("                  1 登陆系统");
            System.out.println("                  9 退出系统");
            System.out.println("请选择：");
            int select = sc.nextInt();
            switch (select) {
                case 1 -> {
                    System.out.println("请输入用户名：");
                    String userID = sc.next();
                    System.out.println("请输入密码:");
                    String passward = sc.next();
                    if (userClientServer.checkUser(userID,passward)) { // 如果合法
                        boolean loop1 = true;
                        while (loop1){
                            System.out.println("=========欢迎" + userID + "来到网络通讯二级菜单==========");
                            System.out.println("                1 显示在线用户列表");
                            System.out.println("                2 群发消息");
                            System.out.println("                3 私聊消息");
                            System.out.println("                4 发送文件");
                            System.out.println("                9 退出系统 ");
                            System.out.println("请选择：");
                            int select1 = sc.nextInt();
                            switch (select1){
                                case 1 -> {userClientServer.getOnlineFriend();}
                                case 2 -> {
                                    System.out.println("请输入你要发送的内容：");
                                    String content = sc.next();
                                    userClientServer.groupMessage(userID, content);
                                }
                                case 3 -> {
                                    System.out.println("请输入你要给谁发送消息:");
                                    String user = sc.next();
                                    userClientServer.privateChat(user);
                                }
                                case 4 -> {
                                    System.out.println("想给谁发送文件：");
                                    String user = sc.next();
                                    System.out.println("输入路径名:");
                                    String path = sc.next();
                                    userClientServer.sendFile(user,path);
                                }
                                case 9 -> {
                                    userClientServer.messageExit();
                                    System.exit(0);
                                    loop1 = false;
                                }
                            }
                        }
                    } else { // 不合法
                        System.out.println("========登陆失败，账号或密码错误==========");
                    }
                }
                case 9 -> loop = false;
            }
        }
    }

    public static void main(String[] args) {
        new QQView().mainView();
    }
}
