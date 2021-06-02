package xyz.destial.abyssalgames.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.match.BasePlayer;

public class Query {
    public static String TABLE_NAME = AbyssalGames.getPlugin().getConfigManager().getConfig().getString("mysql.table-prefix", "ab_") + "users";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (name VARCHAR(16), uuid VARCHAR(100), data VARCHAR(2048))";

    public static PreparedStatement FETCH_USER(Player player) {
        try {
            PreparedStatement statement = AbyssalGames.getPlugin().getDataStore().getConnection().prepareStatement("SELECT data FROM " + TABLE_NAME + " WHERE " + (Database.useUUID ? "uuid=" : "name=") + "(?)");
            if (Database.useUUID) {
                statement.setString(1, player.getUniqueId().toString());
            } else {
                statement.setString(1, player.getName());
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement INSERT_USER(BasePlayer player) {
        try {
            PreparedStatement statement = AbyssalGames.getPlugin().getDataStore().getConnection().prepareStatement("REPLACE INTO " + TABLE_NAME + " (name, uuid, data) VALUES (?,?,?)");
            statement.setString(1, player.getPlayer().getName());
            statement.setString(2, player.getPlayer().getUniqueId().toString());
            statement.setString(3, player.toString());
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement UPDATE_USER(BasePlayer player) {
        try {
            PreparedStatement statement = AbyssalGames.getPlugin().getDataStore().getConnection().prepareStatement("UPDATE " + TABLE_NAME + " SET data=(?) WHERE " + (Database.useUUID ? "uuid=" : "name=") + "(?)");
            statement.setString(1, player.toString());
            if (Database.useUUID) {
                statement.setString(2, player.getPlayer().getUniqueId().toString());
            } else {
                statement.setString(2, player.getPlayer().getName());
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
