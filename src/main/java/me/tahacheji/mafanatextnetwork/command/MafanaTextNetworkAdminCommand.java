package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.ProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import me.tahacheji.mafanatextnetwork.data.PlayerMail;
import me.tahacheji.mafanatextnetwork.logs.PlayerMail_GUI;
import me.tahacheji.mafanatextnetwork.logs.PrivateLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.PublicLog_GUI;
import me.tahacheji.mafanatextnetwork.logs.Recipient_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MafanaTextNetworkAdminCommand {


    @Command(names = {"mtn admin view publicLog", "mafanatext admin view publicLog", "mafanatextnetwork admin view publicLog"}, permission = "mafana.admin", playerOnly = true)
    public void viewPublicLog(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        if (offlineProxyPlayer != null) {
            try {
                new PublicLog_GUI().getPublicMessageGUI(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), true, "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Command(names = {"mtn admin view privateLog", "mafanatext admin view privateLog", "mafanatextnetwork admin view privateLog"}, permission = "mafana.admin", playerOnly = true)
    public void viewPrivateLog(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        if (offlineProxyPlayer != null) {
            try {
                new PrivateLog_GUI().getPrivateMessageGUI(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), true, "", "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Command(names = {"mtn admin view recipients", "mafanatext admin view recipients", "mafanatextnetwork admin view recipients"}, permission = "mafana.admin", playerOnly = true)
    public void viewRecipients(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        if (offlineProxyPlayer != null) {
            try {
                new Recipient_GUI().getAllowedRecipientGUI(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), "", player).thenAccept(paginatedGui -> Bukkit.getScheduler().runTask(MafanaTextNetwork.getInstance(), () -> paginatedGui.open(player)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Command(names = {"mtn admin view mail", "mafanatext admin view mail", "mafanatextnetwork admin view mail"}, permission = "mafana.admin", playerOnly = true)
    public void viewMail(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        if (offlineProxyPlayer != null) {
            try {
                new PlayerMail_GUI().getPlayerMail(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), true, "", "", true, player).thenAccept(paginatedGui -> {
                    paginatedGui.open(player);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Command(names = {"mtn admin troll", "mafanatext admin troll", "mafanatextnetwork admin troll"}, permission = "mafana.admin")
    public void trollTextPlayer(@Param(name = "letTheSenderSee") boolean letTheSenderSee, @Param(name = "sender") OfflineProxyPlayer sender, @Param(name = "receiver") OfflineProxyPlayer receiver, @Param(name = "message", concated = true) String message) {
        try {
            if (letTheSenderSee) {
                MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(sender.getPlayerUUID())).thenAcceptAsync(proxyPlayer -> {
                    proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] TO " + ChatColor.GOLD + receiver.getPlayerName() + ": " + ChatColor.WHITE + message);
                });
            }
            MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(sender.getPlayerUUID())).thenAcceptAsync(proxyPlayer -> {
                proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] FROM " + ChatColor.GOLD + sender.getPlayerName() + ": " + ChatColor.WHITE + message);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command(names = {"mtn admin clear privateLogs", "mafanatext admin clear privateLogs", "mafanatextnetwork admin clear privateLogs"}, permission = "mafana.admin")
    public void clearPrivateLogs(@Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearPrivateTextLogs(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
    }

    @Command(names = {"mtn admin clear publicLogs", "mafanatext admin clear publicLogs", "mafanatextnetwork admin clear publicLogs"}, permission = "mafana.admin")
    public void clearPublicLogs(@Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearPublicTextLogs(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
    }

    @Command(names = {"mtn admin clear recipients", "mafanatext admin clear recipients", "mafanatextnetwork admin clear recipients"}, permission = "mafana.admin")
    public void clearRecipients(@Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearAllowedRecipients(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
    }

    @Command(names = {"mtn admin clear time", "mafanatext admin clear time", "mafanatextnetwork admin clear time"}, permission = "mafana.admin")
    public void clearTime(@Param(name = "target") OfflineProxyPlayer offlineProxyPlayer) {
        MafanaTextNetwork.getInstance().getGamePlayerMessageData().clearLastTimeText(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
    }

    @Command(names = {"mtn admin msg", "mafanatext admin msg", "mafanatextnetwork admin msg"}, permission = "mafana.admin")
    public void adminMessage(CommandSender commandSender, @Param(name = "letTheSenderSee") boolean letThemSee, @Param(name = "target") OfflineProxyPlayer target, @Param(name = "message", concated = true) String message) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (letThemSee) {
                MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(target.getPlayerUUID())).thenAcceptAsync(proxyPlayer -> {
                    proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "] FROM " + ChatColor.RED + player.getName() + ": " + ChatColor.WHITE + message);
                });
            } else {
                MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(target.getPlayerUUID())).thenAcceptAsync(proxyPlayer -> {
                    proxyPlayer.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "MTN" + ChatColor.DARK_GRAY + "]: " + ChatColor.WHITE + message);
                });
            }
        }
    }

    @Command(names = {"mtn admin mail", "mafanatext admin mail", "mafanatextnetwork admin mail"}, permission = "mafana.admin", playerOnly = true)
    public void adminMail(Player player, @Param(name = "target") OfflineProxyPlayer offlineProxyPlayer, @Param(name = "message", concated = true) String message) {
        CompletableFuture<ProxyPlayer> proxyPlayerFuture = MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(UUID.fromString(offlineProxyPlayer.getPlayerUUID()));
        proxyPlayerFuture.thenAccept(proxyPlayer -> {
            if (proxyPlayer == null) {
                player.sendMessage(ChatColor.RED + "MafanaTextNetwork: PLAYER_NOT_FOUND");
                return;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
            LocalDateTime now = LocalDateTime.now();
            String time = "[" + dtf.format(now) + "]";
            PlayerMail playerMail = new PlayerMail(player.getUniqueId().toString(), offlineProxyPlayer.getPlayerUUID().toString(), time, false, message, UUID.randomUUID().toString());
            try {
                CompletableFuture<Void> addMailFuture = MafanaTextNetwork.getInstance().getGamePlayerMessageData().addMail(UUID.fromString(offlineProxyPlayer.getPlayerUUID()), playerMail);
                addMailFuture.thenRun(() -> {
                    proxyPlayer.sendMessage(ChatColor.YELLOW + "Received Mail From: " + player.getName());
                    player.sendMessage(ChatColor.GREEN + "Sent Mail!");
                }).exceptionally(ex -> {
                    player.sendMessage(ChatColor.RED + "MafanaTextNetwork: Error occurred while sending mail");
                    ex.printStackTrace();
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "MafanaTextNetwork: Error occurred while fetching player");
            ex.printStackTrace();
            return null;
        });
    }

}
