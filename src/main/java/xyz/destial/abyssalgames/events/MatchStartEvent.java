package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;

public class MatchStartEvent extends MatchUpdateEvent {
    public MatchStartEvent(Match match) {
        super(match);
    }
}
