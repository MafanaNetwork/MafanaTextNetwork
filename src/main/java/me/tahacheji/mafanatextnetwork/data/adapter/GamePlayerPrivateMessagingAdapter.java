package me.tahacheji.mafanatextnetwork.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tahacheji.mafanatextnetwork.data.GamePlayerPrivateMessaging;
import me.tahacheji.mafanatextnetwork.util.EncryptionUtil;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class GamePlayerPrivateMessagingAdapter extends TypeAdapter<GamePlayerPrivateMessaging> {
    @Override
    public void write(JsonWriter out, GamePlayerPrivateMessaging message) throws IOException {
        out.beginObject();
        if (Objects.nonNull(message.getSender())) {
            out.name("sender").value(message.getSender().getUniqueId().toString());
        }
        if (Objects.nonNull(message.getReceiver())) {
            out.name("receiver").value(message.getReceiver().getUniqueId().toString());
        }
        out.name("time").value(message.getTime());
        if (Objects.nonNull(message.getText())) {
            out.name("text").value(message.getText());
        }
        if (Objects.nonNull(message.getItem())) {
            out.name("item").value(new EncryptionUtil().itemToBase64(message.getItem()));
        }
        out.endObject();
    }

    @Override
    public GamePlayerPrivateMessaging read(JsonReader in) throws IOException {
        in.beginObject();
        String sender = null;
        String receiver = null;
        String time = null;
        String text = null;
        String item = null;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "sender":
                    sender = in.nextString();
                    break;
                case "receiver":
                    receiver = in.nextString();
                    break;
                case "time":
                    time = in.nextString();
                    break;
                case "text":
                    text = in.nextString();
                    break;
                case "item":
                    item = in.nextString();
                    break;
            }
        }
        in.endObject();
        GamePlayerPrivateMessaging message = new GamePlayerPrivateMessaging(Bukkit.getPlayer(UUID.fromString(sender)), Bukkit.getPlayer(UUID.fromString(receiver)), time, text);
        if(item != null) {
            message.setItem(new EncryptionUtil().itemFromBase64(item));
        }
        return message;
    }
}
