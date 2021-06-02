package xyz.destial.abyssalgames.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.CountdownCallEvent;
import xyz.destial.abyssalgames.events.CountdownCompleteEvent;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.state.MatchState;

public class CountdownListener implements Listener {
    public CountdownListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCountdownCall(CountdownCallEvent e) {
        Bukkit.broadcastMessage(e.getMessage());
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        matchManager.playSound(Sound.CLICK);

        if (e.getCountdown().getTime() == (AbyssalGames.getPlugin().getConfigManager().getMatchDuration() - AbyssalGames.getPlugin().getConfigManager().getRefillChestsTime())) {
            AbyssalGames.getPlugin().getChestManager().refill();
        }

        if (e.getCountdown().getTime() == 30 && matchManager.isRunning()) {
            matchManager.prepareDeathmatch();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCountdownComplete(CountdownCompleteEvent e) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        if (e.getMatch().getState() == MatchState.PREPARING) {
            for (int i = 0; i < matchManager.getWaitingPlayers().size(); i++) {
                matchManager.addPlayer(matchManager.getWaitingPlayers().get(i), e.getMatch().getMap().getSpawnPoints().get(i));
            }
            matchManager.getWaitingPlayers().clear();
            matchManager.toMatch();
            AbyssalGames.getPlugin().getMatchManager().startMatch();

        } else if (e.getMatch().getState() == MatchState.PREPARING_DEATHMATCH) {
            matchManager.toMatch();
            AbyssalGames.getPlugin().getMatchManager().startDeathmatch();

        } else if (e.getMatch().getState() == MatchState.STARTING) {
            matchManager.beginMatch();
            matchManager.playSound(Sound.ENDERDRAGON_GROWL);

        } else if (e.getMatch().getState() == MatchState.STARTING_DEATHMATCH) {
            matchManager.beginDeathmatch();
            matchManager.playSound(Sound.ENDERDRAGON_GROWL);

        } else if (e.getMatch().getState() == MatchState.RUNNING) {
            matchManager.stopGracePeriod();

        } else if (e.getMatch().getState() == MatchState.ENDING) {
            matchManager.endMatch();
        }
    }
}
