package me.tahacheji.mafanatextnetwork.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafanatextnetwork.util.EncryptionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GamePlayerMessageData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public GamePlayerMessageData() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public void addPlayer(Player player) {
        if(!sqlGetter.exists(player.getUniqueId())) {
            UUID uuid = player.getUniqueId();
            sqlGetter.setString(new MysqlValue("NAME", uuid, player.getName()));
            sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", uuid, ""));
            sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("TEXT_VALUE", uuid, "0"));
            
            sqlGetter.setUUID(new MysqlValue("UUID", uuid, uuid));
        }
    }

    public void addPublicText(Player player, String string) throws IOException {
        setLastTimeText(player);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        if(getPublicTextList(player) != null) {
            List<GamePlayerPublicMessaging> list = getPublicTextList(player);
            list.add(new GamePlayerPublicMessaging(player, time, string));
            setPublicText(player, list);
        } else {
            List<GamePlayerPublicMessaging> list = new ArrayList<>();
            list.add(new GamePlayerPublicMessaging(player, time, string));
            setPublicText(player, list);
        }
    }

    public void setPublicText(Player player, List<GamePlayerPublicMessaging> list) throws JsonProcessingException {
        sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", player.getUniqueId(), new EncryptionUtil().encryptPublicMessages(list)));
    }

    public List<GamePlayerPublicMessaging> getPublicTextList(Player player) throws IOException {
        return new EncryptionUtil().decryptPublicMessages(sqlGetter.getString(player.getUniqueId(), new MysqlValue("PUBLIC_TEXT")));
    }


    public void setTextValue(OfflinePlayer offlinePlayer, String i) {
        sqlGetter.setString(new MysqlValue("TEXT_VALUE", offlinePlayer.getUniqueId(), i));
    }

    public String getTextValue(OfflinePlayer offlinePlayer) {
        return sqlGetter.getString(offlinePlayer.getUniqueId(), new MysqlValue("TEXT_VALUE"));
    }



    public void addPrivateText(Player sender, OfflinePlayer receiver, String string) throws Exception {
        setLastTimeText(sender);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        if(getPrivateTextList(sender) != null) {
            List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
            gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender, receiver, time, string));
            setPrivateText(sender, gamePlayerPrivateMessagings);
        } else {
            List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = new ArrayList<>();
            gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender, receiver, time, string));
            setPrivateText(sender, gamePlayerPrivateMessagings);
        }
    }

    public void addPrivateText(Player sender, OfflinePlayer receiver) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender, receiver, time));
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void addPrivateText(Player sender, OfflinePlayer receiver, String string, ItemStack itemStack) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        GamePlayerPrivateMessaging gamePlayerPrivateMessaging = new GamePlayerPrivateMessaging(sender, receiver, time, string);
        gamePlayerPrivateMessaging.setItem(itemStack);
        gamePlayerPrivateMessagings.add(gamePlayerPrivateMessaging);
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void addPrivateText(Player sender, OfflinePlayer receiver, ItemStack itemStack) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        GamePlayerPrivateMessaging gamePlayerPrivateMessaging = new GamePlayerPrivateMessaging(sender, receiver, time);
        gamePlayerPrivateMessaging.setItem(itemStack);
        gamePlayerPrivateMessagings.add(gamePlayerPrivateMessaging);
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void setPrivateText(Player player, List<GamePlayerPrivateMessaging> list) throws JsonProcessingException {
        sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", player.getUniqueId(), new EncryptionUtil().encryptPrivateMessages(list)));
    }

    public List<GamePlayerPrivateMessaging> getPrivateTextList(Player player) throws Exception {
        return new EncryptionUtil().decryptPrivateMessages(sqlGetter.getString(player.getUniqueId(), new MysqlValue("PRIVATE_TEXT")));
    }



    public boolean isRecipient(OfflinePlayer request, OfflinePlayer player) {
        for(AllowedRecipient uuid : getAllowedRecipients(request)) {
            if(uuid.getPlayerUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public void addRecipient(OfflinePlayer player, OfflinePlayer recipient) {
        List<AllowedRecipient> list = new LinkedList<>(getAllowedRecipientsString(player));
        list.add(new AllowedRecipient(recipient.getName(), recipient.getName(), recipient.getUniqueId().toString()));
        setAllowedRecipientString(player, list);
    }

    public void removeRecipient(OfflinePlayer player, OfflinePlayer recipient) {
        UUID recipientUUID = recipient.getUniqueId();
        List<AllowedRecipient> allowedRecipients = getAllowedRecipientsString(player);
        List<AllowedRecipient> updatedRecipients = new ArrayList<>();

        for (AllowedRecipient uuidString : allowedRecipients) {
            if (!recipientUUID.toString().equals(uuidString.getPlayerUUID().toString())) {
                updatedRecipients.add(uuidString);
            }
        }

        setAllowedRecipientString(player, updatedRecipients); // Update the list in the database
    }

    public void setAllowedRecipientString(OfflinePlayer player, List<AllowedRecipient> list) {
        Gson gson = new Gson();
        sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", player.getUniqueId(), gson.toJson(list)));
    }
    public List<AllowedRecipient> getAllowedRecipientsString(OfflinePlayer player) {
        String x = sqlGetter.getString(player.getUniqueId(), new MysqlValue("ALLOWED_RECIPIENTS"));
        Gson gson = new Gson();
        List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<String>>() {}.getType());
        return recipients != null ? recipients : new ArrayList<>();
    }

    public List<AllowedRecipient> getAllowedRecipients(OfflinePlayer player) {
        List<AllowedRecipient> list = new ArrayList<>();
        for (AllowedRecipient s : getAllowedRecipientsString(player)) {
            if (s == null) {
                continue;
            }
            try {
                list.add(s);
            } catch (IllegalArgumentException ignored) {

            }
        }
        return list;
    }

    public List<GamePlayerPrivateMessaging> getPrivateChatsWithAllowedRecipient(Player player, OfflinePlayer allowedRecipient) throws Exception {
        List<GamePlayerPrivateMessaging> privateMessages = getPrivateTextList(player);

        List<GamePlayerPrivateMessaging> chatsWithAllowedRecipient = new ArrayList<>();

        for (GamePlayerPrivateMessaging privateMessage : privateMessages) {
            if (privateMessage.getReceiver().getUniqueId().equals(allowedRecipient.getUniqueId())) {
                chatsWithAllowedRecipient.add(privateMessage);
            }
        }

        return chatsWithAllowedRecipient;
    }

    public void setLastTimeText(Player player) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", player.getUniqueId(), time));
    }

    public String getLastTimeText(Player player) {
        return sqlGetter.getString(player.getUniqueId(), new MysqlValue("LAST_TIME_TEXT"));
    }

    public void clearLastTimeText(Player player) {
        sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", player.getUniqueId(), ""));
    }

    public void clearPrivateTextLogs(Player player) {
        sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", player.getUniqueId(), ""));
    }

    public void clearPublicTextLogs(Player player) {
        sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", player.getUniqueId(), ""));
    }

    public void clearAllowedRecipients(Player player) {
        sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", player.getUniqueId(), ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("player_text_database",
                new MysqlValue("NAME", ""),
                new MysqlValue("LAST_TIME_TEXT", ""),
                new MysqlValue("ALLOWED_RECIPIENTS", ""),
                new MysqlValue("PRIVATE_TEXT", ""),
                new MysqlValue("PUBLIC_TEXT", ""),
                new MysqlValue("TEXT_VALUE", ""));
    }
}
