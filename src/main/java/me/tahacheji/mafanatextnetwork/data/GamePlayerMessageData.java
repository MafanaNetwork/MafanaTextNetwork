package me.tahacheji.mafanatextnetwork.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.*;
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

    public CompletableFuture<Boolean> addPlayer(Player player, List<String> values) {
        UUID uuid = player.getUniqueId();
        return sqlGetter.existsAsync(uuid)
                .thenComposeAsync(exists -> {
                    if (!exists) {
                        Gson gson = new Gson();
                        return sqlGetter.setStringAsync(new DatabaseValue("NAME", uuid, player.getName()))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("LAST_TIME_TEXT", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("ALLOWED_RECIPIENTS", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("PRIVATE_TEXT", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("PUBLIC_TEXT", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("PLAYER_MAIL", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("LAST_SENDER", uuid, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", uuid, gson.toJson(values))))
                                .thenCompose(__ -> sqlGetter.setUUIDAsync(new DatabaseValue("UUID", uuid, uuid)))
                                .thenApply(__ -> true);
                    } else {
                        return CompletableFuture.completedFuture(false);
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return false;
                });
    }

    public CompletableFuture<Void> replaceUserValue(UUID uuid, String replace, String newValue) {
        return getUserValues(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String r = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String l : x) {
                if(l.equalsIgnoreCase(replace)) {
                    r = l;
                }
            }
            x.remove(r);
            x.add(newValue);
            return setUserValues(uuid, x);
        });
    }

    public CompletableFuture<Void> addUserValue(UUID uuid, String s) {
        return getUserValues(uuid).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(s);
            return setUserValues(uuid, x);
        });
    }

    public CompletableFuture<Void> setUserValues(UUID uuid, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("USER_VALUES", uuid, gson.toJson(s)));
    }

    public CompletableFuture<List<String>> getUserValues(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("USER_VALUES"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> clearLastSender(UUID uuid) {
        return sqlGetter.setStringAsync(new DatabaseValue("LAST_SENDER", uuid, ""));
    }

    public CompletableFuture<Void> setLastSender(UUID uuid, LastSender lastSender) {
        Gson gson = new Gson();
        String v = gson.toJson(lastSender);
        return sqlGetter.setStringAsync(new DatabaseValue("LAST_SENDER", uuid, v));
    }

    public CompletableFuture<LastSender> getLastSender(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("LAST_SENDER"))
                .thenApplyAsync(x -> {
                    Gson gson = new Gson();
                    return gson.fromJson(x, new TypeToken<LastSender>() {
                    }.getType());
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
                .thenApplyAsync(mails -> {
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
            if (playerMails != null) {
                p.addAll(playerMails);
            }
            PlayerMail playerMailToRemove = p.stream()
                    .filter(x -> x.getMailUUID().equalsIgnoreCase(mail.getMailUUID()))
                    .findFirst()
                    .orElse(null);
            if (playerMailToRemove != null) {
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
                .thenApplyAsync(x -> {
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
                .thenComposeAsync(list -> {
                    final List<GamePlayerPublicMessaging> finalList;
                    if (list != null) {
                        finalList = new ArrayList<>(list);
                    } else {
                        finalList = new ArrayList<>();
                    }
                    return MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(player)
                            .thenApplyAsync(p -> {
                                if (finalList != null) {
                                    finalList.add(new GamePlayerPublicMessaging(player.toString(), time, string, p.getServerID().toString()));
                                }
                                return finalList;
                            });
                })
                .thenComposeAsync(finalList -> setPublicTextAsync(player, finalList));
    }


    public CompletableFuture<Void> setPublicTextAsync(UUID uuid, List<GamePlayerPublicMessaging> list) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PUBLIC_TEXT", uuid, gson.toJson(list)));
    }

    public CompletableFuture<List<GamePlayerPublicMessaging>> getPublicTextListAsync(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("PUBLIC_TEXT"))
                .thenApplyAsync(x -> {
                    Gson gson = new Gson();
                    List<GamePlayerPublicMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPublicMessaging>>() {
                    }.getType());
                    return m != null ? m : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> addPrivateText(UUID sender, UUID receiver, String string) {
        setLastTimeText(sender);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";

        return getPrivateTextListAsync(sender)
                .thenComposeAsync(gamePlayerPrivateMessagings -> {
                    final List<GamePlayerPrivateMessaging> finalList;
                    if (gamePlayerPrivateMessagings != null) {
                        finalList = new ArrayList<>(gamePlayerPrivateMessagings);
                    } else {
                        finalList = new ArrayList<>();
                    }
                    return MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(sender)
                            .thenCombineAsync(
                                    MafanaNetworkCommunicator.getInstance().getNetworkCommunicatorDatabase().getProxyPlayerAsync(receiver),
                                    (senderProxy, receiverProxy) -> {
                                        if (finalList != null) {
                                            finalList.add(new GamePlayerPrivateMessaging(sender.toString(), receiver.toString(), time, string, senderProxy.getServerID().toString(), receiverProxy.getServerID().toString()));
                                        }
                                        return finalList;
                                    })
                            .thenComposeAsync(list -> setPrivateTextAsync(sender, list));
                });
    }


    public CompletableFuture<Void> setPrivateTextAsync(UUID uuid, List<GamePlayerPrivateMessaging> list) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PRIVATE_TEXT", uuid, gson.toJson(list)));
    }

    public CompletableFuture<List<GamePlayerPrivateMessaging>> getPrivateTextListAsync(UUID uuid) {
        return sqlGetter.getStringAsync(uuid, new DatabaseValue("PRIVATE_TEXT"))
                .thenApplyAsync(x -> {
                    Gson gson = new Gson();
                    List<GamePlayerPrivateMessaging> m = gson.fromJson(x, new TypeToken<List<GamePlayerPrivateMessaging>>() {
                    }.getType());
                    return m != null ? m : new ArrayList<>();
                });
    }

    public CompletableFuture<Boolean> isRecipient(UUID request, UUID player) {
        return getAllowedRecipientsAsync(request)
                .thenApplyAsync(allowedRecipients -> allowedRecipients.stream()
                        .anyMatch(uuid -> uuid.getPlayerUUID().toString().equalsIgnoreCase(player.toString())));
    }

    public CompletableFuture<Void> addRecipient(UUID player, UUID recipient) {
        CompletableFuture<Void> voidCompletableFuture = new CompletableFuture<>();
        List<AllowedRecipient> allowedRecipients = new ArrayList<>();
        getAllowedRecipientsAsync(player).thenAcceptAsync(allowedRecipients1 -> {
            allowedRecipients.addAll(allowedRecipients1);
            MafanaNetworkCommunicator.getInstance().getPlayerDatabase().getOfflineProxyPlayerAsync(recipient).thenAcceptAsync(offlineProxyPlayer -> {
                allowedRecipients.add(new AllowedRecipient(player.toString(), offlineProxyPlayer.getPlayerName(), offlineProxyPlayer.getPlayerDisplayName(), recipient.toString()));
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
            for (AllowedRecipient allowedRecipient : allowedRecipients) {
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
                .thenApplyAsync(x -> {
                    Gson gson = new Gson();
                    List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<AllowedRecipient>>() {
                    }.getType());
                    return recipients != null ? recipients : new ArrayList<>();
                });
    }

    public CompletableFuture<List<AllowedRecipient>> getAllAllowedRecipients() {
        return sqlGetter.getAllStringAsync(new DatabaseValue("ALLOWED_RECIPIENTS"))
                .thenApplyAsync(list -> {
                    List<AllowedRecipient> marketListingList = new ArrayList<>();
                    if (list != null) {
                        Gson gson = new Gson();
                        for (String s : list) {
                            if(gson.fromJson(s, new TypeToken<List<AllowedRecipient>>() {}.getType()) != null) {
                                marketListingList.addAll(gson.fromJson(s, new TypeToken<List<AllowedRecipient>>() {
                                }.getType()));
                            }
                        }
                    }
                    return marketListingList;
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return new ArrayList<>(); // Return an empty list in case of exception
                });
    }

    public CompletableFuture<List<AllowedRecipient>> getAllowedRecipientsAsync(UUID player) {
        return getAllowedRecipientsStringAsync(player)
                .thenApplyAsync(list -> {
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
                .thenApplyAsync(privateMessages -> {
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
        String x = sqlGetter.getStringSync(player, new DatabaseValue("ALLOWED_RECIPIENTS"));
        Gson gson = new Gson();
        List<AllowedRecipient> recipients = gson.fromJson(x, new TypeToken<List<AllowedRecipient>>() {
        }.getType());
        return recipients != null ? recipients : new ArrayList<>();
    }


    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    public void connect() {
        sqlGetter.createTable("player_text_database",
                new DatabaseValue("NAME", ""),
                new DatabaseValue("LAST_TIME_TEXT", ""),
                new DatabaseValue("ALLOWED_RECIPIENTS", ""),
                new DatabaseValue("PRIVATE_TEXT", ""),
                new DatabaseValue("PUBLIC_TEXT", ""),
                new DatabaseValue("PLAYER_MAIL", ""),
                new DatabaseValue("LAST_SENDER", ""),
                new DatabaseValue("USER_VALUES", ""));
    }
}
