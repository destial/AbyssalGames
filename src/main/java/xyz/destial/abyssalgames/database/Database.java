package xyz.destial.abyssalgames.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.match.BasePlayer;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class Database {
    private Connection connection;
    private final FileConfiguration config;
    public static boolean useUUID;
    public Database(FileConfiguration config) {
        this.config = config;
        Database.useUUID = config.getBoolean("mysql.uuid", false);
        if (config.getBoolean("mysql.enabled")) {
            String address = config.getString("mysql.address", "localhost");
            String port = config.getString("mysql.port", "3306");
            String database = config.getString("mysql.database", "abyssalgames");
            String username = config.getString("mysql.username", "");
            String password = config.getString("mysql.password", "");
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database + "?useSSL=false", username, password)) {
                this.connection = connection;
                PreparedStatement statement = connection.prepareStatement(Query.CREATE_TABLE);
                statement.executeUpdate();
                return;
            } catch (SQLException e) {
                this.connection = null;
            }
        }

        File dataFolder = new File(AbyssalGames.getPlugin().getDataFolder(), "data.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                AbyssalGames.getPlugin().getLogger().log(Level.SEVERE, "File write error: data.db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            PreparedStatement statement = connection.prepareStatement(Query.CREATE_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            AbyssalGames.getPlugin().getLogger().log(Level.SEVERE,"SQLite exception on initialization", e);
            connection = null;
        } catch (ClassNotFoundException e) {
            AbyssalGames.getPlugin().getLogger().log(Level.SEVERE, "No SQLite library installed!");
            connection = null;
        }
    }

    public String getData(Player player) {
        if (connection == null) return null;
        try {
            PreparedStatement statement = Query.FETCH_USER(player);
            if (statement != null) {
                ResultSet result = statement.executeQuery();
                ArrayList<String> array = new ArrayList<>();
                if (result.next()) {
                    array.add(result.getString("data"));
                }
                if (array.size() == 1) {
                    return array.get(0);
                }
            }
            return "";
        } catch (SQLException e) {
            BasePlayer basePlayer = new BasePlayer(player);
            insertData(basePlayer);
        }
        return "";
    }

    public void insertData(BasePlayer player) {
        if (connection == null) return;
        try {
            PreparedStatement statement = Query.INSERT_USER(player);
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(BasePlayer player) {
        if (connection == null) return;
        try {
            PreparedStatement statement = Query.UPDATE_USER(player);
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            insertData(player);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Connection getConnection() {
        return connection;
    }
}
