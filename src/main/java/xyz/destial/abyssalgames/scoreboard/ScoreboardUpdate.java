package xyz.destial.abyssalgames.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.manager.ScoreboardManager;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.state.MatchState;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.utils.ParseMessage;

public class ScoreboardUpdate implements Runnable {
    private static final long UPDATE_DELAY = 20L;
    private static final String COLOR_CHAR = String.valueOf(ChatColor.COLOR_CHAR);

    private final BasePlayer player;
    private final MatchManager matchManager;
    private final xyz.destial.abyssalgames.manager.ScoreboardManager scoreboardManager;
    private MatchState matchState;
    private Objective objective;
    private Scoreboard scoreboard;
    public ScoreboardUpdate(BasePlayer player) {
        this.player = player;
        matchManager = AbyssalGames.getPlugin().getMatchManager();
        scoreboardManager = AbyssalGames.getPlugin().getScoreboardManager();
        scoreboard = player.getScoreboard();
        objective = scoreboard.registerNewObjective("match", "dummy");
        matchState = getMatchState();

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(MessageManager.translate(ParseMessage.count(AbyssalGames.getPlugin().getScoreboardManager().getTitle())));

        for (int i = 0; i < scoreboardManager.getCurrentScoreboard().size(); i++){
            Score score = objective.getScore(xyz.destial.abyssalgames.manager.ScoreboardManager.SCOREBOARD_LINES[i]);
            score.setScore(i);
        }
    }

    @Override
    public void run() {
        if (matchState != null && !matchState.equals(getMatchState())) {
            matchState = getMatchState();
            resetObjective();
        }

        int i = 0;
        for (String line : scoreboardManager.getCurrentScoreboard()) {
            String first = "";
            String second = "";
            if (!line.isEmpty()) {
                String useLine = line;
                if (matchManager.getMatch() != null) {
                    useLine = ParseMessage.map(useLine, matchManager.getMatch().getMap());
                } else {
                    useLine = ParseMessage.votemap(useLine, matchManager.getVoteManager().getMaps());
                }
                String parsedLine = ParseMessage.rank(useLine, player);

                if (parsedLine.length() <= 16) {
                    first = parsedLine;
                } else {
                    int split = 16;
                    first = parsedLine.substring(0, split);
                    boolean copyColor = false;
                    if (first.endsWith(COLOR_CHAR)) {
                        copyColor = true;
                        split = 15;
                        first = parsedLine.substring(0, split);
                        if (first.substring(0, 14).endsWith(COLOR_CHAR)) {
                            split = 13;
                            first = parsedLine.substring(0, split);
                        }
                    }

                    if (copyColor) {
                        second = ChatColor.getLastColors(first);
                    }

                    second += parsedLine.substring(split);

                    if (second.length() > 16) {
                        second = "";
                    }
                }
            }

            Team lineTeam = scoreboard.getTeam(xyz.destial.abyssalgames.manager.ScoreboardManager.SCOREBOARD_LINES[i]);
            if (!lineTeam.getPrefix().equals(first)) {
                lineTeam.setPrefix(first);
            }
            if (!lineTeam.getSuffix().equals(second)) {
                lineTeam.setSuffix(second);
            }
            i++;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(AbyssalGames.getPlugin(),this, UPDATE_DELAY);
    }

    private MatchState getMatchState() {
        if (matchManager.getMatch() == null) return null;
        switch (matchManager.getMatch().getState()) {
            case STARTING_DEATHMATCH:
                return MatchState.STARTING_DEATHMATCH;
            case ENDING:
                return MatchState.ENDING;
            case RUNNING:
                return MatchState.RUNNING;
            case STARTING:
                return MatchState.STARTING;
            case PREPARING:
                return MatchState.PREPARING;
            case DEATHMATCH:
                return MatchState.DEATHMATCH;
            case PREPARING_DEATHMATCH:
                return MatchState.PREPARING_DEATHMATCH;
            default:
                return null;
        }
    }

    public BasePlayer getPlayer() {
        return player;
    }

    private void resetObjective(){
        objective.unregister();
        objective = scoreboard.registerNewObjective("match","dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(MessageManager.translate(ParseMessage.count(AbyssalGames.getPlugin().getScoreboardManager().getTitle())));

        for (int i = 0; i < scoreboardManager.getPreviousScoreboard().size(); i++){
            Score score = objective.getScore(ScoreboardManager.SCOREBOARD_LINES[i]);
            score.setScore(i);
        }
    }
}
