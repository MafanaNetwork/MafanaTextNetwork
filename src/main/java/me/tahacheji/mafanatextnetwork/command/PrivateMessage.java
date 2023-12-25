package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;

public class PrivateMessage {

    @Command(names = {"pm", "private", "message", "privatemessage"}, playerOnly = true)
    public void sendPrivateMessage(Player player, @Param(name = "player") OfflinePlayer target, @Param(name = "message", concated = true) String message) {
        ProxyPlayer proxyPlayer = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayer(target);
        if (proxyPlayer == null) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND::PLAYER_NOT_ONLINE");
            return;
        }
        if(!MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(target, player)) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
            return;
        }
        try {
            MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, target, message);
            proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + net.md_5.bungee.api.ChatColor.of(new Color(203, 161, 53)) + "MTN" + ChatColor.DARK_GRAY + "] FROM " + net.md_5.bungee.api.ChatColor.of(new Color(170, 136, 34)) + player.getName() + ": " + ChatColor.WHITE + message);
            player.sendMessage(ChatColor.DARK_GRAY + "[" + net.md_5.bungee.api.ChatColor.of(new Color(203, 161, 53)) + "MTN" + ChatColor.DARK_GRAY + "] TO " + net.md_5.bungee.api.ChatColor.of(new Color(170, 136, 34)) + target.getName() + ": " + ChatColor.WHITE + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
