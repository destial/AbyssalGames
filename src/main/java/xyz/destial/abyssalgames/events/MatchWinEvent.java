package xyz.destial.abyssalgames.events;

import xyz.destial.abyssalgames.match.Match;
import xyz.destial.abyssalgames.match.MatchPlayer;

public class MatchWinEvent extends MatchUpdateEvent {
    private final MatchPlayer winner;

    public MatchWinEvent(Match match, MatchPlayer winner) {
        super(match);
        this.winner = winner;
    }

    public MatchPlayer getWinner() {
        return winner;
    }
}
