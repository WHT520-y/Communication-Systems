package com.whtedu.qqcommon;

/*
 *@author 文帅帅
 *@version 1.0
 */
public interface MessageType {
    public static final String MASSAGE_LOGIN_SUCCEED = "1"; // 登陆成功
    String MASSAGE_LOGIN_FALSE = "2"; // 登陆失败
    String MESSAGE_COMM_MES = "3"; // 普通的消息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; // 请求在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; // 返回在线列表
    String MESSAGE_CLIENT_EXIT = "6"; // 客户端请求退出
    String MESSAGE_GROUP = "7"; // 群发消息
    String MESSAGE_FILE = "8"; // 文件
    String MESSAGE_NEWS = "9"; // 新闻消息
}
