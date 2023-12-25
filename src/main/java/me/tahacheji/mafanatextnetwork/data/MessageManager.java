package me.tahacheji.mafanatextnetwork.data;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MessageManager {

    private final List<ArmorStand> armorStandList = new ArrayList<>();
    private static final int MAX_ARMOR_STANDS = 5;
    private static final double DISTANCE_BETWEEN_MESSAGES = 0.2;

    public void sendMessage(Player sender, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < armorStandList.size(); i++) {
                    ArmorStand stand = armorStandList.get(i);
                    Location newLocation = stand.getLocation().add(0, DISTANCE_BETWEEN_MESSAGES + .2, 0);
                    stand.teleport(newLocation);
                }
                ArmorStand armorStand = spawnMessageArmorStand(sender, message);
                armorStandList.add(0, armorStand); // Add at the beginning of the list
                if (armorStandList.size() > MAX_ARMOR_STANDS) {
                    ArmorStand oldestArmorStand = armorStandList.remove(armorStandList.size() - 1);
                    if (oldestArmorStand != null) {
                        oldestArmorStand.remove();
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        armorStand.remove();
                        armorStandList.remove(armorStand);
                    }
                }.runTaskLater(MafanaTextNetwork.getInstance(), 100);
            }
        }.runTask(MafanaTextNetwork.getInstance());
    }

    private ArmorStand spawnMessageArmorStand(Player player, String message) {
        World world = player.getWorld();
        Location location = player.getLocation().clone().add(0, 0, 0);

        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    armorStand.remove();
                    armorStandList.remove(armorStand);
                    cancel();
                    return;
                }

                Location newLocation = player.getLocation().clone().add(0, armorStandList.indexOf(armorStand) * DISTANCE_BETWEEN_MESSAGES + .5, 0);
                armorStand.teleport(newLocation);
            }
        }.runTaskTimer(MafanaTextNetwork.getInstance(), 0, 0);

        return armorStand;
    }

}

