package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafanatextnetwork.logs.PrivateLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.PublicLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.Recipient_GUI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MafanaTextNetworkCommand {

    @Command(names = {"mtn addRecipient", "mafanatext addRecipient", "mafanatextnetwork addRecipient"}, playerOnly = true)
    public void addRecipient(Player player, @Param(name = "recipient")OfflinePlayer target) {
        if(target == null) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
            return;
        }
        if(me.tahacheji.mafanatextnetwork.MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(player, target)) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_ALREADY_RECIPIENT");
            return;
        }
        me.tahacheji.mafanatextnetwork.MafanaTextNetwork.getInstance().getGamePlayerMessageData().addRecipient(player, target);
        player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_ADDED");
    }

    @Command(names = {"mtn removeRecipient", "mafanatext removeRecipient", "mafanatextnetwork removeRecipient"}, playerOnly = true)
    public void removeRecipient(Player player, @Param(name = "recipient")OfflinePlayer target) {
        if(target == null) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
            return;
        }
        if(!me.tahacheji.mafanatextnetwork.MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(player, target)) {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_IS_NOT_RECIPIENT");
            return;
        }
        me.tahacheji.mafanatextnetwork.MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeRecipient(player, target);
        player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_REMOVED");
    }

    @Command(names = {"mtn publicTextLog", "mafanatext publicTextLog", "mafanatextnetwork publicTextLog"}, playerOnly = true)
    public void publicTextLog(Player player) {
        try {
            new PublicLog_GUI().getPublicMessageGUI(player, true, "").open(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Command(names = {"mtn privateTextLog", "mafanatext privateTextLog", "mafanatextnetwork privateTextLog"}, playerOnly = true)
    public void privateTextLog(Player player) {
        try {
            new PrivateLog_GUI().getPrivateMessageGUI(player, true, "", "").open(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Command(names = {"mtn currentRecipients", "mafanatext currentRecipients", "mafanatextnetwork currentRecipients"}, playerOnly = true)
    public void currentRecipients(Player player) {
        try {
            new Recipient_GUI().getAllowedRecipientGUI(player, "").open(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
