package xyz.destial.abyssalgames.utils;

import org.bukkit.Bukkit;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.events.CountdownCallEvent;
import xyz.destial.abyssalgames.events.CountdownCompleteEvent;
import xyz.destial.abyssalgames.events.CountdownStopEvent;
import xyz.destial.abyssalgames.match.Match;

public class Countdown {
    private final Match match;
    private final String message;
    private final Countdown self;
    private int time;
    private int id;

    public Countdown(Match match, int time, String message) {
        this.match = match;
        this.time = time;
        this.message = message;
        self = this;
    }

    public void startTimer() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbyssalGames.getPlugin(), () -> {
            if (time >= 0) {
                if (time == 0) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCompleteEvent(match, self));
                    Bukkit.getScheduler().cancelTask(id);
                } else if (time <= 30 && time % 5 == 0) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCallEvent(match, self, ParseMessage.countdown(message, time)));
                } else if (time < 10) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCallEvent(match, self, ParseMessage.countdown(message, time)));
                } else if (time >= 600 && time % 600 == 0) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCallEvent(match, self, ParseMessage.countdown(message, time)));
                } else if (time < 600 && time % 300 == 0) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCallEvent(match, self, ParseMessage.countdown(message, time)));
                } else if (time == 60) {
                    AbyssalGames.getPlugin().getMatchManager().call(new CountdownCallEvent(match, self, ParseMessage.countdown(message, time)));
                }
                --time;
            } else {
                stopTimer();
            }
        }, 0L, 20L);
    }

    public void stopTimer() {
        if (id != -1) {
            Bukkit.getScheduler().cancelTask(getID());
        }
    }

    public void forceStopTimer() {
        if (id != -1) {
            AbyssalGames.getPlugin().getMatchManager().call(new CountdownStopEvent(match, this));
            Bukkit.getScheduler().cancelTask(getID());
        }
    }

    public Match getMatch() {
        return match;
    }

    public int getID() {
        return id;
    }

    public int getTime() {
        return time;
    }
}
