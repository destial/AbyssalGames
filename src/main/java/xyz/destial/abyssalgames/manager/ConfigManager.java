package xyz.destial.abyssalgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.map.Lobby;
import xyz.destial.abyssalgames.utils.Hub;

import java.io.File;

public class ConfigManager {
    private final FileConfiguration config;
    private final File dataFolder;
    private final Hub hub;
    private final int matchDuration;
    private final int deathmatchDuration;
    private final int gracePeriod;
    private final int refillChests;
    private final boolean kickOnJoin;

    public ConfigManager(FileConfiguration config, File dataFolder) {
        this.config = config;
        this.dataFolder = dataFolder;

        World lobbyWorld = Bukkit.getServer().getWorld(config.getString("lobby.world"));
        double lobbySpawnX = config.getDouble("lobby.x");
        double lobbySpawnY = config.getDouble("lobby.y");
        double lobbySpawnZ = config.getDouble("lobby.z");
        float lobbySpawnYaw = (float) config.getDouble("lobby.yaw");
        float lobbySpawnPitch = (float) config.getDouble("lobby.pitch");
        Location lobbySpawn = new Location(lobbyWorld, lobbySpawnX, lobbySpawnY, lobbySpawnZ, lobbySpawnYaw, lobbySpawnPitch);
        AbyssalGames.getPlugin().getMatchManager().setLobby(new Lobby(lobbySpawn));

        hub = new Hub(config.getString("hub"));

        matchDuration = config.getInt("time.game", 1200);
        deathmatchDuration = config.getInt("time.deathmatch", 120);
        gracePeriod = config.getInt("time.grace", 0);
        refillChests = config.getInt("time.refill", 600);
        kickOnJoin = config.getBoolean("spectators.kick-on-join", false);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public Hub getHub() {
        return hub;
    }

    public int getDeathmatchDuration() {
        return deathmatchDuration;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public int getMatchDuration() {
        return matchDuration;
    }

    public int getRefillChestsTime() {
        return refillChests;
    }

    public void save() {
        AbyssalGames.getPlugin().saveConfig();
    }

    public boolean kickOnJoin() {
        return kickOnJoin;
    }
}
