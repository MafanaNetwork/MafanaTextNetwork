package me.tahacheji.mafanatextnetwork.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GamePlayerPublicMessaging {

    private final String sender;

    private final String time;

    private String text;


    public GamePlayerPublicMessaging(String sender, String time, String text) {
        this.sender = sender;
        this.time = time;
        this.text = text;
    }

    public GamePlayerPublicMessaging(String sender, String time) {
        this.sender = sender;
        this.time = time;
    }

    public UUID getSender() {
        return UUID.fromString(sender);
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
}
