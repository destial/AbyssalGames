package xyz.destial.abyssalgames.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.map.Map;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.utils.ParseMessage;

public class MapsCommand implements CommandExecutor {
    public MapsCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            if (matchManager.isLobby()) {
                player.sendMessage(MessageManager.MAP_LIST_TITLE);
                for (Map votingMaps : matchManager.getVoteManager().getMaps()) {
                    player.sendMessage(ParseMessage.map(MessageManager.MAP_LIST_FORMAT, votingMaps));
                }
                return true;
            }
            player.sendMessage(MessageManager.MAP_VOTE_ALREADY_END);
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(MessageManager.CONSOLE_COMMAND);
        return true;
    }
}
