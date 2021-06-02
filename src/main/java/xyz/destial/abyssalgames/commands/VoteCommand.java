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

public class VoteCommand implements CommandExecutor {
    public VoteCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            if (matchManager.isLobby()) {
                if (args.length == 1) {
                    try {
                        int mapIndex = Integer.parseInt(args[0]);
                        Map map = matchManager.getVoteManager().vote(mapIndex);
                        if (map != null) {
                            player.sendMessage(ParseMessage.map(MessageManager.MAP_VOTE, map));
                        } else {
                            player.sendMessage(MessageManager.UNKNOWN_MAP);
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(MessageManager.NOT_A_NUMBER);
                    }
                } else {
                    player.sendMessage(MessageManager.COMMAND_USAGE + "/vote [number]");
                }
                return true;
            }
            player.sendMessage(MessageManager.KICK_GAME_RUNNING);
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(MessageManager.CONSOLE_COMMAND);
        return true;
    }
}
