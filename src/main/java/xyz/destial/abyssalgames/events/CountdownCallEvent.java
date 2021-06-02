package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.utils.Countdown;

public class CountdownCallEvent extends MatchUpdateEvent {
    private final Countdown countdown;
    private final String originalMessage;
    private String message;

    public CountdownCallEvent(Match match, Countdown countdown, String message) {
        super(match);
        this.countdown = countdown;
        this.message = originalMessage = message;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        super.setCancelled(cancelled);
        if (cancelled) {
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            matchManager.call(new CountdownStopEvent(getMatch(), getCountdown()));
            countdown.stopTimer();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public Countdown getCountdown() {
        return countdown;
    }
}
