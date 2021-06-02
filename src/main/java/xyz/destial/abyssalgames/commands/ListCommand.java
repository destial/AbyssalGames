package xyz.destial.abyssalgames.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.manager.MessageManager;

public class ListCommand implements CommandExecutor {
    public ListCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            StringBuilder message = new StringBuilder();
            message.append(MessageManager.translate("&aOnline Players: &7"));
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            int index = 0;
            if (matchManager.isRunning()) {
                for (MatchPlayer matchPlayer : matchManager.getMatch().getPlayers().values()) {
                    index++;
                    message.append(matchPlayer.getBasePlayer().getPlayer().getName());
                    if (index != matchManager.getMatch().getPlayers().size()) {
                        message.append(", ");
                    }
                }
            } else {
                for (BasePlayer waitingPlayer : matchManager.getWaitingPlayers()) {
                    index++;
                    message.append(waitingPlayer.getPlayer().getName());
                    if (index != matchManager.getWaitingPlayers().size()) {
                        message.append(", ");
                    }
                }
            }
            player.sendMessage(message.toString());
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(MessageManager.CONSOLE_COMMAND);
        return true;
    }
}
