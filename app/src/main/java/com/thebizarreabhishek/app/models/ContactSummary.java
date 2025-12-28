package com.thebizarreabhishek.app.models;

public class ContactSummary {
    private String senderName;
    private String lastMessage;
    private String lastTimestamp;
    private int messageCount;

    public ContactSummary(String senderName, String lastMessage, String lastTimestamp, int messageCount) {
        this.senderName = senderName;
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
        this.messageCount = messageCount;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public int getMessageCount() {
        return messageCount;
    }
}
