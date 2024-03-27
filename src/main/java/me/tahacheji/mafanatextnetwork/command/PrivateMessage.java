package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.AllowedRecipient;
import me.tahacheji.mafanatextnetwork.data.LastSender;
import me.tahacheji.mafanatextnetwork.data.MutedPlayer;
import me.tahacheji.mafanatextnetwork.util.CensorUtil;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PrivateMessage {

    @Command(names = {"pm", "private", "message", "privatemessage"}, playerOnly = true)
    public void sendPrivateMessage(Player player, @Param(name = "player") String target, @Param(name = "message", concated = true) String message) {
        CompletableFuture<ProxyPlayer> proxyPlayerFuture = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(target);
        MafanaTextNetwork.getInstance().getServerMessageData().getMutedPlayerList("SERVER").thenAcceptAsync(m -> {
            if (new CensorUtil().getMutedPlayer(player.getUniqueId(), m) == null) {
                proxyPlayerFuture.thenAccept(proxyPlayer -> {
                    if (proxyPlayer == null) {
                        player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND::PLAYER_NOT_ONLINE");
                        return;
                    }
                    CompletableFuture<Boolean> isRecipientFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().isRecipient(UUID.fromString(proxyPlayer.getPlayerUUID()), player.getUniqueId());
                    isRecipientFuture.thenAccept(isRecipient -> {
                        if (!isRecipient && !player.hasPermission("mafana.admin")) {
                            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: CANNOT_MESSAGE_PLAYER_NOT_RECIPIENT");
                            return;
                        }
                        try {
                            CompletableFuture<Void> addPrivateTextFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player.getUniqueId(), UUID.fromString(proxyPlayer.getPlayerUUID()), message);
                            addPrivateTextFuture.thenAccept((Void) -> MafanaTextNetwork.getInstance().getGamePlayerMessageData().setLastSender(UUID.fromString(proxyPlayer.getPlayerUUID()), new LastSender(player.getUniqueId().toString(), proxyPlayer.getPlayerUUID().toString())).thenRunAsync(() -> {
                                proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] FROM " + ChatColor.GOLD + player.getName() + ": " + ChatColor.WHITE + message);
                                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] TO " + ChatColor.GOLD + proxyPlayer.getPlayerName() + ": " + ChatColor.WHITE + message);
                            })).exceptionally(ex -> {
                                ex.printStackTrace();
                                return null;
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        });
    }

    @Command(names = {"reply"}, playerOnly = true)
    public void replyToLastMessage(Player player, @Param(name = "message", concated = true) String message) {
        MafanaTextNetwork.getInstance().getServerMessageData().getMutedPlayerList("SERVER").thenAcceptAsync(m -> {
            if (new CensorUtil().getMutedPlayer(player.getUniqueId(), m) == null) {
                MafanaTextNetwork.getInstance().getGamePlayerMessageData().getLastSender(player.getUniqueId()).thenAcceptAsync(lastSender -> {
                    if (lastSender != null) {
                        CompletableFuture<ProxyPlayer> senderFuture = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(lastSender.getSender()));
                        senderFuture.thenAccept(sender -> {
                            if (sender == null) {
                                player.sendMessage(ChatColor.RED + "The sender of the last message is no longer online.");
                                MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearLastSender(player.getUniqueId());
                                return;
                            }

                            CompletableFuture<Void> addPrivateTextFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().addPrivateText(player.getUniqueId(), UUID.fromString(lastSender.getSender()), message);
                            addPrivateTextFuture.thenAccept((Void) -> {
                                sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] FROM " + ChatColor.GOLD + player.getName() + ": " + ChatColor.WHITE + message);
                                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] TO " + ChatColor.GOLD + sender.getPlayerName() + ": " + ChatColor.WHITE + message);
                            }).exceptionally(ex -> {
                                ex.printStackTrace();
                                return null;
                            });
                        });
                    } else {
                        player.sendMessage(ChatColor.RED + "You have not received any messages to reply to.");
                    }
                });
            }
        });
    }


}
