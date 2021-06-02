package xyz.destial.abyssalgames.match;

import org.bukkit.GameMode;
import org.bukkit.Location;

public class MatchPlayer {
    private final BasePlayer basePlayer;
    private final Match match;
    private final Location spawnPoint;

    public MatchPlayer(BasePlayer player, Match match, Location spawnPoint) {
        this.basePlayer = player;
        this.match = match;
        this.spawnPoint = spawnPoint;
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        player.getPlayer().getInventory().clear();
    }

    public Match getMatch() {
        return match;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public BasePlayer getBasePlayer() {
        return basePlayer;
    }

    public void teleportToSpawnPoint() {
        getBasePlayer().getPlayer().teleport(spawnPoint);
    }
}
