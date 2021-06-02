package xyz.destial.abyssalgames.utils;

import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Hub {
    private final String name;

    public Hub(String name) {
        this.name = name;
    }

    public void send(Player player) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(this.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(AbyssalGames.getPlugin(), "BungeeCord", b.toByteArray());
    }
}
