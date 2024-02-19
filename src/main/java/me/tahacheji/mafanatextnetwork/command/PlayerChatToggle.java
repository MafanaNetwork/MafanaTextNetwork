package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerChatToggle {

    @Command(names = "toggleChat", playerOnly = true, permission = "")
    public void toggleChat(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CompletableFuture<String> s = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(player.getUniqueId());
            s.thenAcceptAsync(value -> {
                if (value.equalsIgnoreCase("2")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "0");
                    player.sendMessage(ChatColor.GREEN + "Toggle Chat: " + ChatColor.RED + "OFF");
                    //set it to 0
                } else if (value.equalsIgnoreCase("3")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "1");
                    player.sendMessage(ChatColor.GREEN + "Toggle Chat: " + ChatColor.RED + "OFF");
                    //set to 1
                } else if (value.equalsIgnoreCase("1")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "3");
                    player.sendMessage(ChatColor.GREEN + "Toggle Chat: " + ChatColor.GREEN + "ON");
                } else {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "2");
                    player.sendMessage(ChatColor.GREEN + "Toggle Chat: " + ChatColor.GREEN + "ON");
                }
            });
        }
    }

    @Command(names = "toggleBubble", playerOnly = true, permission = "")
    public void toggleBubble(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CompletableFuture<String> s = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(player.getUniqueId());
            s.thenAcceptAsync(value -> {
                if (value.equalsIgnoreCase("1")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "0");
                    player.sendMessage(ChatColor.GREEN + "Toggle Bubble: " + ChatColor.RED + "OFF");
                    //set it to 0
                } else if (value.equalsIgnoreCase("3")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "2");
                    player.sendMessage(ChatColor.GREEN + "Toggle Bubble: " + ChatColor.RED + "OFF");
                    //set to 2
                } else if (value.equalsIgnoreCase("2")) {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "3");
                    player.sendMessage(ChatColor.GREEN + "Toggle Bubble: " + ChatColor.GREEN + "ON");
                } else {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().setTextValue(player.getUniqueId(), "1");
                    player.sendMessage(ChatColor.GREEN + "Toggle Bubble: " + ChatColor.GREEN + "ON");
                    //set it to 1
                }
            });
        }
    }
}
