package xyz.destial.abyssalgames.commands;

import xyz.destial.abyssalgames.AbyssalGames;

public class CommandHandler {

    public CommandHandler() {
        AbyssalGames.getPlugin().getCommand("ab").setExecutor(new AdminCommands());
        AbyssalGames.getPlugin().getCommand("vote").setExecutor(new VoteCommand());
        AbyssalGames.getPlugin().getCommand("list").setExecutor(new ListCommand());
        AbyssalGames.getPlugin().getCommand("maps").setExecutor(new MapsCommand());
    }
}
