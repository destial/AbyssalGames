package xyz.destial.abyssalgames.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.MatchPlayerDeathEvent;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.manager.MatchManager;

public class PlayerInteractListener implements Listener {
    public PlayerInteractListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (matchManager.isRunning()) {
            if (!matchManager.getMatch().getPlaceableBlocks().contains(e.getBlock().getType())) {
                e.setCancelled(true);
                return;
            }
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        e.setExpToDrop(0);
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (matchManager.isRunning()) {
            if (!matchManager.getMatch().getBreakableBlocks().contains(e.getBlock().getType())) {
                e.setCancelled(true);
                return;
            }
            e.getBlock().setType(Material.AIR);
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (matchManager.isStarting() || matchManager.isStartingDeathmatch()) {
            if (matchManager.isPlaying(e.getPlayer())) {
                if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                    e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getTo().getY(), e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch()));
                }
            }
        }
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (matchManager.isRunning()) {
            Player player = (Player) e.getPlayer();
            if (matchManager.isSpectating(player)) {
                e.setCancelled(true);
            }
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        Player victim = e.getEntity();
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (matchManager.isRunning()) {
            if (matchManager.getMatch().getPlayers().containsKey(victim.getName())) {
                MatchPlayer victimMatch = matchManager.getMatch().getPlayers().get(victim.getName());
                victimMatch.getBasePlayer().getPlayer().setMaxHealth(20);
                victimMatch.getBasePlayer().getPlayer().setFoodLevel(20);
                victimMatch.getBasePlayer().getPlayer().setHealth(victimMatch.getBasePlayer().getPlayer().getMaxHealth());
                if (victim.getKiller() != null) {
                    MatchPlayer killer = matchManager.getMatch().getPlayers().get(victim.getKiller().getName());
                    matchManager.call(new MatchPlayerDeathEvent(matchManager.getMatch(), victimMatch, killer));
                    return;
                }
                matchManager.call(new MatchPlayerDeathEvent(matchManager.getMatch(), victimMatch, null));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            if (!matchManager.isRunning() || matchManager.isGracePeriod()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLost(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            if (!matchManager.isRunning() || matchManager.isGracePeriod()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        e.setCancelled(true);
    }
}
