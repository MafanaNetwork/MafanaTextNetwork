package me.tahacheji.mafanatextnetwork.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPublicMessaging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class GamePlayerPublicMessagingAdapter extends TypeAdapter<GamePlayerPublicMessaging> {

    @Override
    public void write(JsonWriter out, GamePlayerPublicMessaging message) throws IOException {
        out.beginObject();
        if (Objects.nonNull(message.getSender())) {
            out.name("sender").value(message.getSender().getUniqueId().toString());
        }
        out.name("time").value(message.getTime());
        if (Objects.nonNull(message.getText())) {
            out.name("text").value(message.getText());
        }
        out.endObject();
    }

    @Override
    public GamePlayerPublicMessaging read(JsonReader in) throws IOException {
        in.beginObject();
        String senderUUID = null;
        String time = null;
        String text = null;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "sender":
                    senderUUID = in.nextString();
                    break;
                case "time":
                    time = in.nextString();
                    break;
                case "text":
                    text = in.nextString();
                    break;
            }
        }
        in.endObject();

        Player sender = Bukkit.getPlayer(UUID.fromString(senderUUID));
        return new GamePlayerPublicMessaging(sender, time, text);
    }
}
