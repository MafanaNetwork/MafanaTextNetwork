package me.tahacheji.mafanatextnetwork.data;

import java.util.UUID;

public class AllowedRecipient {

    private String user;
    private String playerName;
    private String playerDisplayName;
    private String playerUUID;

    public AllowedRecipient(String user, String playerName, String playerDisplayName, String playerUUID) {
        this.user = user;
        this.playerName = playerName;
        this.playerDisplayName = playerDisplayName;
        this.playerUUID = playerUUID;
    }

    public String getUser() {
        return user;
    }

    public String getPlayerDisplayName() {
        return playerDisplayName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(playerUUID);
    }
}
