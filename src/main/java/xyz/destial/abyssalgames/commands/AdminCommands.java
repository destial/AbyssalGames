package xyz.destial.abyssalgames.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.manager.MapManager;
import xyz.destial.abyssalgames.match.BasePlayer;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.match.MatchPlayer;
import xyz.destial.abyssalgames.manager.MessageManager;
import xyz.destial.abyssalgames.utils.ParseMessage;

import java.util.Arrays;
import java.util.Map;

public class AdminCommands implements CommandExecutor {
    public AdminCommands() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
            MapManager mapManager = AbyssalGames.getPlugin().getMapManager();
            World world = player.getWorld();
            xyz.destial.abyssalgames.map.Map currentMap = mapManager.getMap(world.getName());
            Map<String, xyz.destial.abyssalgames.map.Map> maps = AbyssalGames.getPlugin().getMapManager().getMaps();
            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "setlobby":
                        matchManager.getLobby().setSpawn(player.getLocation());
                        player.sendMessage(MessageManager.SET_LOBBY);
                        break;
                    case "players":
                        String message = "";
                        if (matchManager.isRunning()) {
                            for (MatchPlayer matchPlayer : matchManager.getMatch().getPlayers().values()) {
                                message += matchPlayer.getBasePlayer().getPlayer().getName() + ", ";
                            }
                        } else {
                            for (BasePlayer waitingPlayer : matchManager.getWaitingPlayers()) {
                                message += waitingPlayer.getPlayer().getName() + ", ";
                            }
                        }
                        player.sendMessage(message);
                        break;
                    case "reload":
                        AbyssalGames.getPlugin().reloadConfig();
                        player.sendMessage(MessageManager.RELOAD);
                        break;
                    case "maps":
                        player.sendMessage(MessageManager.MAP_LIST_TITLE);
                        for (xyz.destial.abyssalgames.map.Map map : maps.values()) {
                            player.sendMessage(ParseMessage.map(MessageManager.MAP_LIST_FORMAT, map));
                        }
                        break;
                    case "tp":
                        if (args.length >= 2) {
                            String mapName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                            mapName = mapName.toLowerCase();
                            xyz.destial.abyssalgames.map.Map map = AbyssalGames.getPlugin().getMapManager().getMaps().get(mapName);
                            if (map != null && map.getWorld() != null) {
                                player.teleport(map.getWorld().getSpawnLocation());
                                player.sendMessage(ParseMessage.map(MessageManager.TELEPORT_TO_MAP, map));
                            } else {
                                player.sendMessage(MessageManager.UNKNOWN_MAP);
                            }
                        } else {
                            player.sendMessage(MessageManager.COMMAND_USAGE + "/ab tp [map]");
                        }
                        break;
                    case "create":
                        if (mapManager.getMap(world.getName()) == null) {
                            xyz.destial.abyssalgames.map.Map map = mapManager.newMap(world);
                            player.sendMessage(ParseMessage.map(MessageManager.MAP_CREATE, map));
                        } else {
                            player.sendMessage(MessageManager.MAP_ALREADY_LOADED);
                        }
                        break;
                    case "load":
                        if (args.length >= 2) {
                            String mapName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                            xyz.destial.abyssalgames.map.Map map = AbyssalGames.getPlugin().getMapManager().getMaps().get(mapName.toLowerCase());
                            if (map == null) {
                                World w = Bukkit.getWorld(mapName);
                                if (w != null) {
                                    player.sendMessage(MessageManager.WORLD_ALREADY_LOADED);
                                } else {
                                    WorldCreator worldCreator = new WorldCreator(mapName);
                                    w = Bukkit.createWorld(worldCreator);
                                    player.teleport(w.getSpawnLocation());
                                    player.sendMessage(MessageManager.WORLD_CREATE.replace("{map}", w.getName()));
                                }
                            } else {
                                player.sendMessage(MessageManager.MAP_ALREADY_LOADED);
                            }
                        } else {
                            player.sendMessage(MessageManager.COMMAND_USAGE + "/ab load [map]");
                        }
                        break;
                    case "unload":
                        if (args.length >= 2) {
                            String mapName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                            if (Bukkit.getWorld(mapName) != null && !mapName.equalsIgnoreCase(AbyssalGames.getServerProperty("level-name"))) {
                                for (Player worldPlayers : Bukkit.getWorld(mapName).getPlayers()) {
                                    worldPlayers.teleport(matchManager.getLobby().getSpawn());
                                }
                                if (Bukkit.unloadWorld(Bukkit.getWorld(mapName), false)) {
                                    player.sendMessage(MessageManager.MAP_UNLOAD.replace("{map}", mapName));
                                } else {
                                    player.sendMessage(MessageManager.UNKNOWN_ERROR);
                                }
                            } else {
                                player.sendMessage(MessageManager.UNKNOWN_ERROR);
                            }
                        } else {
                            player.sendMessage(MessageManager.COMMAND_USAGE + "/ab unload [map]");
                        }
                        break;
                    case "addspawn":
                        if (currentMap != null) {
                            if (currentMap.addSpawnPoint(player.getLocation())) {
                                player.sendMessage(ParseMessage.spawnpoint(MessageManager.ADD_SPAWNPOINT, currentMap.getSpawnPoints().size()));
                            } else {
                                player.sendMessage(MessageManager.MAP_TOO_MANY_SPAWNPOINTS);
                            }
                        } else {
                            player.sendMessage(MessageManager.UNKNOWN_MAP);
                        }
                        break;
                    case "removespawn":
                        if (currentMap != null) {
                            if (currentMap.getSpawnPoints().size() > 1) {
                                if (args.length >= 2) {
                                    try {
                                        currentMap.removeSpawnPoint(Integer.parseInt(args[1]));
                                        player.sendMessage(ParseMessage.spawnpoint(MessageManager.REMOVE_SPAWNPOINT, Integer.parseInt(args[1])));
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(MessageManager.NOT_A_NUMBER);
                                    }
                                } else {
                                    player.sendMessage(MessageManager.COMMAND_USAGE + "/ab removespawn [number]");
                                }
                            } else {
                                player.sendMessage(MessageManager.MAP_NO_SPAWNPOINTS);
                            }
                        } else {
                            player.sendMessage(MessageManager.UNKNOWN_MAP);
                        }
                        break;
                    case "start":
                        xyz.destial.abyssalgames.map.Map votedMap = matchManager.getVoteManager().getVotedMap();
                        if (votedMap != null) {
                            matchManager.prepareMatch();
                            player.sendMessage(MessageManager.FORCE_START);
                        } else {
                            player.sendMessage(MessageManager.UNKNOWN_MATCH);
                        }
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "/ab players, /ab maps, /ab reload, /ab setlobby");
                        break;
                }
            } else {
                player.sendMessage(ChatColor.RED + "/ab players, /ab maps, /ab reload, /ab setlobby");
            }
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(MessageManager.CONSOLE_COMMAND);
        return true;
    }
}
