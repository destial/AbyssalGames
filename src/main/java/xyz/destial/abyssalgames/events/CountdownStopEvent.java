package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.utils.Countdown;

public class CountdownStopEvent extends MatchUpdateEvent {
    private final Countdown countdown;

    public CountdownStopEvent(Match match, Countdown countdown) {
        super(match);
        this.countdown = countdown;
    }

    public Countdown getCountdown() {
        return countdown;
    }
}
