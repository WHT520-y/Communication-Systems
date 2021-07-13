package com.whtedu.qqcommon;

import java.io.Serializable;

/*
 *@author 文帅帅
 *@version 1.0
 * 用户的消息
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content; // 消息内容
    private String sender; // 发送者
    private String receiver; // 接收者
    private String sendTime; // 发送时间
    private String mesType;// 消息类型，因为会发送视频音频等
    private byte[] bytes;  // 专门处理二进制文件

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
