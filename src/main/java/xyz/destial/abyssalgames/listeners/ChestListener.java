package xyz.destial.abyssalgames.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.chest.Tier1Chest;
import xyz.destial.abyssalgames.chest.Tier2Chest;
import xyz.destial.abyssalgames.chest.Tier3Chest;
import xyz.destial.abyssalgames.manager.ChestManager;

public class ChestListener implements Listener {
    private final ChestManager chestManager;
    public ChestListener() {
        chestManager = AbyssalGames.getPlugin().getChestManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void registerChests(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Material material = e.getClickedBlock().getType();

        if (material != Material.CHEST && material != Material.ENDER_CHEST && material != Material.TRAPPED_CHEST) return;
        e.setCancelled(true);
        if (material == Material.CHEST) {
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                DoubleChest doubleChest = ((DoubleChest) e.getClickedBlock().getState());
                Chest left = (Chest) doubleChest.getLeftSide();
                Chest right = (Chest) doubleChest.getRightSide();
                if (chestManager.getTier1Chests().containsKey(left.getLocation())) return;
                if (chestManager.getTier1Chests().containsKey(right.getLocation())) return;
            }
            if (chestManager.getTier1Chests().containsKey(e.getClickedBlock().getLocation())) return;

            Inventory inventory = Bukkit.createInventory(null, 27, "Tier 1");
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                inventory = Bukkit.createInventory(null, 54, "Tier 1");
            }
            Tier1Chest chest = new Tier1Chest(e.getClickedBlock(), inventory);
            chest.refill();
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                DoubleChest doubleChest = ((DoubleChest) e.getClickedBlock().getState());
                Chest left = (Chest) doubleChest.getLeftSide();
                Chest right = (Chest) doubleChest.getRightSide();
                chestManager.getTier1Chests().put(left.getLocation(), chest);
                chestManager.getTier1Chests().put(right.getLocation(), chest);
            } else {
                chestManager.getTier1Chests().put(chest.getLocation(), chest);
            }

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }

        if (material == Material.TRAPPED_CHEST) {
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                DoubleChest doubleChest = ((DoubleChest) e.getClickedBlock().getState());
                Chest left = (Chest) doubleChest.getLeftSide();
                Chest right = (Chest) doubleChest.getRightSide();
                if (chestManager.getTier2Chests().containsKey(left.getLocation())) return;
                if (chestManager.getTier2Chests().containsKey(right.getLocation())) return;
            }
            if (chestManager.getTier2Chests().containsKey(e.getClickedBlock().getLocation())) return;

            Inventory inventory = Bukkit.createInventory(null, 27, "Tier 2");
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                inventory = Bukkit.createInventory(null, 54, "Tier 2");
            }
            Tier2Chest chest = new Tier2Chest(e.getClickedBlock(), inventory);
            chest.refill();
            if (e.getClickedBlock().getState() instanceof DoubleChest) {
                DoubleChest doubleChest = ((DoubleChest) e.getClickedBlock().getState());
                Chest left = (Chest) doubleChest.getLeftSide();
                Chest right = (Chest) doubleChest.getRightSide();
                chestManager.getTier2Chests().put(left.getLocation(), chest);
                chestManager.getTier2Chests().put(right.getLocation(), chest);
            } else {
                chestManager.getTier2Chests().put(chest.getLocation(), chest);
            }

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }

        if (material == Material.ENDER_CHEST) {
            if (chestManager.getTier3Chests().containsKey(e.getClickedBlock().getLocation())) return;

            Inventory inventory = Bukkit.createInventory(null, 27, "Tier 3");
            Tier3Chest chest = new Tier3Chest(e.getClickedBlock(), inventory);
            chest.refill();

            chestManager.getTier3Chests().put(chest.getLocation(), chest);

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpenChest(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Material material = e.getClickedBlock().getType();

        if (material != Material.CHEST && material != Material.ENDER_CHEST && material != Material.TRAPPED_CHEST) return;
        e.setCancelled(true);
        if (material == Material.CHEST) {
            Tier1Chest chest = chestManager.getTier1Chests().get(e.getClickedBlock().getLocation());
            if (chest == null) return;

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }

        if (material == Material.TRAPPED_CHEST) {
            Tier2Chest chest = chestManager.getTier2Chests().get(e.getClickedBlock().getLocation());
            if (chest == null) return;

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }

        if (material == Material.ENDER_CHEST) {
            Tier3Chest chest = chestManager.getTier3Chests().get(e.getClickedBlock().getLocation());
            if (chest == null) return;

            chest.playSound(true);
            e.getPlayer().openInventory(chest.getInventory());
        }
    }

    @EventHandler
    public void onCloseChest(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;

            Tier1Chest tier1Chest = chestManager.getTier1Chests().get(chest.getLocation());
            if (tier1Chest != null) {
                tier1Chest.playSound(false);
                return;
            }

            Tier2Chest tier2Chest = chestManager.getTier2Chests().get(chest.getLocation());
            if (tier2Chest != null) {
                tier2Chest.playSound(false);
                return;
            }

            Tier3Chest tier3Chest = chestManager.getTier3Chests().get(chest.getLocation());
            if (tier3Chest != null) {
                tier3Chest.playSound(false);
            }
        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) holder;

            Tier1Chest tier1Chest = chestManager.getTier1Chests().get(chest.getLocation());
            if (tier1Chest != null) {
                tier1Chest.playSound(false);
                return;
            }

            Tier2Chest tier2Chest = chestManager.getTier2Chests().get(chest.getLocation());
            if (tier2Chest != null) {
                tier2Chest.playSound(false);
            }
        }
    }
}
