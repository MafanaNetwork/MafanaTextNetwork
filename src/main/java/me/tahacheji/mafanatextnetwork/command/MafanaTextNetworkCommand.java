package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import me.tahacheji.mafanatextnetwork.logs.PrivateLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.PublicLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.Recipient_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MafanaTextNetworkCommand {

    @Command(names = {"mtn addRecipient", "mafanatext addRecipient", "mafanatextnetwork addRecipient"}, playerOnly = true)
    public void addRecipient(Player player, @Param(name = "recipient") OfflinePlayer target) {
        CompletableFuture<Void> recipientFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().addRecipient(player.getUniqueId(), target.getUniqueId());
        recipientFuture.thenRun(() -> player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_ADDED"))
                .exceptionally(ex -> {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: Error occurred while adding player as recipient");
                    ex.printStackTrace();
                    return null;
                });
    }

    @Command(names = {"mtn removeRecipient", "mafanatext removeRecipient", "mafanatextnetwork removeRecipient"}, playerOnly = true)
    public void removeRecipient(Player player, @Param(name = "recipient") AllowedRecipient target) {
        CompletableFuture<Void> recipientFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().removeRecipient(player.getUniqueId(), target.getPlayerUUID());
        recipientFuture.thenRun(() -> player.sendMessage(ChatColor.GREEN + "MafanaTextNetwork: PLAYER_REMOVED"))
                .exceptionally(ex -> {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: Error occurred while removing player as recipient");
                    ex.printStackTrace();
                    return null;
                });
    }


    @Command(names = {"mtn publicTextLog", "mafanatext publicTextLog", "mafanatextnetwork publicTextLog"}, playerOnly = true)
    public void publicTextLog(Player player) {
        try {
            new PublicLog_GUI().getPublicMessageGUI(player.getUniqueId(), true, "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Command(names = {"mtn privateTextLog", "mafanatext privateTextLog", "mafanatextnetwork privateTextLog"}, playerOnly = true)
    public void privateTextLog(Player player) {
        try {
            new PrivateLog_GUI().getPrivateMessageGUI(player.getUniqueId(), true, "", "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Command(names = {"mtn currentRecipients", "mafanatext currentRecipients", "mafanatextnetwork currentRecipients"}, playerOnly = true)
    public void currentRecipients(Player player) {
        try {
            new Recipient_GUI().getAllowedRecipientGUI(player.getUniqueId(), "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
