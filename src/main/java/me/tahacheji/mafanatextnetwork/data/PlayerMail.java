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

    public String getLocalDateTime() {
        if (dtf == null) {
            dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        }

        String newTime = getDate();
        newTime = newTime.replace("[", "");
        newTime = newTime.replace("]", "");
        LocalDateTime parsedTime = LocalDateTime.parse(newTime, dtf);
        return dtf.format(parsedTime);
    }


}
