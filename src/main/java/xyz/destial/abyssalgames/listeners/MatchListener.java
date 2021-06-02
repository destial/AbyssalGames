package xyz.destial.abyssalgames.listeners;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.*;
import xyz.destial.abyssalgames.manager.MapManager;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.match.Spectator;
import xyz.destial.abyssalgames.utils.ParseMessage;

import java.util.List;

public class MatchListener implements Listener {
    public MatchListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(MatchPlayerDeathEvent e) {
        e.getVictim().getBasePlayer().getPlayer().getWorld().strikeLightningEffect(e.getVictim().getBasePlayer().getPlayer().getLocation());
        if (e.getKiller() != null) {
            e.getKiller().getBasePlayer().addKill();
            e.getKiller().getBasePlayer().saveData();
            Bukkit.broadcastMessage(ParseMessage.death(MessageManager.DEATH_MESSAGE, e.getKiller().getBasePlayer().getPlayer(), e.getVictim().getBasePlayer().getPlayer()));
        } else {
            Bukkit.broadcastMessage(ParseMessage.unknownDeath(MessageManager.UNKNOWN_DEATH_MESSAGE, e.getVictim().getBasePlayer().getPlayer()));
        }
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        matchManager.removePlayer(e.getVictim().getBasePlayer().getPlayer());
        e.getVictim().getBasePlayer().addDeath();
        e.getVictim().getBasePlayer().saveData();
        e.getMatch().addDeath();
        if (e.getVictim().getBasePlayer().isOnline()) {
            e.getVictim().getBasePlayer().getPlayer().setMaxHealth(20);
            e.getVictim().getBasePlayer().getPlayer().setFoodLevel(20);
            e.getVictim().getBasePlayer().getPlayer().setHealth(e.getVictim().getBasePlayer().getPlayer().getMaxHealth());
            matchManager.addSpectator(e.getVictim().getBasePlayer());
            for (Spectator spectator1 : matchManager.getMatch().getSpectators().values()) {
                for (Spectator spectator2 : matchManager.getMatch().getSpectators().values()) {
                    if (!spectator1.equals(spectator2)) {
                        spectator1.getBasePlayer().getPlayer().showPlayer(spectator2.getBasePlayer().getPlayer());
                    }
                }
            }
        }

        if (matchManager.getMatch().getPlayers().size() > 1 && matchManager.getMatch().getPlayers().size() <= 3 && !matchManager.isDeathmatch()) {
            matchManager.prepareDeathmatch();
        } else if (matchManager.getMatch().getPlayers().size() <= 1) {
            if (matchManager.getMatch().getPlayers().size() == 1) {
                for (MatchPlayer player : matchManager.getMatch().getPlayers().values()) {
                    player.getBasePlayer().addGameWon();
                    player.getBasePlayer().saveData();
                }
            }
            matchManager.prepareEndMatch();
        }
    }

    @EventHandler
    public void onMatchPrepare(MatchPrepareEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        MapManager mapManager = AbyssalGames.getPlugin().getMapManager();
        mapManager.loadMap(matchManager.getVoteManager().getVotedMap().getName());
        matchManager.getLobby().deannounceVote();

        Bukkit.broadcastMessage(ParseMessage.map(MessageManager.MAP_VOTE_WON, matchManager.getVoteManager().getVotedMap()));

        List<String> breakableBlocks = AbyssalGames.getPlugin().getConfigManager().getConfig().getStringList("blocks.break");
        for (String block : breakableBlocks) {
            Material material = Material.getMaterial(block);
            if (material != null) {
                e.getMatch().addBreakableBlock(material);
                AbyssalGames.getPlugin().getLogger().info("Added " + material.name() + " to the breakable blocks");
            }
        }
        List<String> placeableBlocks = AbyssalGames.getPlugin().getConfigManager().getConfig().getStringList("blocks.place");
        for (String block : placeableBlocks) {
            Material material = Material.getMaterial(block);
            if (material != null) {
                e.getMatch().addPlaceableBlock(material);
                AbyssalGames.getPlugin().getLogger().info("Added " + material.name() + " to the placeable blocks");
            }
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        for (MatchPlayer matchPlayer : e.getMatch().getPlayers().values()) {
            matchPlayer.getBasePlayer().getPlayer().getInventory().clear();
        }
        matchManager.toMatch();

        for (MatchPlayer player : matchManager.getMatch().getPlayers().values()) {
            player.getBasePlayer().addGame();
            player.getBasePlayer().saveData();
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        MapManager mapManager = AbyssalGames.getPlugin().getMapManager();
        matchManager.toLobby();

        Match match = e.getMatch();
        mapManager.unloadMap(match.getMap());
        matchManager.setMatch(null);
        matchManager.getVoteManager().createMaps();
        matchManager.getLobby().announceVote();
        AbyssalGames.getPlugin().getLogger().info("Unloaded match");
    }

    @EventHandler
    public void onDeathmatchStart(MatchDeathMatchEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        matchManager.toMatch();
        AbyssalGames.getPlugin().getChestManager().refill();

        if (matchManager.getMatch().getPlayers().size() <= 1) {
            matchManager.endMatch();
        }
    }

    @EventHandler
    public void onMatchWin(MatchWinEvent e) {
        e.getMatch().getMap().getWorld().setTime(14000);
        for (Location spawpoints : e.getMatch().getMap().getSpawnPoints()) {
            Firework fw = (Firework) e.getMatch().getMap().getWorld().spawnEntity(spawpoints, EntityType.FIREWORK);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).trail(true).build());
            fw.setFireworkMeta(meta);
        }
    }
}
