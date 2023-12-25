package me.tahacheji.mafanatextnetwork.util;

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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MTNCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("MTN")) {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage(ChatColor.RED + "MafanaTextNetwork: /MTN [add/pubLog/privLog/recipient] {player_name}");
                return true;
            }
            if(args[0].equalsIgnoreCase("add")) {
                Player recipient = Bukkit.getPlayer(args[1]);
                if(recipient == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                if(MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(player, recipient)) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_ALREADY_RECIPIENT");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().addRecipient(player, recipient);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_ADDED");
            }
            if(args[0].equalsIgnoreCase("remove")) {
                Player recipient = Bukkit.getPlayer(args[1]);
                if(recipient == null) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                    return true;
                }
                if(!MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(player, recipient)) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_IS_NOT_RECIPIENT");
                    return true;
                }
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeRecipient(player, recipient);
                player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_REMOVED");
            }
            if(args[0].equalsIgnoreCase("pubLog")) {
                try {
                    new PublicLog_GUI().getPublicMessageGUI(player, true, "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(args[0].equalsIgnoreCase("privLog")) {
                try {
                    new PrivateLog_GUI().getPrivateMessageGUI(player, true, "", "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(args[0].equalsIgnoreCase("recipients")) {
                try {
                    new Recipient_GUI().getAllowedRecipientGUI(player, "").open(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
}
