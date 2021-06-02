package xyz.destial.abyssalgames.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.MatchPlayerDeathEvent;
import xyz.destial.abyssalgames.map.Lobby;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.match.Spectator;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.utils.ParseMessage;


public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (!matchManager.isLobby()) {
            if (!e.getPlayer().hasPermission("abyssal.spectate")) {
                e.disallow(PlayerLoginEvent.Result.KICK_FULL, matchManager.isRunning() || matchManager.isStartingDeathmatch() ? MessageManager.KICK_GAME_RUNNING : MessageManager.KICK_GAME_ENDING);
                return;
            }
        }
        e.allow();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().getInventory().clear();
        e.getPlayer().setExp(0);
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        BasePlayer basePlayer = matchManager.addAllPlayer(e.getPlayer());
        e.getPlayer().setLevel(basePlayer.getEXP());
        if (!matchManager.isLobby()) {
            if (matchManager.isRunning()) {
                Spectator spectator = matchManager.addSpectator(basePlayer);
                spectator.teleportToSpawnPoint();
            } else {
                e.getPlayer().kickPlayer(MessageManager.KICK_GAME_RUNNING);
            }
        } else {
            matchManager.addWaitingPlayer(e.getPlayer());
            Lobby lobby = matchManager.getLobby();
            if (lobby.getSpawn() != null) {
                lobby.teleport(e.getPlayer());
            }
            Bukkit.getServer().broadcastMessage(ParseMessage.rank(MessageManager.JOIN_MESSAGE, basePlayer));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        AbyssalGames.getPlugin().getScoreboardManager().cancelScoreboardUpdate(e.getPlayer().getUniqueId());
        MatchPlayer matchPlayer = matchManager.removePlayer(e.getPlayer());
        if (matchPlayer != null) {
            matchPlayer.getBasePlayer().setOnline(false);
            matchManager.call(new MatchPlayerDeathEvent(matchManager.getMatch(), null, matchPlayer, null));
        }

        Spectator spectator = matchManager.removeSpectator(e.getPlayer());
        if (spectator != null) {
            spectator.getBasePlayer().setOnline(false);
        }

        if (matchManager.removeWaitingPlayer(e.getPlayer())) {
            BasePlayer basePlayer = matchManager.removeAllPlayer(e.getPlayer());
            Bukkit.getServer().broadcastMessage(ParseMessage.rank(MessageManager.QUIT_MESSAGE, basePlayer));
        } else {
            matchManager.removeAllPlayer(e.getPlayer());
        }
    }
}
