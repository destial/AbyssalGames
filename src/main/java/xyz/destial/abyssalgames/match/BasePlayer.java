package xyz.destial.abyssalgames.match;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.destial.abyssalgames.AbyssalGames;

public class BasePlayer {

    private long kills = 0;
    private long deaths = 0;
    private long games = 0;
    private long gamesWon = 0;
    private long exp = 0;
    private final Player player;
    private Scoreboard scoreboard = null;
    private boolean online = true;

    public BasePlayer(Player player) {
        this.player = player;
    }

    public int getKills() {
        return (int) kills;
    }

    public void addKill() {
        kills++;
    }

    public int getDeaths() {
        return (int) deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public int getGames() {
        return (int) games;
    }

    public void addGame() {
        games++;
    }

    public int getGamesWon() {
        return (int) gamesWon;
    }

    public void addGameWon() {
        gamesWon++;
    }

    public int getEXP() {
        return (int) exp;
    }

    public void addEXP(int xp) {
        exp += xp;
    }

    public Player getPlayer() {
        return player;
    }

    public void loadData() {
        String result = AbyssalGames.getPlugin().getDataStore().getData(player);
        if (result.isEmpty()) return;

        try {
            Object jsonObject = new JSONParser().parse(result);
            kills = (long) ((JSONObject) jsonObject).get("kills");
            deaths = (long) ((JSONObject) jsonObject).get("deaths");
            games = (long) ((JSONObject) jsonObject).get("games");
            gamesWon = (long) ((JSONObject) jsonObject).get("games_won");
            exp = (long) ((JSONObject) jsonObject).get("exp");
        } catch (ParseException e) {
            AbyssalGames.getPlugin().getLogger().severe("Unable to parse player data at position: " + e.getPosition());
        }
    }

    public void saveData() {
        AbyssalGames.getPlugin().getDataStore().updateData(this);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        player.setScoreboard(scoreboard);
        this.scoreboard = scoreboard;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "{ "
                + "\"kills\": " + getKills() + ", "
                + "\"deaths\": " + getDeaths() + ", "
                + "\"games\": " + getGames() + ", "
                + "\"games_won\": " + getGamesWon() + ", "
                + "\"exp\": " + getEXP()
                + " }";
    }
}
