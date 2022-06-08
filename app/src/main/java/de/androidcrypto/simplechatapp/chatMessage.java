package de.androidcrypto.simplechatapp;

import java.util.Date;

public class chatMessage {
    private String MessageText;
    private String MessageUser;
    private long MessageTime;

    public chatMessage(String MessageText,String MessageUser)
    {
        this.MessageText = MessageText;
        this.MessageUser = MessageUser;

        MessageTime = new Date().getTime();
    }

    public chatMessage()
    {

    }

    public String getMessageText()
    {
        return MessageText;
    }

    public void setMessageText(String MessageText){
        this.MessageText = MessageText;
    }

    public String getMessageUser()
    {
        return MessageUser;
    }

    public void setMessageUser(String MessageUser){
        this.MessageUser = MessageUser;
    }

    public long getMessageTime(){
        return MessageTime;
    }

    public void setMessageTime(long MessageTime){
        this.MessageTime = MessageTime;
    }

}

