package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import me.tahacheji.mafanatextnetwork.data.PlayerMail;
import me.tahacheji.mafanatextnetwork.logs.PlayerMail_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerMailCommand {

    @Command(names = {"sendMail", "mail", "sm"}, playerOnly = true)
    public void sendMail(Player player, @Param(name = "player") String name, @Param(name = "message", concated = true) String message) {
        MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(name).thenAcceptAsync(offlineProxyPlayer -> {
            CompletableFuture<Boolean> isRecipientFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), player.getUniqueId());
            isRecipientFuture.thenAccept(isRecipient -> {
                if (!isRecipient && !player.hasPermission("mafana.admin")) {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
                    return;
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
                LocalDateTime now = LocalDateTime.now();
                String time = "[" + dtf.format(now) + "]";
                PlayerMail playerMail = new PlayerMail(player.getUniqueId().toString(), offlineProxyPlayer.getPlayerUUID(), time, false, message, UUID.randomUUID().toString());
                try {
                    CompletableFuture<Void> addMailFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().addMail(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), playerMail);
                    addMailFuture.thenAccept((Void) -> {
                        MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(offlineProxyPlayer.getPlayerUUID()))
                                .thenAcceptAsync(proxyPlayer -> {
                                    if (proxyPlayer != null) {
                                        proxyPlayer.sendMessage(ChatColor.YELLOW + "Received Mail From: " + player.getName());
                                    }
                                    player.sendMessage(ChatColor.GREEN + "Sent Mail!");
                                });
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Command(names = {"openMail", "om"}, playerOnly = true)
    public void openMail(Player player) {
        try {
            new PlayerMail_GUI().getPlayerMail(player.getUniqueId(), true, "", "", true, player).thenAccept(paginatedGui -> {
                Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> {
                    paginatedGui.open(player);
                });
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
