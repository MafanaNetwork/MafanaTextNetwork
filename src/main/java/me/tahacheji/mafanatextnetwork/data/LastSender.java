package me.tahacheji.mafanatextnetwork.data;

public class LastSender {

    private String sender;
    private String receiver;

    public LastSender(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}
