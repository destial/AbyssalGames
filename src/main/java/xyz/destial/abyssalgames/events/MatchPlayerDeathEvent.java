package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.match.MatchPlayer;

public class MatchPlayerDeathEvent extends MatchUpdateEvent {
    private final MatchPlayer victim;
    private final MatchPlayer killer;

    public MatchPlayerDeathEvent(Match match, MatchPlayer victim, MatchPlayer killer) {
        super(match);
        this.victim = victim;
        this.killer = killer;
    }

    public MatchPlayer getVictim() {
        return victim;
    }

    public MatchPlayer getKiller() {
        return killer;
    }

}
