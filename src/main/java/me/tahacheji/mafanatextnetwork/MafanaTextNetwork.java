package me.tahacheji.mafanatextnetwork;

import me.tahacheji.mafana.commandExecutor.CommandHandler;
import me.tahacheji.mafanatextnetwork.command.*;
import me.tahacheji.mafanatextnetwork.data.GamePlayerMessageData;
import me.tahacheji.mafanatextnetwork.data.MessageManager;
import me.tahacheji.mafanatextnetwork.data.ServerMessageData;
import me.tahacheji.mafanatextnetwork.event.PlayerChatEvent;
import me.tahacheji.mafanatextnetwork.event.PlayerJoin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MafanaTextNetwork extends JavaPlugin{


    private static MafanaTextNetwork instance;
    private final GamePlayerMessageData gamePlayerMessageData = new GamePlayerMessageData();
    private final ServerMessageData serverMessageData = new ServerMessageData();

    private MessageManager messageManager;
    @Override
    public void onEnable() {
        instance = this;
        messageManager = new MessageManager();
        gamePlayerMessageData.connect();
        serverMessageData.connect();
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(messageManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        CommandHandler.registerCommands(PlayerChatToggle.class, this);
        CommandHandler.registerCommands(PrivateMessage.class, this);
        CommandHandler.registerCommands(PlayerMailCommand.class, this);
        CommandHandler.registerCommands(MafanaTextNetworkCommand.class, this);
        CommandHandler.registerCommands(MafanaTextNetworkAdminCommand.class, this);
        CommandHandler.registerCommands(ServerCommand.class, this);
    }

    @Override
    public void onDisable() {
        gamePlayerMessageData.close();
        serverMessageData.close();
    }

    public ServerMessageData getServerMessageData() {
        return serverMessageData;
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
