package xyz.destial.abyssalgames.match;

import org.bukkit.GameMode;

public class Spectator {
    private final BasePlayer basePlayer;
    private final Match match;

    public Spectator(BasePlayer player, Match match) {
        this.basePlayer = player;
        this.match = match;
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    public Match getMatch() {
        return match;
    }

    public BasePlayer getBasePlayer() {
        return basePlayer;
    }

    public void teleportToSpawnPoint() {
        basePlayer.getPlayer().teleport(match.getMap().getSpawnPoints().get(0));
    }
}
