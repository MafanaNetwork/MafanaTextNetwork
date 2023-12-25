package me.tahacheji.mafanatextnetwork.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GamePlayerPrivateMessaging {

    private final OfflinePlayer sender;
    private final OfflinePlayer receiver;

    private final String time;

    private String text;

    private ItemStack item;


    public GamePlayerPrivateMessaging(OfflinePlayer sender, OfflinePlayer receiver, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public GamePlayerPrivateMessaging(OfflinePlayer sender, OfflinePlayer receiver, String time, String text) {
        this(sender, receiver, time);
        this.text = text;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OfflinePlayer getSender() {
        return sender;
    }

    public OfflinePlayer getReceiver() {
        return receiver;
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
        return item;
    }
}
