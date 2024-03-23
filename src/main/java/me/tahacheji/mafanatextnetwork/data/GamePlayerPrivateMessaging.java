package me.tahacheji.mafanatextnetwork.data;

import me.tahacheji.mafanatextnetwork.util.EncryptionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GamePlayerPrivateMessaging {

    private final String sender;

    private final String receiver;

    private final String time;

    private String text;

    private String item;

    private String serverSenderID;
    private String serverReceiverID;


    public GamePlayerPrivateMessaging(String sender, String receiver, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public GamePlayerPrivateMessaging(String sender, String receiver, String time, String text, String serverSenderID, String serverReceiverID) {
        this(sender, receiver, time);
        this.text = text;
        this.serverSenderID = serverSenderID;
        this.serverReceiverID = serverReceiverID;
    }

    public void setItem(ItemStack item) {
        this.item = new EncryptionUtil().itemToBase64(item);
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getSender() {
        return UUID.fromString(sender);
    }

    public UUID getReceiver() {
        return UUID.fromString(receiver);
    }

    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    public String getDate() {
        return time;
    }

    public String getLocalDateTime() {
        if (dtf == null) {
            dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        }
        String newTime = time;
        newTime = newTime.replace("[", "");
        newTime = newTime.replace("]", "");
        LocalDateTime parsedTime = LocalDateTime.parse(newTime, dtf);
        return dtf.format(parsedTime);
    }

    public int getTime() {
        String newTime = getDate();
        String[] components = newTime.replaceAll("[\\[\\]]", "").split("[ /:]");
        if (components.length >= 6) {
            int month = Integer.parseInt(components[0]);
            int day = Integer.parseInt(components[1]);
            int year = Integer.parseInt(components[2]);
            int hour = Integer.parseInt(components[3]);
            int minute = Integer.parseInt(components[4]);
            String amPm = components[5];
            int time = year * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute;
            if (amPm.equalsIgnoreCase("PM")) {
                hour += 12;
                hour %= 24;
            }
            time += hour * 10000;
            return time;
        } else {
            System.err.println("Invalid date format: " + newTime);
            return 0;
        }
    }

    public String getServerReceiverID() {
        return serverReceiverID;
    }

    public String getServerSenderID() {
        return serverSenderID;
    }

    public String getText() {
        return text;
    }

    public ItemStack getItem() {
        try {
            return new EncryptionUtil().itemFromBase64(item);
        } catch (Exception ignore) {}
        return null;
    }
}
