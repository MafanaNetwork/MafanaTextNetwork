package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class PlayerChatEvent implements Listener {


    private final MessageManager messageManager;

    public PlayerChatEvent(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setCancelled(true);
        CompletableFuture<String> z = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(player.getUniqueId());
        z.thenAccept(value -> {
            MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPublicText(player.getUniqueId(), message);
            if (!value.equalsIgnoreCase("1") && !value.equalsIgnoreCase("3")) {
                messageManager.sendMessage(player, message);
            }

            for(Player x : Bukkit.getOnlinePlayers()) {
                CompletableFuture<String> s = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(x.getUniqueId());
                s.thenAccept(m -> {
                    if(!m.equalsIgnoreCase("2") && !m.equalsIgnoreCase("3")) {
                        x.sendMessage(player.getDisplayName() + ChatColor.WHITE +  ": "  + message);
                    }
                });
            }
        });

    }
}
