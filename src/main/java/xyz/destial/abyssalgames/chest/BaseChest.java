package xyz.destial.abyssalgames.chest;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BaseChest {
    private final Block block;
    private final BlockState blockState;
    private final Location location;
    private Inventory inventory;

    public BaseChest(BlockState chest) {
        this.block = chest.getBlock();
        this.blockState = chest;
        this.location = chest.getLocation();
        if (chest instanceof Chest) {
            inventory = ((Chest) chest).getInventory();
            inventory.clear();
        }
        if (chest instanceof DoubleChest) {
            inventory = ((DoubleChest) chest).getInventory();
            inventory.clear();
        }
    }

    public BaseChest(Block block, Inventory inventory) {
        this.block = block;
        this.blockState = block.getState();
        this.location = block.getLocation();
        this.inventory = inventory;
    }

    public BlockState getBlockState() {
        return blockState;
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
        getLocation().getWorld().playSound(getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1, 1);
    }
}
