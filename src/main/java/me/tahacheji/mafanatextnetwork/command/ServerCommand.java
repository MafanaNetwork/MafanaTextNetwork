package me.tahacheji.mafanatextnetwork.command;

import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ServerCommand {

    @Command(names = "mtn admin setServer", permission = "mafana.admin", playerOnly = true)
    public void setServer(Player player, @Param(name = "server") String server) {
        MafanaTextNetwork.getInstance().getServerMessageData().setServerIgnoreList(server).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin addBlackListedWord", permission = "mafana.admin", playerOnly = true)
    public void addBlackListedWord(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaTextNetwork.getInstance().getServerMessageData().addBlackListedWord(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin removeBlackListedWord", permission = "mafana.admin", playerOnly = true)
    public void removeBlackListedWord(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaTextNetwork.getInstance().getServerMessageData().removeBlackListedWord(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin addStarredWord", permission = "mafana.admin", playerOnly = true)
    public void addStarredWord(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaTextNetwork.getInstance().getServerMessageData().addStaredOutWord(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin removeStarredWord", permission = "mafana.admin", playerOnly = true)
    public void removeStarredWord(Player player,@Param(name = "server") String server, @Param(name = "id") String id) {
        MafanaTextNetwork.getInstance().getServerMessageData().removeStaredOutWord(server, id).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin addMutedPlayer", permission = "mafana.admin", playerOnly = true)
    public void addMutedPlayer(Player player,@Param(name = "server") String server, @Param(name = "id") OfflineProxyPlayer id) {
        MafanaTextNetwork.getInstance().getServerMessageData().addMutedPlayer(server, id.getPlayerUUID()).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

    @Command(names = "mtn admin removeMutedPlayer", permission = "mafana.admin", playerOnly = true)
    public void removeMutedPlayer(Player player,@Param(name = "server") String server, @Param(name = "id") OfflineProxyPlayer id) {
        MafanaTextNetwork.getInstance().getServerMessageData().removeMutedPlayer(server, id.getPlayerUUID()).thenRunAsync(() -> {
            player.sendMessage(ChatColor.GREEN + "Complete.");
        });
    }

}
