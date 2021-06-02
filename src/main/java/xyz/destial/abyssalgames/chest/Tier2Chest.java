package xyz.destial.abyssalgames.chest;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import xyz.destial.abyssalgames.AbyssalGames;

import java.util.Random;

public class Tier2Chest extends BaseChest {
    public Tier2Chest(BlockState chest) {
        super(chest);
    }

    public Tier2Chest(Block chest, Inventory inventory) {
        super(chest, inventory);
    }

    @Override
    public void refill() {
        getInventory().clear();
        int filledSpots = 0;
        while (filledSpots < AbyssalGames.getPlugin().getChestManager().getMinContents()) {
            for (int i = 0; i < getInventory().getSize(); i++) {
                if (new Random().nextInt(12) == 0) {
                    if (getInventory().getItem(i) == null) {
                        getInventory().setItem(i, AbyssalGames.getPlugin().getChestManager().getRandomTier2Content());
                        filledSpots++;
                    }
                }
                if (filledSpots >= AbyssalGames.getPlugin().getChestManager().getMaxContents()) break;
            }
        }
    }
}
