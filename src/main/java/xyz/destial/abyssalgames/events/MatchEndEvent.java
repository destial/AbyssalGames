package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;

public class MatchEndEvent extends MatchUpdateEvent {
    public MatchEndEvent(Match match) {
        super(match);
    }
}
