package xyz.destial.abyssalgames;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.destial.abyssalgames.listeners.*;
import xyz.destial.abyssalgames.manager.ChestManager;
import xyz.destial.abyssalgames.commands.CommandHandler;
import xyz.destial.abyssalgames.database.Database;
import xyz.destial.abyssalgames.manager.ConfigManager;
import xyz.destial.abyssalgames.manager.MapManager;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.manager.ScoreboardManager;
import xyz.destial.abyssalgames.manager.MessageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public final class AbyssalGames extends JavaPlugin {
    private static AbyssalGames plugin;
    private ConfigManager configManager;
    private MatchManager matchManager;
    private CommandHandler commandHandler;
    private MapManager mapManager;
    private ChestManager chestManager;
    private ScoreboardManager scoreboardManager;
    private Database database;

    public AbyssalGames() {
        super();
    }

    @Override
    public void onEnable() {
        plugin = this;

        File mapsFolder = new File("maps/");
        File mapsConfig = new File(getDataFolder(), "maps.yml");
        mapsFolder.mkdir();
        mapManager = new MapManager(YamlConfiguration.loadConfiguration(mapsConfig), mapsFolder);
        mapManager.deleteExistingMaps();
        mapManager.loadMapNames();

        matchManager = new MatchManager();
        reloadConfig();
        registerListeners();
    }

    @Override
    public void onDisable() {
        saveConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(MessageManager.KICK_RELOADING);
        }
        mapManager.deleteExistingMaps();
        if (database.getConnection() != null) {
            try {
                database.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static AbyssalGames getPlugin() {
        return plugin;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("contents.yml", false);
        saveResource("maps.yml", false);
        saveResource("scoreboard.yml", false);
        File messages = new File(getDataFolder(), "messages.yml");
        new MessageManager(YamlConfiguration.loadConfiguration(messages), getDataFolder());

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(MessageManager.KICK_RELOADING);
        }

        boolean startup = configManager == null;
        configManager = new ConfigManager(getConfig(), getDataFolder());

        File contents = new File(getDataFolder(), "contents.yml");
        chestManager = new ChestManager(YamlConfiguration.loadConfiguration(contents), getDataFolder());

        File scoreboard = new File(getDataFolder(), "scoreboard.yml");
        scoreboardManager = new ScoreboardManager(YamlConfiguration.loadConfiguration(scoreboard));

        database = new Database(getConfig());

        if (!startup) {
            getLogger().log(Level.INFO, "Reloaded config");
        }
    }

    public void registerListeners() {
        commandHandler = new CommandHandler();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents(new MatchListener(), getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents(new CountdownListener(), getPlugin());
        Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(), getPlugin());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public MatchManager getMatchManager() {
        return matchManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ChestManager getChestManager() {
        return chestManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static String getServerProperty(String s) {
        Properties pr = new Properties();
        try {
            FileInputStream in = new FileInputStream(new File("server.properties"));
            pr.load(in);
            return pr.getProperty(s, "");
        } catch (IOException e) {
            // do nothing
        }
        return "";
    }

    public Database getDataStore() {
        return database;
    }
}
