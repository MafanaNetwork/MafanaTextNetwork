package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafana.data.Server;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerLeave implements Listener {


    @EventHandler (priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event) {
        CompletableFuture.supplyAsync(() -> {
            List<AllowedRecipient> getAllowedRecipients = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getAllAllowedRecipients().join();
            for(AllowedRecipient allowedRecipient : getAllowedRecipients) {
                if(allowedRecipient.getPlayerUUID().equals(event.getPlayer().getUniqueId())) {
                    ProxyPlayer player = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(allowedRecipient.getUser())).join();
                    if(player != null) {
                        Server server = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getServerFromUUIDAsync(player.getServerID()).join();
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "]: " + ChatColor.RED + event.getPlayer().getName() + " has left " + server.getServerNickName());
                    }
                }
            }
            return null;
        });
    }
}
