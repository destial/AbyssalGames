package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;

public class MatchPrepareEvent extends MatchUpdateEvent {
    public MatchPrepareEvent(Match match) {
        super(match);
    }
}
