package xyz.destial.abyssalgames.chest;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class BaseChest {
    private final Block block;
    private final Location location;
    private final Inventory inventory;

    public BaseChest(Block block, Inventory inventory) {
        this.block = block;
        this.location = block.getLocation();
        this.inventory = inventory;
    }

    public Location getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Block getBlock() {
        return block;
    }

    public void refill() {}

    public void playSound(boolean open) {
        location.getWorld().playSound(location, open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1, 1);
    }
}
