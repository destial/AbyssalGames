package xyz.destial.abyssalgames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.Spectator;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.utils.ParseMessage;

public class ChatListener implements Listener {
    public ChatListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        BasePlayer player = matchManager.getAllPlayers().get(e.getPlayer().getUniqueId());
        String format = ParseMessage.player(ParseMessage.chat(MessageManager.DEFAULT_CHAT_FORMAT, e.getMessage()), e.getPlayer());
        if (player != null) {
            format = ParseMessage.rank(ParseMessage.chat(MessageManager.DEFAULT_CHAT_FORMAT, e.getMessage()), player);
        }

        if (matchManager.isSpectating(e.getPlayer())) {
            format = ParseMessage.player(ParseMessage.chat(MessageManager.SPECTATOR_CHAT_FORMAT, e.getMessage()), e.getPlayer());
            if (player != null) {
                format = ParseMessage.rank(ParseMessage.chat(MessageManager.SPECTATOR_CHAT_FORMAT, e.getMessage()), player);
            }
            
            for (Spectator spectator : matchManager.getMatch().getSpectators().values()) {
                spectator.getBasePlayer().getPlayer().sendMessage(format);
            }
            return;
        }
        for (BasePlayer basePlayer : matchManager.getAllPlayers().values()) {
            basePlayer.getPlayer().sendMessage(format);
        }
    }
}
