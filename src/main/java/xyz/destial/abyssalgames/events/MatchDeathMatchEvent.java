package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;

public class MatchDeathMatchEvent extends MatchUpdateEvent {
    public MatchDeathMatchEvent(Match match) {
        super(match);
    }
}
