package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.util.CensorUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerChatToggle {

    @Command(names = "toggleChat", playerOnly = true, permission = "")
    public void toggleChat(Player player) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().getUserValues(player.getUniqueId()).thenAcceptAsync(x -> {
           String c = new CensorUtil().getChat(x);
           if(c.equalsIgnoreCase("ALLOW_CHAT")) {
               MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "ALLOW_CHAT", "DECLINE_CHAT").thenRunAsync(() -> {
                  player.sendMessage(ChatColor.RED + "Chat Toggled Off");
               });
           } else {
               MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "DECLINE_CHAT", "ALLOW_CHAT").thenRunAsync(() -> {
                   player.sendMessage(ChatColor.GREEN + "Chat Toggled On");
               });
           }
        });
    }

    @Command(names = "toggleStarred", playerOnly = true, permission = "")
    public void toggleStarred(Player player) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().getUserValues(player.getUniqueId()).thenAcceptAsync(x -> {
            String c = new CensorUtil().getStarred(x);
            if(c.equalsIgnoreCase("ALLOW_STARRED_MESSAGES")) {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "ALLOW_STARRED_MESSAGES", "DECLINE_STARRED_MESSAGES").thenRunAsync(() -> {
                    player.sendMessage(ChatColor.RED + "Starred Messaged Toggled Off");
                });
            } else {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "DECLINE_STARRED_MESSAGES", "ALLOW_STARRED_MESSAGES").thenRunAsync(() -> {
                    player.sendMessage(ChatColor.GREEN + "Starred Messaged Toggled On");
                });
            }
        });
    }

    @Command(names = "toggleBubble", playerOnly = true, permission = "")
    public void toggleBubble(Player player) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().getUserValues(player.getUniqueId()).thenAcceptAsync(x -> {
            String c = new CensorUtil().getBubble(x);
            if(c.equalsIgnoreCase("ALLOW_TEXT_BUBBLE")) {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "ALLOW_TEXT_BUBBLE", "DECLINE_TEXT_BUBBLE").thenRunAsync(() -> {
                    player.sendMessage(ChatColor.RED + "Text Bubble Toggled Off");
                });
            } else {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().replaceUserValue(player.getUniqueId(), "DECLINE_TEXT_BUBBLE", "ALLOW_TEXT_BUBBLE").thenRunAsync(() -> {
                    player.sendMessage(ChatColor.GREEN + "Text Bubble Toggled On");
                });
            }
        });
    }
}
