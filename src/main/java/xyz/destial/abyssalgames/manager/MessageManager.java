package xyz.destial.abyssalgames.manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.destial.abyssalgames.AbyssalGames;

import java.io.File;

public class MessageManager {
    private static FileConfiguration messages;
    private static File dataFolder;

    public static String DEATH_MESSAGE;
    public static String UNKNOWN_DEATH_MESSAGE;
    public static String KILLER_MESSAGE;
    public static String VICTIM_MESSAGE;
    public static String JOIN_MESSAGE;
    public static String QUIT_MESSAGE;
    public static String GAME_BEGIN_MESSAGE;
    public static String COUNTDOWN_MESSAGE;
    public static String GAME_STARTING_MESSAGE;
    public static String GRACE_PERIOD;
    public static String GRACE_PERIOD_END;
    public static String GAME_ENDING_MESSAGE;
    public static String GAME_ENDED_MESSAGE;
    public static String WIN_MESSAGE;
    public static String DEATHMATCH_STARTING_MESSAGE;
    public static String DEATHMATCH_START_MESSAGE;
    public static String VOTE_MESSAGE;
    public static String KICK_GAME_RUNNING;
    public static String KICK_GAME_ENDING;
    public static String TELEPORTING;
    public static String FORCE_START;
    public static String SET_LOBBY;
    public static String TELEPORT_TO_MAP;
    public static String RELOAD;
    public static String REFILL_CHESTS;
    public static String MAP_CREATE;
    public static String WORLD_CREATE;
    public static String MAP_UNLOAD;
    public static String WORLD_UNLOAD;
    public static String ADD_SPAWNPOINT;
    public static String REMOVE_SPAWNPOINT;
    public static String MAP_VOTE_WON;
    public static String MAP_LIST_TITLE;
    public static String MAP_LIST_FORMAT;
    public static String MAP_VOTE_ALREADY_END;
    public static String MAP_VOTE;
    public static String DEFAULT_CHAT_FORMAT;
    public static String SPECTATOR_CHAT_FORMAT;
    public static String NOT_A_NUMBER;
    public static String UNKNOWN_MAP;
    public static String UNKNOWN_MATCH;
    public static String UNKNOWN_WORLD;
    public static String MAP_ALREADY_LOADED;
    public static String MATCH_ALREADY_LOADED;
    public static String WORLD_ALREADY_LOADED;
    public static String MAP_NO_SPAWNPOINTS;
    public static String MAP_TOO_MANY_SPAWNPOINTS;
    public static String UNKNOWN_ERROR;
    public static String CONSOLE_COMMAND;
    public static String COMMAND_USAGE;
    public static String CANNOT_DO_THAT;
    public static String KICK_RELOADING;
    public static String NO_PERMISSION;
    public static String SECOND;
    public static String SECONDS;
    public static String MINUTE;
    public static String MINUTES;
    public static String HOUR;
    public static String HOURS;

    public MessageManager(FileConfiguration messages, File dataFolder) {
        MessageManager.messages = messages;
        MessageManager.dataFolder = dataFolder;

        DEATH_MESSAGE = parse("death.with-killer");
        UNKNOWN_DEATH_MESSAGE = parse("death.unknown");
        KILLER_MESSAGE = parse("death.to-killer");
        VICTIM_MESSAGE = parse("death.to-victim");

        JOIN_MESSAGE = parse("game.join");
        QUIT_MESSAGE = parse("game.quit");
        GAME_BEGIN_MESSAGE = parse("game.begin");
        GAME_STARTING_MESSAGE = parse("game.starting");
        GRACE_PERIOD = parse("game.grace-period");
        GRACE_PERIOD_END = parse("game.grace-period-end");
        GAME_ENDING_MESSAGE = parse("game.ending");
        GAME_ENDED_MESSAGE = parse("game.end");
        WIN_MESSAGE = parse("game.win");
        DEATHMATCH_STARTING_MESSAGE = parse("game.deathmatch-starting");
        DEATHMATCH_START_MESSAGE = parse("game.deathmatch-start");
        KICK_GAME_RUNNING = parse("game.kick-running");
        KICK_GAME_ENDING = parse("game.kick-ending");

        MAP_VOTE_WON = parse("maps.vote-won");
        MAP_VOTE = parse("maps.vote");
        MAP_LIST_TITLE = parse("maps.list-title");
        MAP_LIST_FORMAT = parse("maps.list-format");
        MAP_VOTE_ALREADY_END = parse("maps.vote-already-ended");

        VOTE_MESSAGE = parse("others.vote");
        TELEPORTING = parse("others.teleport");
        COUNTDOWN_MESSAGE = parse("others.countdown");
        SECOND = parse("others.second", "second");
        SECONDS = parse("others.seconds", "seconds");
        MINUTE = parse("others.minute", "minute");
        MINUTES = parse("others.minutes", "minutes");
        HOUR = parse("others.hour", "hour");
        HOURS = parse("others.hours", "hours");
        REFILL_CHESTS = parse("others.refill");
        DEFAULT_CHAT_FORMAT = parse("others.chat-format");
        SPECTATOR_CHAT_FORMAT = parse("others.spectator-chat-format");

        FORCE_START = parse("admin.force-start");
        SET_LOBBY = parse("admin.set-lobby");
        TELEPORT_TO_MAP = parse("admin.teleport-to-map-message");
        RELOAD = parse("admin.reload");
        MAP_CREATE = parse("admin.map-create");
        WORLD_CREATE = parse("admin.world-create");
        MAP_UNLOAD = parse("admin.map-unload");
        WORLD_UNLOAD = parse("admin.world-unload");
        ADD_SPAWNPOINT = parse("admin.add-spawnpoint");
        REMOVE_SPAWNPOINT = parse("admin.remove-spawnpoint");
        KICK_RELOADING = parse("admin.kick-reload");

        NO_PERMISSION = parse("error.no-permission");
        NOT_A_NUMBER = parse("error.not-a-number");
        UNKNOWN_MAP = parse("error.unknown-map");
        UNKNOWN_MATCH = parse("error.unknown-match");
        UNKNOWN_WORLD = parse("error.unknown-world");
        MAP_ALREADY_LOADED = parse("error.map-loaded");
        MATCH_ALREADY_LOADED = parse("error.match-loaded");
        WORLD_ALREADY_LOADED = parse("error.world-loaded");
        MAP_NO_SPAWNPOINTS = parse("error.map-no-spawnpoints");
        MAP_TOO_MANY_SPAWNPOINTS = parse("error.map-too-many-spawnpoints");
        UNKNOWN_ERROR = parse("error.unknown");
        CONSOLE_COMMAND = parse("error.console-command");
        COMMAND_USAGE = parse("error.command-usage");
        CANNOT_DO_THAT = parse("error.cannot-do-that");
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static File getDataFolder() {
        return dataFolder;
    }

    public static void save() {
        AbyssalGames.getPlugin().saveResource("messages.yml", false);
    }

    public static String translate(String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public static String parse(String path) {
        return translate(messages.getString(path, ""));
    }

    public static String parse(String path, String def) {
        return translate(messages.getString(path, def));
    }
}
