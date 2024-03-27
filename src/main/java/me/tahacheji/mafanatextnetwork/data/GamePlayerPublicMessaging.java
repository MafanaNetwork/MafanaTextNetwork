package me.tahacheji.mafanatextnetwork.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GamePlayerPublicMessaging {

    private final String sender;

    private final String time;

    private final String text;

    private final String senderServerID;


    public GamePlayerPublicMessaging(String sender, String time, String text, String senderServer) {
        this.sender = sender;
        this.time = time;
        this.text = text;
        this.senderServerID = senderServer;
    }


    public UUID getSender() {
        return UUID.fromString(sender);
    }

    public String getDate() {
        return time;
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

    public String getSenderServerID() {
        return senderServerID;
    }

    public String getText() {
        return text;
    }
}
