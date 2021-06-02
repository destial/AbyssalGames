package xyz.destial.abyssalgames.manager;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.*;
import xyz.destial.abyssalgames.map.Lobby;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.match.Spectator;
import xyz.destial.abyssalgames.match.state.MatchState;
import xyz.destial.abyssalgames.utils.Countdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MatchManager {
    private final ArrayList<BasePlayer> waitingPlayers;
    private final HashMap<UUID, BasePlayer> allPlayers;
    private final VoteManager voteManager;
    private Countdown currentCountdown;
    private Countdown gracePeriod;
    private Match match;
    private Lobby lobby;

    public MatchManager() {
        match = null;
        lobby = null;
        waitingPlayers = new ArrayList<>();
        allPlayers = new HashMap<>();
        voteManager = new VoteManager();
        currentCountdown = null;
        gracePeriod = null;
    }

    public void clear() {
        match = null;
        lobby = null;
        waitingPlayers.clear();
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void prepareMatch() {
        if (match == null && voteManager.getVotedMap() != null) {
            setMatch(new Match(voteManager.getVotedMap()));
            MapManager mapManager = AbyssalGames.getPlugin().getMapManager();
            for (Entity entity : mapManager.createMap(match.getMap()).getEntities()) {
                entity.remove();
            }
            match.getMap().loadSpawnPoints();
            for (Location spawn : match.getMap().getSpawnPoints()) {
                spawn.getWorld().loadChunk(spawn.getChunk());
            }
            match.setState(MatchState.PREPARING);
            startCountdown();
            call(new MatchPrepareEvent(match));
        }
    }

    public void startMatch() {
        if (match != null) {
            match.setState(MatchState.STARTING);
            startCountdown();
            call(new MatchStartEvent(match));
        }
    }

    public void prepareEndMatch() {
        if (match != null) {
            match.setState(MatchState.ENDING);
            MatchPlayer winner = null;
            for (MatchPlayer player : match.getPlayers().values()) {
                winner = player;
            }
            call(new MatchWinEvent(match, winner));
            startCountdown();
        }
    }

    public void endMatch() {
        if (match != null) {
            call(new MatchEndEvent(match));
            AbyssalGames.getPlugin().getChestManager().clear();
        }
    }

    public void prepareDeathmatch() {
        if (match != null) {
            match.setState(MatchState.PREPARING_DEATHMATCH);
            startCountdown();
        }
    }

    public void startDeathmatch() {
        if (match != null) {
            match.setState(MatchState.STARTING_DEATHMATCH);
            startCountdown();
            call(new MatchDeathMatchEvent(match));
        }
    }

    public void clearCountdown() {
        if (currentCountdown != null) {
            currentCountdown.stopTimer();
            currentCountdown = null;
        }
    }

    public void beginMatch() {
        if (match != null) {
            match.setState(MatchState.RUNNING);
            if (match.getPlayers().size() == 0) {
                prepareEndMatch();
                return;
            }
            if (AbyssalGames.getPlugin().getConfigManager().getGracePeriod() > 0) {
                gracePeriod = new Countdown(match, AbyssalGames.getPlugin().getConfigManager().getGracePeriod(), MessageManager.GRACE_PERIOD);
                startGracePeriod();
            }
            Bukkit.broadcastMessage(MessageManager.GAME_BEGIN_MESSAGE);
            startCountdown();
        }
    }

    public void beginDeathmatch() {
        if (match != null) {
            match.setState(MatchState.DEATHMATCH);
            startCountdown();
        }
    }

    public void startGracePeriod() {
        if (gracePeriod != null) {
            gracePeriod.startTimer();
        }
    }

    public void stopGracePeriod(){
        if (gracePeriod != null) {
            gracePeriod.stopTimer();
            gracePeriod = null;
        }
    }

    public void playSound(Sound sound, float volume, float pitch) {
        for (BasePlayer basePlayer : allPlayers.values()) {
            basePlayer.getPlayer().playSound(basePlayer.getPlayer().getLocation(), sound, volume, pitch);
        }
    }

    public void playSound(Sound sound) {
        playSound(sound, 1, 1);
    }

    public void startCountdown() {
        if (match != null && currentCountdown == null) {
            switch (match.getState()) {
                case STARTING_DEATHMATCH:
                    currentCountdown = new Countdown(match, 15, MessageManager.DEATHMATCH_STARTING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case STARTING:
                    currentCountdown = new Countdown(match, 15, MessageManager.GAME_STARTING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case PREPARING_DEATHMATCH:
                    currentCountdown = new Countdown(match, 30, MessageManager.DEATHMATCH_STARTING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case PREPARING:
                    currentCountdown = new Countdown(match, 30, MessageManager.GAME_STARTING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case RUNNING:
                    currentCountdown = new Countdown(match, AbyssalGames.getPlugin().getConfigManager().getMatchDuration(), MessageManager.DEATHMATCH_STARTING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case DEATHMATCH:
                    currentCountdown = new Countdown(match, AbyssalGames.getPlugin().getConfigManager().getDeathmatchDuration(), MessageManager.DEATHMATCH_START_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                case ENDING:
                    currentCountdown = new Countdown(match, 10, MessageManager.GAME_ENDING_MESSAGE);
                    currentCountdown.startTimer();
                    break;
                default:
                    break;
            }
        } else {
            clearCountdown();
            startCountdown();
        }
    }

    public void call(Event e) {
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    public ArrayList<BasePlayer> getWaitingPlayers() {
        return waitingPlayers;
    }

    public void addPlayer(BasePlayer player, Location spawnPoint) {
        MatchPlayer matchPlayer = new MatchPlayer(player, match, spawnPoint);
        match.addPlayer(matchPlayer);
    }

    public void addWaitingPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        BasePlayer basePlayer = addAllPlayer(player);
        waitingPlayers.add(basePlayer);
    }

    public HashMap<UUID, BasePlayer> getAllPlayers() {
        return allPlayers;
    }

    public BasePlayer removeAllPlayer(Player player) {
        if (allPlayers.containsKey(player.getUniqueId())) {
            BasePlayer basePlayer = allPlayers.get(player.getUniqueId());
            allPlayers.remove(player.getUniqueId());
            return basePlayer;
        }
        return null;
    }

    public BasePlayer addAllPlayer(Player player) {
        if (allPlayers.containsKey(player.getUniqueId())) return allPlayers.get(player.getUniqueId());
        BasePlayer basePlayer = new BasePlayer(player);
        basePlayer.loadData();
        basePlayer.setOnline(true);
        AbyssalGames.getPlugin().getScoreboardManager().setUpPlayer(basePlayer);
        allPlayers.put(player.getUniqueId(), basePlayer);
        return basePlayer;
    }

    public Spectator addSpectator(BasePlayer player) {
        if (isLobby()) return null;
        Spectator spectator = new Spectator(player, match);
        match.addSpectator(spectator);
        return spectator;
    }

    public MatchPlayer removePlayer(Player player) {
        if (isLobby()) return null;
        MatchPlayer matchPlayer = match.getPlayers().get(player.getName());
        if (matchPlayer != null) {
            match.removePlayer(matchPlayer);
        }
        return matchPlayer;
    }

    public boolean removeWaitingPlayer(Player player) {
        BasePlayer basePlayer = getPlayer(player);
        if (basePlayer != null) {
            return waitingPlayers.remove(basePlayer);
        }
        return false;
    }

    public Spectator removeSpectator(Player player) {
        if (isLobby()) return null;
        Spectator spectator = match.getSpectators().get(player.getName());
        if (spectator != null) {
            match.removeSpectator(spectator);
        }
        return spectator;
    }

    public void toLobby() {
        if (isLobby()) return;

        for (BasePlayer player : getAllPlayers().values()) {
            player.getPlayer().getInventory().clear();
        }

        for (MatchPlayer matchPlayer : match.getPlayers().values()) {
            for (Spectator spectator : match.getSpectators().values()) {
                matchPlayer.getBasePlayer().getPlayer().showPlayer(spectator.getBasePlayer().getPlayer());
            }
        }

        for (MatchPlayer player : match.getPlayers().values()) {
            getLobby().teleport(player.getBasePlayer().getPlayer());
            addWaitingPlayer(player.getBasePlayer().getPlayer());
        }
        match.getPlayers().clear();

        for (Spectator spectator : match.getSpectators().values()) {
            getLobby().teleport(spectator.getBasePlayer().getPlayer());
            addWaitingPlayer(spectator.getBasePlayer().getPlayer());
        }
        match.getSpectators().clear();
    }

    public BasePlayer getPlayer(Player player) {
        return allPlayers.get(player.getUniqueId());
    }

    public void toMatch() {
        if (match != null) {
            for (MatchPlayer matchPlayer : match.getPlayers().values()) {
                matchPlayer.teleportToSpawnPoint();
            }
            for (Spectator spectator : match.getSpectators().values()) {
                spectator.teleportToSpawnPoint();
            }
        }
    }

    public Match getMatch() {
        return match;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public boolean isRunning() {
        return match != null && (match.getState() == MatchState.RUNNING || isDeathmatch());
    }

    public boolean isInGame() {
        return (isRunning() || isStartingDeathmatch() || isEnding());
    }

    public boolean isStarting() {
        return match != null && match.getState() == MatchState.STARTING;
    }

    public boolean isEnding() {
        return match != null && match.getState() == MatchState.ENDING;
    }

    public boolean isStartingDeathmatch() {
        return match != null && match.getState() == MatchState.STARTING_DEATHMATCH;
    }

    public boolean isDeathmatch() {
        return match != null && match.getState() == MatchState.DEATHMATCH;
    }

    public boolean isLobby() {
        return match == null;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public boolean isPlaying(Player player) {
        return match != null && match.getPlayers().containsKey(player.getName());
    }

    public boolean isSpectating(Player player) {
        return match != null && match.getSpectators().containsKey(player.getName());
    }

    public boolean isGracePeriod() {
        return gracePeriod != null;
    }
}
