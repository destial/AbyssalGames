package xyz.destial.abyssalgames.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.destial.abyssalgames.match.Match;

public class MatchUpdateEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Match match;
    private static final HandlerList handlers = new HandlerList();

    public MatchUpdateEvent(Match match) {
        super();
        this.match = match;
        setCancelled(false);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public Match getMatch() {
        return match;
    }
}
