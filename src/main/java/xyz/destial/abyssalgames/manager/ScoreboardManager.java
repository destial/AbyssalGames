package xyz.destial.abyssalgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.scoreboard.ScoreboardUpdate;
import xyz.destial.abyssalgames.match.BasePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreboardManager {
    private final FileConfiguration config;
    private final List<String> lobbyScoreboardRaw;
    private final List<String> gameScoreboardRaw;
    private final String title;
    private final org.bukkit.scoreboard.ScoreboardManager bukkitScoreboardManager;
    private final HashMap<UUID, Integer> taskIDs;

    public ScoreboardManager(FileConfiguration config) {
        this.config = config;
        lobbyScoreboardRaw = getLines(config.getStringList("lobby"));
        gameScoreboardRaw = getLines(config.getStringList("game"));
        title = config.getString("title", "Abyssal Games");
        bukkitScoreboardManager = Bukkit.getScoreboardManager();
        taskIDs = new HashMap<>();
    }

    public void setUpPlayer(BasePlayer player) {
        Scoreboard scoreboard = bukkitScoreboardManager.getNewScoreboard();
        player.setScoreboard(scoreboard);

        for (int i = 0; i < 15; i++) {
            Team team = scoreboard.registerNewTeam(SCOREBOARD_LINES[i]);
            team.addEntry(SCOREBOARD_LINES[i]);
        }

        taskIDs.put(player.getPlayer().getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(AbyssalGames.getPlugin(), new ScoreboardUpdate(player),1L));
    }

    public void cancelScoreboardUpdate(UUID uuid) {
        Integer taskID = taskIDs.get(uuid);
        if (taskID != null) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    public List<String> getCurrentScoreboard() {
        if (AbyssalGames.getPlugin().getMatchManager().isLobby() || AbyssalGames.getPlugin().getMatchManager().isStarting()) {
            return lobbyScoreboardRaw;
        }
        return gameScoreboardRaw;
    }

    public List<String> getPreviousScoreboard() {
        return (getCurrentScoreboard() == lobbyScoreboardRaw ? gameScoreboardRaw : lobbyScoreboardRaw);
    }

    public String getTitle() {
        return title;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public org.bukkit.scoreboard.ScoreboardManager getBukkitScoreboardManager() {
        return bukkitScoreboardManager;
    }

    private List<String> getLines(List<String> raw) {
        List<String> newList = new ArrayList<>();
        for (int i = raw.size() - 1; i >= 0; i--){
            if (newList.size() == 15){
                AbyssalGames.getPlugin().getLogger().warning("Scoreboard lines can't have more than 15 lines!");
                break;
            }
            newList.add(MessageManager.translate(raw.get(i)));
        }
        return newList;
    }

    public HashMap<UUID, Integer> getTaskIDs() {
        return taskIDs;
    }

    public static final String[] SCOREBOARD_LINES = new String[] {
            ChatColor.UNDERLINE + "" + ChatColor.RESET,
            ChatColor.ITALIC + "" + ChatColor.RESET,
            ChatColor.BOLD + "" + ChatColor.RESET,
            ChatColor.RESET + "" + ChatColor.RESET,
            ChatColor.GREEN + "" + ChatColor.RESET,
            ChatColor.DARK_GRAY + "" + ChatColor.RESET,
            ChatColor.GOLD + "" + ChatColor.RESET,
            ChatColor.RED + "" + ChatColor.RESET,
            ChatColor.YELLOW + "" + ChatColor.RESET,
            ChatColor.WHITE + "" + ChatColor.RESET,
            ChatColor.DARK_GREEN + "" + ChatColor.RESET,
            ChatColor.BLUE + "" + ChatColor.RESET,
            ChatColor.STRIKETHROUGH + "" + ChatColor.RESET,
            ChatColor.MAGIC + "" + ChatColor.RESET,
            ChatColor.DARK_RED + "" + ChatColor.RESET
    };
}
