package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.logs.PrivateLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.PublicLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.Recipient_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("MTNAdmin")) {
            Player player = (Player) sender;
            if(!player.isOp()) {
                return true;
            }
            if(args[0].equalsIgnoreCase("pubLog")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                try {
                    new PublicLog_GUI().getPublicMessageGUI(request, true, "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(args[0].equalsIgnoreCase("privLog")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                try {
                    new PrivateLog_GUI().getPrivateMessageGUI(request, true, "", "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(args[0].equalsIgnoreCase("recipients")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                try {
                    new Recipient_GUI().getAllowedRecipientGUI(request, "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(args[0].equalsIgnoreCase("troll")) {
                Player request = Bukkit.getPlayer(args[1]);
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                MafanaTextNetwork.getInstance().getMessageManager().sendMessage(request, message);
            }
            if(args[0].equalsIgnoreCase("clearPrivate")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearPrivateTextLogs(player);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: " + "cleared!");
            }
            if(args[0].equalsIgnoreCase("clearPublic")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearPublicTextLogs(player);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: " + "cleared!");
            }
            if(args[0].equalsIgnoreCase("clearRecipient")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearAllowedRecipients(player);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: " + "cleared!");
            }
            if(args[0].equalsIgnoreCase("clearTime")) {
                Player request = Bukkit.getPlayer(args[1]);
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearLastTimeText(player);
            }
            if(args[0].equalsIgnoreCase("msg")) {
                Player recipient = Bukkit.getPlayer(args[1]);
                if (recipient == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                if (!recipient.isOnline()) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_ONLINE");
                    return true;
                }
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                try {
                    MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, recipient, message);
                    player.sendMessage(ChatColor.DARK_GREEN + "[MTN] TO " + ChatColor.DARK_PURPLE + recipient.getName() + ": " + ChatColor.WHITE + message);
                    recipient.sendMessage(ChatColor.DARK_RED + "[MTN ADMIN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + ChatColor.WHITE + message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
                    recipient.sendMessage(ChatColor.DARK_RED + "[MTN ADMIN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + itemStack.getItemMeta().getDisplayName()+ ChatColor.WHITE +" " + message);
                } else {
                    try {
                        MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player, recipient, itemStack);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    player.setItemInHand(null);
                    recipient.getInventory().addItem(itemStack);
                    player.sendMessage(ChatColor.DARK_GREEN + "[MTN] TO " + ChatColor.DARK_PURPLE + recipient.getName() + ": " + itemStack.getItemMeta().getDisplayName());
                    recipient.sendMessage(ChatColor.DARK_RED + "[MTN ADMIN] FROM " + ChatColor.DARK_PURPLE + player.getName() + ": " + itemStack.getItemMeta().getDisplayName());
                }
                return true;
            }
        }
        return false;
    }
}
