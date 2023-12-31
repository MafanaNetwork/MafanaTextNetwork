package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.PlayerMail;
import me.tahacheji.mafanatextnetwork.logs.PlayerMail_GUI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PlayerMailCommand {

    @Command(names = {"sendMail", "mail", "sm"}, playerOnly = true)
    public void sendMail(Player player, @Param(name = "player") OfflinePlayer target, @Param(name = "message", concated = true) String message) {
        ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayer(target);
        if(!MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
            return;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        PlayerMail playerMail = new PlayerMail(player.getUniqueId().toString(), target.getUniqueId().toString(), time, false, message,  UUID.randomUUID().toString());
        try {
            MafanaTextNetwork.getInstance().getGamePlayerMessageData().addMail(target.getUniqueId(), playerMail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(proxyPlayer != null) {
            proxyPlayer.sendMessage(ChatColor.YELLOW + "Received Mail From: " + player.getName());
        }
        player.sendMessage(ChatColor.GREEN + "Sent Mail!");
    }

    @Command(names = {"openMail", "om"}, playerOnly = true)
    public void openMail(Player player) {
        try {
            new PlayerMail_GUI().getPlayerMail(player, true, "", "", true).open(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
