package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.utils.Countdown;

public class CountdownCompleteEvent extends MatchUpdateEvent {
    private final Countdown countdown;

    public CountdownCompleteEvent(Match match, Countdown countdown) {
        super(match);
        this.countdown = countdown;
    }

    public Countdown getCountdown() {
        return countdown;
    }
}
