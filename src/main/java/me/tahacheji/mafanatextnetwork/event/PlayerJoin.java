package me.tahacheji.mafanatextnetwork.event;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {


    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();

            // Check if the armor stand should be invulnerable
            if (armorStand.isInvulnerable()) {
                event.setCancelled(true); // Cancel the damage event
            }
        }
    }
}
