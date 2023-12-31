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

public class PlayerChatEvent implements Listener {


    private final MessageManager messageManager;

    public PlayerChatEvent(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws IOException {
        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setCancelled(true);
        String value = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(player.getUniqueId());
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPublicText(player.getUniqueId(), message);
        if (!value.equalsIgnoreCase("1") && !value.equalsIgnoreCase("3")) {
            messageManager.sendMessage(player, message);
        }

        for(Player x : Bukkit.getOnlinePlayers()) {
            String m = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(x.getUniqueId());
            if(!m.equalsIgnoreCase("2") && !m.equalsIgnoreCase("3")) {
                x.sendMessage(player.getDisplayName() + ChatColor.WHITE +  ": "  + message);
            }
        }

    }
}
