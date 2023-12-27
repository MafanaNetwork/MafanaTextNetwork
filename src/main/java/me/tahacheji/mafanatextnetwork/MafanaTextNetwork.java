package me.tahacheji.mafanatextnetwork;

import me.tahacheji.mafana.commandExecutor.CommandHandler;
import me.tahacheji.mafanatextnetwork.command.*;
import me.tahacheji.mafanatextnetwork.data.GamePlayerMessageData;
import me.tahacheji.mafanatextnetwork.data.MessageManager;
import me.tahacheji.mafanatextnetwork.event.PlayerChatEvent;
import me.tahacheji.mafanatextnetwork.event.PlayerJoin;
import me.tahacheji.mafanatextnetwork.util.MTNCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MafanaTextNetwork extends JavaPlugin{


    private static MafanaTextNetwork instance;
    private final GamePlayerMessageData gamePlayerMessageData = new GamePlayerMessageData();

    private MessageManager messageManager;
    @Override
    public void onEnable() {
        instance = this;
        messageManager = new MessageManager();
        gamePlayerMessageData.connect();
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(messageManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        CommandHandler.registerCommands(PlayerChatToggle.class, this);
        CommandHandler.registerCommands(PrivateMessage.class, this);
        CommandHandler.registerCommands(PlayerMailCommand.class, this);
        CommandHandler.registerCommands(MafanaTextNetworkCommand.class, this);
        getCommand("MTNAdmin").setExecutor(new AdminCommand());

    }

    @Override
    public void onDisable() {
        gamePlayerMessageData.disconnect();
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static MafanaTextNetwork getInstance() {
        return instance;
    }

    public GamePlayerMessageData getGamePlayerMessageData() {
        return gamePlayerMessageData;
    }

}
