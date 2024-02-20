package me.tahacheji.mafanatextnetwork.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.DatabaseValue;
import me.tahacheji.mafana.data.MySQL;
import me.tahacheji.mafana.data.OfflineProxyPlayer;
import me.tahacheji.mafana.data.SQLGetter;
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
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GamePlayerMessageData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public GamePlayerMessageData() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public CompletableFuture<Void> addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        return sqlGetter.existsAsync(uuid).thenComposeAsync(exists -> {
            if (!exists) {
                CompletableFuture<Void> setNameFuture = sqlGetter.setStringAsync(new DatabaseValue("NAME", uuid, player.getName()));
                CompletableFuture<Void> setLastTimeTextFuture = sqlGetter.setStringAsync(new DatabaseValue("LAST_TIME_TEXT", uuid, ""));
                CompletableFuture<Void> setAllowedRecipientsFuture = sqlGetter.setStringAsync(new DatabaseValue("ALLOWED_RECIPIENTS", uuid, ""));
                CompletableFuture<Void> setPrivateTextFuture = sqlGetter.setStringAsync(new DatabaseValue("PRIVATE_TEXT", uuid, ""));
                CompletableFuture<Void> setPublicTextFuture = sqlGetter.setStringAsync(new DatabaseValue("PUBLIC_TEXT", uuid, ""));
                CompletableFuture<Void> setPlayerMailFuture = sqlGetter.setStringAsync(new DatabaseValue("PLAYER_MAIL", uuid, ""));
                CompletableFuture<Void> setTextValueFuture = sqlGetter.setStringAsync(new DatabaseValue("TEXT_VALUE", uuid, "0"));
                CompletableFuture<Void> setUUIDFuture = sqlGetter.setUUIDAsync(new DatabaseValue("UUID", uuid, uuid));

                return CompletableFuture.allOf(setNameFuture, setLastTimeTextFuture, setAllowedRecipientsFuture,
                        setPrivateTextFuture, setPublicTextFuture, setPlayerMailFuture, setTextValueFuture, setUUIDFuture);
            }
            return CompletableFuture.completedFuture(null);
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<Void> openMail(UUID uuid, PlayerMail mail) {
        PlayerMail newPlayerMail = mail;
        newPlayerMail.setOpened(true);
        return removeMail(uuid, mail)
                .thenCompose(aVoid -> addMail(uuid, newPlayerMail));
    }

    public CompletableFuture<Void> addMail(UUID uuid, PlayerMail mail) {
        return getPlayerMailAsync(uuid)
                .thenApply(mails -> {
                    List<PlayerMail> updatedMails = new ArrayList<>(mails);
                    updatedMails.add(mail);
                    return updatedMails;
                })
                .thenCompose(updatedMails -> setPlayerMailAsync(uuid, updatedMails));
    }

    public CompletableFuture<Void> removeMail(UUID uuid, PlayerMail mail) {
        CompletableFuture<Void> z = new CompletableFuture<>();
        List<PlayerMail> p = new ArrayList<>();
        getPlayerMailAsync(uuid).thenAcceptAsync(playerMails -> {
            if(playerMails != null) {
                p.addAll(playerMails);
            }
            PlayerMail playerMailToRemove = p.stream()
                    .filter(x -> x.getMailUUID().equalsIgnoreCase(mail.getMailUUID()))
                    .findFirst()
                    .orElse(null);
            if(playerMailToRemove != null) {
                p.remove(playerMailToRemove);
            }
            setPlayerMailAsync(uuid, p).thenAcceptAsync(o -> {
               z.complete(null);
            });
        });
        return z;
    }

    public CompletableFuture<Void> setPlayerMailAsync(UUID uuid, List<PlayerMail> playerMails) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PLAYER_MAIL", uuid, gson.toJson(playerMails)));
    }

    public CompletableFuture<List<PlayerMail>> getPlayerMailAsync(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("PLAYER_MAIL"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<PlayerMail> m = gson.fromJson(x, new TypeToken<List<PlayerMail>>() {
                    }.getType());
                    return m != null ? m : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> addPublicText(UUID player, String string) {
        setLastTimeText(player);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        return getPublicTextListAsync(player)
                .thenApply(list -> {
                    if (list != null) {
                        list.add(new GamePlayerPublicMessaging(player.toString(), time, string));
                    } else {
                        list = new ArrayList<>();
                        list.add(new GamePlayerPublicMessaging(player.toString(), time, string));
                    }
                    return list;
                })
                .thenCompose(list -> setPublicTextAsync(player, list));
    }

    public CompletableFuture<Void> setPublicTextAsync(UUID uuid, List<GamePlayerPublicMessaging> list) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PUBLIC_TEXT", uuid, gson.toJson(list)));
    }

    public CompletableFuture<List<GamePlayerPublicMessaging>> getPublicTextListAsync(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("PUBLIC_TEXT"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<GamePlayerPublicMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPublicMessaging>>() {
                    }.getType());
                    return m != null ? m : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setTextValue(UUID uuid, String i) {
        return sqlGetter.setStringAsync(new DatabaseValue("TEXT_VALUE", uuid, i));
    }

    public CompletableFuture<String> getTextValue(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("TEXT_VALUE"));
    }

    public CompletableFuture<Void> addPrivateText(UUID sender, UUID receiver, String string) {
        setLastTimeText(sender);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        return getPrivateTextListAsync(sender)
                .thenApply(gamePlayerPrivateMessagings -> {
                    if (gamePlayerPrivateMessagings != null) {
                        gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string));
                    } else {
                        gamePlayerPrivateMessagings = new ArrayList<>();
                        gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string));
                    }
                    return gamePlayerPrivateMessagings;
                })
                .thenCompose(gamePlayerPrivateMessagings -> setPrivateTextAsync(sender, gamePlayerPrivateMessagings));
    }

    public CompletableFuture<Void> addPrivateText(UUID sender, UUID receiver) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        return getPrivateTextListAsync(sender)
                .thenApply(gamePlayerPrivateMessagings -> {
                    if (gamePlayerPrivateMessagings == null) {
                        gamePlayerPrivateMessagings = new ArrayList<>();
                    }
                    gamePlayerPrivateMessagings.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time));
                    return gamePlayerPrivateMessagings;
                })
                .thenCompose(gamePlayerPrivateMessagings -> setPrivateTextAsync(sender, gamePlayerPrivateMessagings));
    }

    public CompletableFuture<Void> setPrivateTextAsync(UUID uuid, List<GamePlayerPrivateMessaging> list) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PRIVATE_TEXT", uuid, gson.toJson(list)));
    }

    public CompletableFuture<List<GamePlayerPrivateMessaging>> getPrivateTextListAsync(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("PRIVATE_TEXT"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<GamePlayerPrivateMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPrivateMessaging>>() {
                    }.getType());
                    return m != null ? m : new ArrayList<>();
                });
    }

    public CompletableFuture<Boolean> isRecipient(UUID request, UUID player) {
        return getAllowedRecipientsAsync(request)
                .thenApply(allowedRecipients -> allowedRecipients.stream()
                        .anyMatch(uuid -> uuid.getPlayerUUID().toString().equalsIgnoreCase(player.toString())));
    }

    public CompletableFuture<Void> addRecipient(UUID player, UUID recipient) {
        CompletableFuture<Void> voidCompletableFuture = new CompletableFuture<>();
        List<AllowedRecipient> allowedRecipients = new ArrayList<>();
        getAllowedRecipientsAsync(player).thenAcceptAsync(allowedRecipients1 -> {
            allowedRecipients.addAll(allowedRecipients1);
            MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(recipient).thenAcceptAsync(offlineProxyPlayer -> {
                allowedRecipients.add(new AllowedRecipient(offlineProxyPlayer.getPlayerName(), offlineProxyPlayer.getPlayerDisplayName(), recipient.toString()));
                setAllowedRecipientStringAsync(player, allowedRecipients).thenAcceptAsync(a -> {
                    voidCompletableFuture.complete(null);
                });
            });
        });
        return voidCompletableFuture;
    }


    public CompletableFuture<Void> removeRecipient(UUID player, UUID recipient) {
        CompletableFuture<Void> x = new CompletableFuture<>();
        List<AllowedRecipient> updatedRecipients = new ArrayList<>();
        getAllowedRecipientsAsync(player).thenAcceptAsync(allowedRecipients -> {
            for(AllowedRecipient allowedRecipient : allowedRecipients) {
                if (!recipient.toString().equals(allowedRecipient.getPlayerUUID().toString())) {
                    updatedRecipients.add(allowedRecipient);
                    MafanaTextNetwork.getInstance().getLogger().log(Level.INFO, "Updated Recipient Added: UUID: " + allowedRecipient.getPlayerUUID() + " NAME: " + allowedRecipient.getPlayerName());
                }
                setAllowedRecipientStringAsync(player, updatedRecipients).thenAcceptAsync(v -> {
                    x.complete(null);
                });

            }
        });
        return x;
    }

    public CompletableFuture<Void> setAllowedRecipientStringAsync(UUID player, List<AllowedRecipient> list) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("ALLOWED_RECIPIENTS", player, gson.toJson(list)));
    }

    public CompletableFuture<List<AllowedRecipient>> getAllowedRecipientsStringAsync(UUID player) {
        return sqlGetter.getStringAsync(player, new DatabaseValue("ALLOWED_RECIPIENTS"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<AllowedRecipient>>() {
                    }.getType());
                    return recipients != null ? recipients : new ArrayList<>();
                });
    }

    public CompletableFuture<List<AllowedRecipient>> getAllowedRecipientsAsync(UUID player) {
        return getAllowedRecipientsStringAsync(player)
                .thenApply(list -> {
                    List<AllowedRecipient> allowedRecipients = new ArrayList<>();
                    for (AllowedRecipient s : list) {
                        if (s != null) {
                            allowedRecipients.add(s);
                        }
                    }
                    return allowedRecipients;
                });
    }

    public CompletableFuture<List<GamePlayerPrivateMessaging>> getPrivateChatsWithAllowedRecipientAsync(UUID uuid, UUID allowedRecipient) {
        return getPrivateTextListAsync(uuid)
                .thenApply(privateMessages -> {
                    if (privateMessages == null) {
                        return new ArrayList<>();
                    }
                    return privateMessages.stream()
                            .filter(privateMessage -> privateMessage.getReceiver().equals(allowedRecipient))
                            .collect(Collectors.toList());
                });
    }

    public CompletableFuture<Void> setLastTimeText(UUID uuid) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        return sqlGetter.setStringAsync(new DatabaseValue("LAST_TIME_TEXT", uuid, time));
    }

    public CompletableFuture<String> getLastTimeText(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("LAST_TIME_TEXT"));
    }

    public CompletableFuture<Void> clearLastTimeText(UUID uuid) {
        return sqlGetter.setStringAsync(new DatabaseValue("LAST_TIME_TEXT", uuid, ""));
    }

    public CompletableFuture<Void> clearPrivateTextLogs(UUID uuid) {
        return sqlGetter.setStringAsync(new DatabaseValue("PRIVATE_TEXT", uuid, ""));
    }

    public CompletableFuture<Void> clearPublicTextLogs(UUID uuid) {
        return sqlGetter.setStringAsync(new DatabaseValue("PUBLIC_TEXT", uuid, ""));
    }

    public CompletableFuture<Void> clearAllowedRecipients(UUID uuid) {
        return sqlGetter.setStringAsync(new DatabaseValue("ALLOWED_RECIPIENTS", uuid, ""));
    }

    public List<AllowedRecipient> getAllowedRecipientsStringSync(UUID player) {
        String x = sqlGetter.getString(player, new DatabaseValue("ALLOWED_RECIPIENTS"));
        Gson gson = new Gson();
        List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<AllowedRecipient>>() {
        }.getType());
        return recipients != null ? recipients : new ArrayList<>();
    }


    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("player_text_database",
                new DatabaseValue("NAME", ""),
                new DatabaseValue("LAST_TIME_TEXT", ""),
                new DatabaseValue("ALLOWED_RECIPIENTS", ""),
                new DatabaseValue("PRIVATE_TEXT", ""),
                new DatabaseValue("PUBLIC_TEXT", ""),
                new DatabaseValue("PLAYER_MAIL", ""),
                new DatabaseValue("TEXT_VALUE", ""));
    }
}
