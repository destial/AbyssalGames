package xyz.destial.abyssalgames.utils;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.map.Map;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;

public class ParseMessage {
    public static String count(String message) {
        MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
        String parsedMessage = message.replace("{currentCount}", String.valueOf(!matchManager.isRunning() ? matchManager.getWaitingPlayers().size() : matchManager.getMatch().getPlayers().size()));
        parsedMessage = parsedMessage.replace("{totalCount}", "24");
        parsedMessage = parsedMessage.replace("{neededPlayers}", String.valueOf((24 - (!matchManager.isRunning() ? matchManager.getWaitingPlayers().size() : matchManager.getMatch().getPlayers().size()))));
        return parsedMessage;
    }

    public static String map(String message, Map map) {
        String parsedMessage = message.replace("{map}", map.getName());
        parsedMessage = parsedMessage.replace("{votes}", String.valueOf(map.getVotes()));
        return count(parsedMessage);
    }

    public static String death(String message, Player killer, Player victim) {
        String parsedMessage = message.replace("{killer}", killer.getName());
        parsedMessage = parsedMessage.replace("{victim}", victim.getName());
        return count(parsedMessage);
    }

    public static String unknownDeath(String message, Player victim) {
        String parsedMessage = message.replace("{victim}", victim.getName());
        return count(parsedMessage);
    }

    public static String player(String message, Player player) {
        String parsedMessage = message.replace("{player}", player.getName());
        return count(parsedMessage);
    }

    public static String rank(String message, BasePlayer basePlayer) {
        String parsedMessage = message.replace("{rank}", String.valueOf(basePlayer.getEXP()));
        parsedMessage = parsedMessage.replace("{kills}", String.valueOf(basePlayer.getKills()));
        parsedMessage = parsedMessage.replace("{deaths}", String.valueOf(basePlayer.getDeaths()));
        parsedMessage = parsedMessage.replace("{games}", String.valueOf(basePlayer.getGames()));
        parsedMessage = parsedMessage.replace("{games_won}", String.valueOf(basePlayer.getGamesWon()));
        return player(parsedMessage, basePlayer.getPlayer());
    }

    public static String spawnpoint(String message, int spawnPoint) {
        return message.replace("{spawnpoint}", String.valueOf(spawnPoint));
    }

    public static String chat(String message, String chat) {
        return message.replace("{message}", chat);
    }

    public static String countdown(String message, int time) {
        String parsedMessage = message.replace("{countdown}", MessageManager.COUNTDOWN_MESSAGE);
        int convertedTime = time;
        String unit = MessageManager.SECONDS;
        if (convertedTime > 3600) {
            unit = MessageManager.HOURS;
            convertedTime = convertedTime/3600;
        } else if (convertedTime == 3600) {
            unit = MessageManager.HOUR;
            convertedTime = 1;
        } else if (convertedTime > 60) {
            unit = MessageManager.MINUTES;
            convertedTime = convertedTime/60;
        } else if (convertedTime == 60) {
            unit = MessageManager.MINUTE;
            convertedTime = 1;
        } else if (convertedTime == 1) {
            unit = MessageManager.SECOND;
        }
        parsedMessage = parsedMessage.replace("{time}", String.valueOf(convertedTime));
        parsedMessage = parsedMessage.replace("{unit}", unit);
        return count(parsedMessage);
    }

    public static String votemap(String message, ArrayList<Map> maps) {
        for (int i = 0; i < maps.size(); i++) {
            String parsedMessage = message.replace("{voting_map_" + (i+1) + "}", maps.get(i).getName());
            parsedMessage = parsedMessage.replace("{map_number_" + (i+1) + "}", String.valueOf(i+1));
            parsedMessage = parsedMessage.replace("{votes_map_" + (i+1) + "}", String.valueOf(maps.get(i).getVotes()));
            if (parsedMessage != message) {
                return parsedMessage;
            }
        }
        return message;
    }
}
