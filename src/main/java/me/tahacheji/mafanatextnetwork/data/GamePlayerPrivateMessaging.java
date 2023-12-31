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


    public GamePlayerPrivateMessaging(String sender, String receiver, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public GamePlayerPrivateMessaging(String sender, String receiver, String time, String text) {
        this(sender, receiver, time);
        this.text = text;
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

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    public String getTime() {
        return time;
    }

    public String getLocalDateTime() {
        String newTime = time;
        newTime = newTime.replace("[", "");
        newTime = newTime.replace("]", "");
        LocalDateTime parsedTime = LocalDateTime.parse(newTime, dtf);
        return dtf.format(parsedTime);
    }

    public String getText() {
        return text;
    }

    public ItemStack getItem() {
        try {
            return new EncryptionUtil().itemFromBase64(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
