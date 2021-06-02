package xyz.destial.abyssalgames.manager;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.map.Map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class MapManager {
    private final java.util.Map<String, Map> maps;
    private final ArrayList<String> mapNames;
    private final FileConfiguration mapsConfig;
    private final File dataFolder;
    public MapManager(FileConfiguration mapsConfig, File dataFolder) {
        this.mapsConfig = mapsConfig;
        this.dataFolder = dataFolder;
        maps = new ConcurrentHashMap<>();
        mapNames = new ArrayList<>();
    }

    public Map newMap(World world) {
        Map map = new Map(world, world.getName());
        addMap(map);
        AbyssalGames.getPlugin().getLogger().log(Level.INFO, "Loaded " + map.getWorld().getName());
        save();
        return map;
    }

    public void deleteExistingMaps() {
        for (String list : Objects.requireNonNull(dataFolder.list())) {
            File worldFile = new File(list + File.separator);
            if (worldFile.exists()) {
                try {
                    Bukkit.unloadWorld(list, false);
                    FileUtils.deleteDirectory(worldFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addMap(Map map) {
        maps.put(map.getName(), map);
        mapNames.add(map.getName());
    }

    public java.util.Map<String, Map> getMaps() {
        return maps;
    }

    public Map getMap(String mapName) {
        return maps.get(mapName);
    }

    public void loadMap(String name) {
        Map map = new Map(null, name);
        addMap(map);
    }

    public void loadMapNames() {
        for (String list : dataFolder.list()) {
            loadMap(list);
        }
    }

    public World createMap(Map map) {
        if (map.getWorld() == null) {
            try {
                File mapFile = new File(dataFolder + File.separator + map.getName());
                File worldFolder = new File(map.getName() + File.separator);
                worldFolder.mkdir();
                FileUtils.copyDirectory(mapFile, worldFolder);
                WorldCreator worldCreator = new WorldCreator(map.getName());
                World world = Bukkit.createWorld(worldCreator);
                map.setWorld(world);
                return world;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return map.getWorld();
    }

    public void unloadMap(Map map) {
        if (map != null && map.getWorld() != null) {
            File worldFile = new File(map.getName() + File.separator);
            Bukkit.unloadWorld(map.getWorld(), false);
            try {
                FileUtils.deleteDirectory(worldFile);
                AbyssalGames.getPlugin().getLogger().info("Unloaded " + map.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void save() {
        mapsConfig.set("maps", mapNames);
        try {
            mapsConfig.save(new File(AbyssalGames.getPlugin().getDataFolder(), "maps.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
