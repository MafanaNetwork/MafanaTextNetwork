package me.tahacheji.mafanatextnetwork.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafanatextnetwork.MafanaTextNetwork;
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
        if (!sqlGetter.exists(player.getUniqueId())) {
            UUID uuid = player.getUniqueId();
            sqlGetter.setString(new MysqlValue("NAME", uuid, player.getName()));
            sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", uuid, ""));
            sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", uuid, ""));
            sqlGetter.setString(new MysqlValue("PLAYER_MAIL", uuid, ""));
            sqlGetter.setString(new MysqlValue("TEXT_VALUE", uuid, "0"));

            sqlGetter.setUUID(new MysqlValue("UUID", uuid, uuid));
        }
    }

    public void openMail(UUID uuid, PlayerMail mail) {
        PlayerMail newPlayerMail = mail;
        newPlayerMail.setOpened(true);
        removeMail(uuid, mail);
        addMail(uuid, newPlayerMail);
    }

    public void addMail(UUID uuid, PlayerMail mail) {
        List<PlayerMail> mails = new ArrayList<>();
        if (getPlayerMail(uuid) != null) {
            mails.addAll(getPlayerMail(uuid));
        }
        mails.add(mail);
        setPlayerMail(uuid, mails);
    }

    public void removeMail(UUID uuid, PlayerMail mail) {
        List<PlayerMail> mails = new ArrayList<>();
        if (getPlayerMail(uuid) != null) {
            mails.addAll(getPlayerMail(uuid));
        }

        PlayerMail playerMailToRemove = mails.stream()
                .filter(x -> x.getMailUUID().equalsIgnoreCase(mail.getMailUUID()))
                .findFirst()
                .orElse(null);

        if (playerMailToRemove != null) {
            mails.remove(playerMailToRemove);
            setPlayerMail(uuid, mails);
        }
    }

    public void setPlayerMail(UUID uuid, List<PlayerMail> playerMails) {
        Gson gson = new Gson();
        sqlGetter.setString(new MysqlValue("PLAYER_MAIL", uuid, gson.toJson(playerMails)));

    }

    public List<PlayerMail> getPlayerMail(UUID uuid) {
        String x = sqlGetter.getString(uuid, new MysqlValue("PLAYER_MAIL"));
        Gson gson = new Gson();
        List<PlayerMail> m = gson.fromJson(x, new TypeToken<List<PlayerMail>>() {
        }.getType());
        return m != null ? m : new ArrayList<>();
    }

    public void addPublicText(UUID player, String string) throws IOException {
        setLastTimeText(player);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        if (getPublicTextList(player) != null) {
            List<GamePlayerPublicMessaging> list = getPublicTextList(player);
            list.add(new GamePlayerPublicMessaging(player.toString(), time, string));
            setPublicText(player, list);
        } else {
            List<GamePlayerPublicMessaging> list = new ArrayList<>();
            list.add(new GamePlayerPublicMessaging(player.toString(), time, string));
            setPublicText(player, list);
        }
    }

    public void setPublicText(UUID uuid, List<GamePlayerPublicMessaging> list) {
        Gson gson = new Gson();

        sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", uuid, gson.toJson(list)));
    }

    public List<GamePlayerPublicMessaging> getPublicTextList(UUID uuid) {
        String x = sqlGetter.getString(uuid, new MysqlValue("PUBLIC_TEXT"));
        Gson gson = new Gson();
        List<GamePlayerPublicMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPublicMessaging>>() {
        }.getType());
        return m != null ? m : new ArrayList<>();
    }


    public void setTextValue(UUID uuid, String i) {
        sqlGetter.setString(new MysqlValue("TEXT_VALUE", uuid, i));
    }

    public String getTextValue(UUID uuid) {
        return sqlGetter.getString(uuid, new MysqlValue("TEXT_VALUE"));
    }


    public void addPrivateText(UUID sender, UUID receiver, String string) throws Exception {
        setLastTimeText(sender);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        if (getPrivateTextList(sender) != null) {
            List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
            gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string));
            setPrivateText(sender, gamePlayerPrivateMessagings);
        } else {
            List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = new ArrayList<>();
            gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string));
            setPrivateText(sender, gamePlayerPrivateMessagings);
        }
    }

    public void addPrivateText(UUID sender, UUID receiver) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time));
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void addPrivateText(UUID sender, UUID receiver, String string, ItemStack itemStack) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        GamePlayerPrivateMessaging gamePlayerPrivateMessaging = new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string);
        gamePlayerPrivateMessaging.setItem(itemStack);
        gamePlayerPrivateMessagings.add(gamePlayerPrivateMessaging);
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void addPrivateText(UUID sender, UUID receiver, ItemStack itemStack) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        List<GamePlayerPrivateMessaging> gamePlayerPrivateMessagings = getPrivateTextList(sender);
        GamePlayerPrivateMessaging gamePlayerPrivateMessaging = new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time);
        gamePlayerPrivateMessaging.setItem(itemStack);
        gamePlayerPrivateMessagings.add(gamePlayerPrivateMessaging);
        setPrivateText(sender, gamePlayerPrivateMessagings);
    }

    public void setPrivateText(UUID uuid, List<GamePlayerPrivateMessaging> list) throws JsonProcessingException {
        Gson gson = new Gson();

        sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", uuid, gson.toJson(list)));
    }

    public List<GamePlayerPrivateMessaging> getPrivateTextList(UUID uuid) throws Exception {
        String x = sqlGetter.getString(uuid, new MysqlValue("PRIVATE_TEXT"));
        Gson gson = new Gson();
        List<GamePlayerPrivateMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPrivateMessaging>>() {
        }.getType());
        return m != null ? m : new ArrayList<>();
    }


    public boolean isRecipient(UUID request, UUID player) {
        for (AllowedRecipient uuid : getAllowedRecipients(request)) {
            if (uuid.getPlayerUUID().toString().equalsIgnoreCase(player.toString())) {
                return true;
            }
        }
        return false;
    }

    public void addRecipient(UUID player, UUID recipient) {
        List<AllowedRecipient> list = new LinkedList<>(getAllowedRecipientsString(player));
        OfflineProxyPlayer offlineProxyPlayer = MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayer(recipient);
        list.add(new AllowedRecipient(offlineProxyPlayer.getPlayerName(), offlineProxyPlayer.getPlayerDisplayName(), recipient.toString()));
        setAllowedRecipientString(player, list);
    }

    public void removeRecipient(UUID player, UUID recipient) {
        List<AllowedRecipient> allowedRecipients = getAllowedRecipientsString(player);
        List<AllowedRecipient> updatedRecipients = new ArrayList<>();

        for (AllowedRecipient uuidString : allowedRecipients) {
            if (!recipient.toString().equals(uuidString.getPlayerUUID().toString())) {
                updatedRecipients.add(uuidString);
            }
        }

        setAllowedRecipientString(player, updatedRecipients); // Update the list in the database
    }

    public void setAllowedRecipientString(UUID player, List<AllowedRecipient> list) {
        Gson gson = new Gson();
        sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", player, gson.toJson(list)));
    }

    public List<AllowedRecipient> getAllowedRecipientsString(UUID player) {
        String x = sqlGetter.getString(player, new MysqlValue("ALLOWED_RECIPIENTS"));
        Gson gson = new Gson();
        List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<AllowedRecipient>>() {
        }.getType());
        return recipients != null ? recipients : new ArrayList<>();
    }

    public List<AllowedRecipient> getAllowedRecipients(UUID player) {
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

    public List<GamePlayerPrivateMessaging> getPrivateChatsWithAllowedRecipient(UUID uuid, UUID allowedRecipient) throws Exception {
        List<GamePlayerPrivateMessaging> privateMessages = getPrivateTextList(uuid);

        if (privateMessages == null) {
            return new ArrayList<>();
        }

        List<GamePlayerPrivateMessaging> chatsWithAllowedRecipient = new ArrayList<>();

        for (GamePlayerPrivateMessaging privateMessage : privateMessages) {
            if (privateMessage.getReceiver().equals(allowedRecipient)) {
                chatsWithAllowedRecipient.add(privateMessage);
            }
        }

        return chatsWithAllowedRecipient;
    }


    public void setLastTimeText(UUID uuid) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", uuid, time));
    }

    public String getLastTimeText(UUID uuid) {
        return sqlGetter.getString(uuid, new MysqlValue("LAST_TIME_TEXT"));
    }

    public void clearLastTimeText(UUID uuid) {
        sqlGetter.setString(new MysqlValue("LAST_TIME_TEXT", uuid, ""));
    }

    public void clearPrivateTextLogs(UUID uuid) {
        sqlGetter.setString(new MysqlValue("PRIVATE_TEXT", uuid, ""));
    }

    public void clearPublicTextLogs(UUID uuid) {
        sqlGetter.setString(new MysqlValue("PUBLIC_TEXT", uuid, ""));
    }

    public void clearAllowedRecipients(UUID uuid) {
        sqlGetter.setString(new MysqlValue("ALLOWED_RECIPIENTS", uuid, ""));
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
                new MysqlValue("PLAYER_MAIL", ""),
                new MysqlValue("TEXT_VALUE", ""));
    }
}
