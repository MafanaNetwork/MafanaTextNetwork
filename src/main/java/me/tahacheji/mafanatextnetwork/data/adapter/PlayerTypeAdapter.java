package me.tahacheji.mafanatextnetwork.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PlayerTypeAdapter extends TypeAdapter<OfflinePlayer> {

    @Override
    public void write(JsonWriter out, OfflinePlayer player) throws IOException {
        // Serialize the player as needed, excluding any problematic fields
        out.beginObject();
        out.name("uuid").value(player.getUniqueId().toString());
        // Add other fields you want to serialize here
        out.endObject();
    }

    @Override
    public OfflinePlayer read(JsonReader in) throws IOException {
        // Deserialize the JSON and recreate the Player object
        in.beginObject();
        UUID uuid = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uuid":
                    String uuidStr = in.nextString();
                    uuid = UUID.fromString(uuidStr);
                    break;
                // Add cases for other fields you want to deserialize here
                default:
                    in.skipValue(); // Skip unknown fields
                    break;
            }
        }

        in.endObject();

        if (uuid != null) {
            // Ensure you obtain the Player object safely based on the UUID
            // You can use Bukkit's getPlayer() method here.
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            return player;
        } else {
            return null;
        }
    }
}
