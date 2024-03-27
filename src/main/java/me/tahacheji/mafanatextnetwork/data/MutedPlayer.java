package me.tahacheji.mafanatextnetwork.data;

public class MutedPlayer {

    private String user;
    private String endDate;

    public MutedPlayer(String user, String endDate) {
        this.user = user;
        this.endDate = endDate;
    }

    public String getUser() {
        return user;
    }

    public String getEndDate() {
        return endDate;
    }
}
