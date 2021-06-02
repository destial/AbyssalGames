package xyz.destial.abyssalgames.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.destial.abyssalgames.AbyssalGames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    private final ArrayList<Location> spawnPoints;
    private FileConfiguration config;
    private int votes;
    private World world;
    private String name;

    public Map(World world, String name) {
        this.world = world;
        this.name = name;
        spawnPoints = new ArrayList<>();
        votes = 0;
    }

    public void loadData() {
        File mapsFolder = AbyssalGames.getPlugin().getMapManager().getDataFolder();
        try {
            mapsFolder.mkdir();
            File mapFolder = new File(mapsFolder + File.separator + getName());
            mapFolder.mkdir();
            File mapFile = new File(mapFolder, "map.yml");
            if (mapFile.createNewFile()) {
                AbyssalGames.getPlugin().getLogger().info("Creating new map.yml for " + getName());
            }
            config = YamlConfiguration.loadConfiguration(mapFile);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        this.votes++;
    }

    public void clearVotes() {
        this.votes = 0;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public void loadSpawnPoints() {
        spawnPoints.clear();
        File mapsFolder = AbyssalGames.getPlugin().getMapManager().getDataFolder();
        File mapFile = new File(mapsFolder + File.separator + getName(), "map.yml");
        if (!mapFile.exists()) {
            AbyssalGames.getPlugin().getLogger().warning("Unable to load map.yml of " + getName());
        } else {
            config = YamlConfiguration.loadConfiguration(mapFile);
            for (String key : config.getConfigurationSection("spawnpoint").getKeys(false)) {
                double x = config.getDouble("spawnpoint." + key + ".x");
                double y = config.getDouble("spawnpoint." + key + ".y", -1);
                double z = config.getDouble("spawnpoint." + key + ".z");
                float yaw = (float) config.getDouble("spawnpoint." + key + ".yaw");
                float pitch = (float) config.getDouble("spawnpoint." + key + ".pitch");
                if (y == -1) {
                    break;
                }
                Location spawnPoint = new Location(world, x, y, z, yaw, pitch);
                spawnPoints.add(spawnPoint);
                AbyssalGames.getPlugin().getLogger().warning("Loaded spawnpoint " + key + " for " + getName());
            }
            if (spawnPoints.size() == 0) {
                AbyssalGames.getPlugin().getLogger().warning("No spawnpoints were added for " + getName());
            }
        }
    }

    public boolean addSpawnPoint(Location location) {
        if (spawnPoints.size() < 24) {
            spawnPoints.add(location);
            save();
            return true;
        }
        return false;
    }

    public boolean removeSpawnPoint(int index) {
        if (spawnPoints.size() > 0) {
            if (index > 0 && index <= spawnPoints.size()) {
                spawnPoints.remove(index - 1);
            }
            save();
            return true;
        }
        return false;
    }

    public void saveSpawnPoints() {
        for (int i = 1; i <= spawnPoints.size(); i++) {
            config.set("spawnpoint." + i + ".x", spawnPoints.get(i - 1).getX());
            config.set("spawnpoint." + i + ".y", spawnPoints.get(i - 1).getY());
            config.set("spawnpoint." + i + ".z", spawnPoints.get(i - 1).getZ());
            config.set("spawnpoint." + i + ".yaw", spawnPoints.get(i - 1).getYaw());
            config.set("spawnpoint." + i + ".pitch", spawnPoints.get(i - 1).getPitch());
        }
    }

    public void save() {
        saveSpawnPoints();
        File mapsFolder = AbyssalGames.getPlugin().getMapManager().getDataFolder();
        try {
            mapsFolder.mkdir();
            File mapFolder = new File(mapsFolder + File.separator + getName());
            mapFolder.mkdir();
            File mapFile = new File(mapFolder, "map.yml");
            if (mapFile.createNewFile()) {
                AbyssalGames.getPlugin().getLogger().info("Creating new map.yml for " + getName());
            }
            config.save(mapFile);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
