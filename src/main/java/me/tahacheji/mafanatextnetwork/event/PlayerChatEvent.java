package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.MessageManager;
import me.tahacheji.mafanatextnetwork.data.MutedPlayer;
import me.tahacheji.mafanatextnetwork.util.CensorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
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
            String bubble = new CensorUtil().getBubble(MafanaTextNetwork.getInstance().getGamePlayerMessageData().getUserValues(player.getUniqueId()).join());
            List<MutedPlayer> mutedPlayers = MafanaTextNetwork.getInstance().getServerMessageData().getMutedPlayerList("SERVER").join();
            if (new CensorUtil().getMutedPlayer(player.getUniqueId(), mutedPlayers) == null) {
                if (!new CensorUtil().containsBlacklistedWord(message[0], blackListedWords)) {
                    String original = message[0];
                    message[0] = new CensorUtil().censorMessage(message[0], starredWords);
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPublicText(player.getUniqueId(), message[0]);

                    for (Player x : Bukkit.getOnlinePlayers()) {
                        List<String> a = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getUserValues(x.getUniqueId()).join();
                        String chat = new CensorUtil().getChat(a);
                        String starred = new CensorUtil().getStarred(a);
                        if(chat.equalsIgnoreCase("ALLOW_CHAT")) {
                            if(starred.equalsIgnoreCase("ALLOW_STARRED_MESSAGES")) {
                                x.sendMessage(player.getDisplayName() + ChatColor.WHITE + ": " + message[0]);
                            } else {
                                x.sendMessage(player.getDisplayName() + ChatColor.WHITE + ": " + original);
                            }
                        }
                    }
                    if (bubble.equalsIgnoreCase("ALLOW_TEXT_BUBBLE")) {
                        messageManager.sendMessage(player, message[0]);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Your message contains inappropriate language and cannot be sent.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are currently muted until " + new CensorUtil().getMutedPlayer(player.getUniqueId(), mutedPlayers).getEndDate() + ".");
            }

            return null;
        });
    }
}
