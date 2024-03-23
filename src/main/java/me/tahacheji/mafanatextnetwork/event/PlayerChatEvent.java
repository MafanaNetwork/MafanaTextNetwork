package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.MessageManager;
import me.tahacheji.mafanatextnetwork.util.CensorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerChatEvent implements Listener {


    private final MessageManager messageManager;

    public PlayerChatEvent(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        final String[] message = {event.getMessage()};
        event.setCancelled(true);
        CompletableFuture.supplyAsync(() -> {
            List<String> starredWords = MafanaTextNetwork.getInstance().getServerMessageData().getStaredOutWordList("SERVER").join();
            List<String> blackListedWords = MafanaTextNetwork.getInstance().getServerMessageData().getBlackListedWordList("SERVER").join();
            String z = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(player.getUniqueId()).join();
            List<String> mutedPlayers = MafanaTextNetwork.getInstance().getServerMessageData().getMutedPlayerList("SERVER").join();
            if (!mutedPlayers.contains(player.getUniqueId().toString())) {
                if (!new CensorUtil().containsBlacklistedWord(message[0], blackListedWords)) {
                    message[0] = new CensorUtil().censorMessage(message[0], starredWords);
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPublicText(player.getUniqueId(), message[0]);
                    if (!z.equalsIgnoreCase("1") && !z.equalsIgnoreCase("3")) {
                        messageManager.sendMessage(player, message[0]);
                    }

                    for (Player x : Bukkit.getOnlinePlayers()) {
                        CompletableFuture<String> s = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getTextValue(x.getUniqueId());
                        s.thenAcceptAsync(m -> {
                            if (!m.equalsIgnoreCase("2") && !m.equalsIgnoreCase("3")) {
                                x.sendMessage(player.getDisplayName() + ChatColor.WHITE + ": " + message[0]);
                            }
                        });
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Your message contains inappropriate language and cannot be sent.");
                }
            }

            return null;
        });
    }
}
