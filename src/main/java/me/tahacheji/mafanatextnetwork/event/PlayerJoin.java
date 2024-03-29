package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafana.data.Server;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerJoin implements Listener {


    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        MafanaTextNetwork.getInstance().getServerMessageData().onJoinMutedPlayer("SERVER");
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPlayer(event.getPlayer(),
                List.of("ALLOW_CHAT", "ALLOW_TEXT_BUBBLE", "ALLOW_STARRED_MESSAGES"));
        CompletableFuture.supplyAsync(() -> {
            List<AllowedRecipient> getAllowedRecipients = MafanaTextNetwork.getInstance().getGamePlayerMessageData().getAllAllowedRecipients().join();
            for(AllowedRecipient allowedRecipient : getAllowedRecipients) {
                if(allowedRecipient.getPlayerUUID().equals(event.getPlayer().getUniqueId())) {
                    ProxyPlayer player = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(allowedRecipient.getUser())).join();
                    if(player != null) {
                        Server server = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getServerFromUUIDAsync(player.getServerID()).join();
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "]: " + ChatColor.GREEN + event.getPlayer().getName() + " has joined " + server.getServerNickName());
                    }
                }
            }
            return null;
        });
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();


            if (armorStand.isInvulnerable()) {
                event.setCancelled(true);
            }
        }
    }
}
