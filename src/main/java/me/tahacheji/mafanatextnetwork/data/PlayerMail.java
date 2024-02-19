package me.tahacheji.mafanatextnetwork.data;

import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMail {

    private String fromUUID;
    private String toUUID;
    private String date;
    private boolean opened;
    private String message;
    private String mailUUID;
    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    public PlayerMail(String fromUUID, String toUUID, String date, boolean opened, String message, String mailUUID) {
        this.fromUUID = fromUUID;
        this.toUUID = toUUID;
        this.date = date;
        this.opened = opened;
        this.message = message;
        this.mailUUID = mailUUID;
    }

    public String getMailUUID() {
        return mailUUID;
    }

    public String getFromUUID() {
        return fromUUID;
    }

    public String getToUUID() {
        return toUUID;
    }

    public String getDate() {
        return date;
    }

    public boolean isOpened() {
        return opened;
    }

    public String getMessage() {
        return message;
    }

    public void setFromUUID(String fromUUID) {
        this.fromUUID = fromUUID;
    }

    public void setToUUID(String toUUID) {
        this.toUUID = toUUID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMailUUID(String mailUUID) {
        this.mailUUID = mailUUID;
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


    public LocalDateTime getLocalDateTime() {
        if (dtf == null) {
            dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        }

        String newTime = getDate();
        newTime = newTime.replace("[", "").replace("]", "");
        return LocalDateTime.parse(newTime, dtf);
    }

}
