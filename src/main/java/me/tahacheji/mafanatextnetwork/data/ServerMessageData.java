package me.tahacheji.mafanatextnetwork.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tahacheji.mafana.MafanaNetworkCommunicator;
import me.tahacheji.mafana.data.DatabaseValue;
import me.tahacheji.mafana.data.MySQL;
import me.tahacheji.mafana.data.SQLGetter;
import me.tahacheji.mafanatextnetwork.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ServerMessageData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public ServerMessageData() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public CompletableFuture<Boolean> setServerIgnoreList(String server) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(server);
            if (!sqlGetter.existsAsync(uuid).join()) {
                return sqlGetter.setStringAsync(new DatabaseValue("SERVER", uuid, server))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("BLACK_LISTED_WORDS", uuid, "[]")))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("STARED_OUT_WORDS", uuid, "[]")))
                        .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("MUTED_PLAYERS", uuid, "[]")))
                        .thenApply(__ -> true)
                        .join();
            }
            return false;
        });
    }

    public CompletableFuture<Void> removeBlackListedWord(String server, String s) {
        return getBlackListedWordList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setBlackListedWordList(server, x);
        });
    }

    public CompletableFuture<Void> addBlackListedWord(String server, String id) {
        return getBlackListedWordList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setBlackListedWordList(server, x);
        });
    }

    public CompletableFuture<List<String>> getBlackListedWordList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("BLACK_LISTED_WORDS"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setBlackListedWordList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("BLACK_LISTED_WORDS", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }

    public CompletableFuture<Void> removeStaredOutWord(String server, String s) {
        return getStaredOutWordList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setStaredOutWordList(server, x);
        });
    }

    public CompletableFuture<Void> addStaredOutWord(String server, String id) {
        return getStaredOutWordList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setStaredOutWordList(server, x);
        });
    }

    public CompletableFuture<List<String>> getStaredOutWordList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("STARED_OUT_WORDS"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setStaredOutWordList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("STARED_OUT_WORDS", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }

    public CompletableFuture<Void> removeMutedPlayer(String server, String s) {
        return getMutedPlayerList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            String itemToRemove = null;
            if(values != null) {
                x.addAll(values);
            }
            for(String d : x) {
                if(d.equalsIgnoreCase(s)) {
                    itemToRemove = d;
                }
            }
            x.remove(itemToRemove);
            return setMutedPlayerList(server, x);
        });
    }

    public CompletableFuture<Void> addMutedPlayer(String server, String id) {
        return getMutedPlayerList(server).thenComposeAsync(values -> {
            List<String> x = new ArrayList<>();
            if(values != null) {
                x.addAll(values);
            }
            x.add(id);
            return setMutedPlayerList(server, x);
        });
    }

    public CompletableFuture<List<String>> getMutedPlayerList(String server) {
        return sqlGetter.getStringAsync(new EncryptionUtil().stringToUUID(server), new DatabaseValue("MUTED_PLAYERS"))
                .thenApply(x -> {
                    Gson gson = new Gson();
                    List<String> values = gson.fromJson(x, new TypeToken<List<String>>() {
                    }.getType());
                    return values != null ? values : new ArrayList<>();
                });
    }

    public CompletableFuture<Void> setMutedPlayerList(String server, List<String> s) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("MUTED_PLAYERS", new EncryptionUtil().stringToUUID(server), gson.toJson(s)));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    public void connect() {
        sqlGetter.createTable("server_text_database",
                new DatabaseValue("SERVER", ""),
                new DatabaseValue("BLACK_LISTED_WORDS", ""),
                new DatabaseValue("STARED_OUT_WORDS", ""),
                new DatabaseValue("MUTED_PLAYERS", ""));
    }
}
