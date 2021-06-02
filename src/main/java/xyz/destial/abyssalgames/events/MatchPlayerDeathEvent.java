package xyz.destial.abyssalgames.events;

import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.match.MatchPlayer;

public class MatchPlayerDeathEvent extends MatchUpdateEvent {
    private final MatchPlayer victim;
    private final MatchPlayer killer;
    private final PlayerDeathEvent vanillaEvent;

    public MatchPlayerDeathEvent(Match match, PlayerDeathEvent event, MatchPlayer victim, MatchPlayer killer) {
        super(match);
        this.vanillaEvent = event;
        this.victim = victim;
        this.killer = killer;
    }

    public MatchPlayer getVictim() {
        return victim;
    }

    public MatchPlayer getKiller() {
        return killer;
    }

    public PlayerDeathEvent getVanillaEvent() {
        return vanillaEvent;
    }
}
