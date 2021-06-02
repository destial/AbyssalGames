package xyz.destial.abyssalgames.match;

import org.bukkit.GameMode;
import org.bukkit.Material;
import xyz.destial.abyssalgames.map.Map;
import xyz.destial.abyssalgames.match.state.MatchState;

import java.util.ArrayList;
import java.util.HashMap;

public class Match {
    private final Map map;
    private final java.util.HashMap<String, MatchPlayer> players;
    private final java.util.HashMap<String, Spectator> spectators;
    private final ArrayList<Material> breakableBlocks;
    private final ArrayList<Material> placeableBlocks;
    private MatchState state;
    private int deaths;

    public Match(Map map) {
        this.map = map;
        state = MatchState.STARTING;
        players = new HashMap<>();
        spectators = new HashMap<>();
        breakableBlocks = new ArrayList<>();
        placeableBlocks = new ArrayList<>();
    }

    public void addPlayer(MatchPlayer matchPlayer) {
        players.put(matchPlayer.getBasePlayer().getPlayer().getName(), matchPlayer);
        matchPlayer.getBasePlayer().setOnline(true);
        matchPlayer.getBasePlayer().getPlayer().setGameMode(GameMode.SURVIVAL);
        matchPlayer.getBasePlayer().getPlayer().teleport(matchPlayer.getSpawnPoint());
    }

    public void addSpectator(Spectator spectator) {
        spectators.put(spectator.getBasePlayer().getPlayer().getName(), spectator);
        spectator.getBasePlayer().setOnline(true);
        for (MatchPlayer player : players.values()) {
            player.getBasePlayer().getPlayer().hidePlayer(spectator.getBasePlayer().getPlayer());
        }
    }

    public void removePlayer(MatchPlayer player) {
        player.getBasePlayer().getPlayer().setGameMode(GameMode.ADVENTURE);
        player.getBasePlayer().setOnline(false);
        players.remove(player.getBasePlayer().getPlayer().getName());
    }

    public void removeSpectator(Spectator spectator) {
        spectators.remove(spectator.getBasePlayer().getPlayer().getName());
        spectator.getBasePlayer().setOnline(false);
        spectator.getBasePlayer().getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    public HashMap<String, MatchPlayer> getPlayers() {
        return players;
    }

    public HashMap<String, Spectator> getSpectators() {
        return spectators;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Material> getBreakableBlocks() {
        return breakableBlocks;
    }

    public ArrayList<Material> getPlaceableBlocks() {
        return placeableBlocks;
    }

    public void addBreakableBlock(Material block) {
        if (!breakableBlocks.contains(block)) {
            breakableBlocks.add(block);
        }
    }

    public void addPlaceableBlock(Material block) {
        if (!placeableBlocks.contains(block)) {
            placeableBlocks.add(block);
        }
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public void addDeath() {
        deaths++;
    }
}
