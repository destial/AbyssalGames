package xyz.destial.abyssalgames.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import xyz.destial.abyssalgames.chest.Tier1Chest;
import xyz.destial.abyssalgames.chest.Tier2Chest;
import xyz.destial.abyssalgames.chest.Tier3Chest;

import java.io.File;
import java.util.*;

public class ChestManager {
    private final FileConfiguration config;
    private final File dataFolder;
    private final int maxContents;
    private final int minContents;

    private final ArrayList<ItemStack> tier1Content;
    private final ArrayList<ItemStack> tier2Content;
    private final ArrayList<ItemStack> tier3Content;

    private final Map<Location, Tier1Chest> tier1Chests;
    private final Map<Location, Tier2Chest> tier2Chests;
    private final Map<Location, Tier3Chest> tier3Chests;

    public ChestManager(FileConfiguration config, File dataFolder) {
        this.config = config;
        this.dataFolder = dataFolder;

        tier1Chests = new HashMap<>();
        tier2Chests = new HashMap<>();
        tier3Chests = new HashMap<>();


        tier1Content = new ArrayList<>();
        tier2Content = new ArrayList<>();
        tier3Content = new ArrayList<>();

        maxContents = config.getInt("max-contents", 3);
        minContents = config.getInt("min-contents", 6);

        List<String> tier1RawContent = config.getStringList("tier-1");
        for (String content : tier1RawContent) {
            String[] arguments = content.split(" : ");
            String itemName = arguments[0];
            if (!itemName.isEmpty()) {
                Material material = Material.getMaterial(itemName);
                if (material != null) {
                    ItemStack itemStack;
                    try {
                        if (arguments[1] != null) {
                            itemStack = new ItemStack(material, Integer.parseInt(arguments[1]));
                        } else {
                            itemStack = new ItemStack(material, 1);
                        }
                        tier1Content.add(itemStack);
                    } catch(NumberFormatException e) {
                        itemStack = new ItemStack(material, 1);
                        tier1Content.add(itemStack);
                    }
                }
            }
        }

        List<String> tier2RawContent = config.getStringList("tier-2");
        for (String content : tier2RawContent) {
            String[] arguments = content.split(" : ");
            String itemName = arguments[0];
            if (!itemName.isEmpty()) {
                Material material = Material.getMaterial(itemName);
                if (material != null) {
                    ItemStack itemStack;
                    try {
                        if (arguments[1] != null) {
                            itemStack = new ItemStack(material, Integer.parseInt(arguments[1]));
                        } else {
                            itemStack = new ItemStack(material, 1);
                        }
                        tier2Content.add(itemStack);
                    } catch(NumberFormatException e) {
                        itemStack = new ItemStack(material, 1);
                        tier2Content.add(itemStack);
                    }
                }
            }
        }

        List<String> tier3RawContent = config.getStringList("tier-3");
        for (String content : tier3RawContent) {
            String[] arguments = content.split(" : ");
            String itemName = arguments[0];
            if (!itemName.isEmpty()) {
                Material material = Material.getMaterial(itemName);
                if (material != null) {
                    ItemStack itemStack;
                    try {
                        if (arguments[1] != null) {
                            itemStack = new ItemStack(material, Integer.parseInt(arguments[1]));
                        } else {
                            itemStack = new ItemStack(material, 1);
                        }
                        tier3Content.add(itemStack);
                    } catch(NumberFormatException e) {
                        itemStack = new ItemStack(material, 1);
                        tier3Content.add(itemStack);
                    }
                }
            }
        }
    }

    public void clear() {
        tier1Chests.clear();
        tier2Chests.clear();
        tier3Chests.clear();
    }

    public void refill() {
        for (Tier1Chest chest : tier1Chests.values()) {
            chest.refill();
        }
        for (Tier2Chest chest : tier2Chests.values()) {
            chest.refill();
        }
        for (Tier3Chest chest : tier3Chests.values()) {
            chest.refill();
        }
    }

    public ItemStack getRandomTier1Content() {
        Random random = new Random();
        return tier1Content.get(random.nextInt(tier1Content.size() - 1));
    }

    public ItemStack getRandomTier2Content() {
        Random random = new Random();
        return tier2Content.get(random.nextInt(tier2Content.size() - 1));
    }

    public ItemStack getRandomTier3Content() {
        Random random = new Random();
        return tier3Content.get(random.nextInt(tier3Content.size() - 1));
    }

    public Map<Location, Tier1Chest> getTier1Chests() {
        return tier1Chests;
    }

    public Map<Location, Tier2Chest> getTier2Chests() {
        return tier2Chests;
    }

    public Map<Location, Tier3Chest> getTier3Chests() {
        return tier3Chests;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public int getMaxContents() {
        return maxContents;
    }

    public int getMinContents() {
        return minContents;
    }
}
