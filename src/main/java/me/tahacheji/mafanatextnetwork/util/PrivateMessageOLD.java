package me.tahacheji.mafanatextnetwork.util;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;

public class PrivateMessageOLD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("pm")) {
            Player player = (Player) sender;
            if (args.length <= 0) {
                player.sendMessage(ChatColor.RED + "MafanaTextNetwork: /pm [name] {message}");
                return true;
            }
            if(args[0].equalsIgnoreCase("item")) {
                Player recipient = Bukkit.getPlayer(args[1]);
                if (recipient == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                if (!recipient.isOnline()) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_ONLINE");
                    return true;
                }
                if (!MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(recipient, player)) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
                    return true;
                }
                ItemStack itemStack = player.getItemInHand();
                if(itemStack == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: ITEM_NULL");
                    return true;
                }
                if(itemStack.getItemMeta() == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: ITEM_NULL");
                    return true;
                }
                if(args.length != 2) {
                    String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    try {
                        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, recipient, message, itemStack);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    player.setItemInHand(null);
                    recipient.getInventory().addItem(itemStack);

                    player.sendMessage(ChatColor.DARK_GREEN + "[MTN] TO " + ChatColor.DARK_PURPLE + recipient.getName() + ": " + itemStack.getItemMeta().getDisplayName() + ChatColor.WHITE +" " + message);
                    recipient.sendMessage(ChatColor.DARK_GREEN + "[MTN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + itemStack.getItemMeta().getDisplayName()+ ChatColor.WHITE +" " + message);
                } else {
                    try {
                        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, recipient, itemStack);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    player.setItemInHand(null);
                    recipient.getInventory().addItem(itemStack);
                    player.sendMessage(ChatColor.DARK_GREEN + "[MTN] TO " + ChatColor.DARK_PURPLE + recipient.getName() + ": " + itemStack.getItemMeta().getDisplayName());
                    recipient.sendMessage(ChatColor.DARK_GREEN + "[MTN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + itemStack.getItemMeta().getDisplayName());
                }
                return true;
            }
            OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[0]);
            //if (recipient == null) {
                //player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                //return true;
            //}
            //if (!recipient.isOnline()) {
                //player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_ONLINE");
                //return true;
            //}
            //if (!MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(recipient, player)) {
                //player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
                //return true;
            //}

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            try {
                //MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, recipient, message);


                //mesaging player
                ByteArrayOutputStream x = new ByteArrayOutputStream();
                DataOutput m = new DataOutputStream(x);
                try {
                    m.writeUTF("Message");
                    m.writeUTF(recipient.getName());
                    m.writeUTF(ChatColor.DARK_GREEN + "[MTN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + ChatColor.WHITE + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //player.getServer().sendPluginMessage(MafanaTextNetwork.getInstance(), "BungeeCord", z.toByteArray());


                player.sendMessage(ChatColor.DARK_GREEN + "[MTN] TO " + ChatColor.DARK_PURPLE + recipient.getName() + ": " + ChatColor.WHITE + message);
                //recipient.sendMessage(ChatColor.DARK_GREEN + "[MTN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + ChatColor.WHITE + message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }


}
