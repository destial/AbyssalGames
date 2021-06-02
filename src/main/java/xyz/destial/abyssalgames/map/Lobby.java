package xyz.destial.abyssalgames.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.manager.MatchManager;
import xyz.destial.abyssalgames.manager.MessageManager;

public class Lobby {
    private Location spawn;
    private int announcerID;

    public Lobby(Location spawn) {
        this.spawn = spawn;
        announcerID = -1;
        announceVote();
    }
    
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.world", spawn.getWorld().getName());
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.x", spawn.getX());
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.y", spawn.getY());
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.z", spawn.getZ());
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.yaw", spawn.getYaw());
        AbyssalGames.getPlugin().getConfigManager().getConfig().set("lobby.pitch", spawn.getPitch());
        AbyssalGames.getPlugin().getConfigManager().save();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void teleport(Player player) {
        player.teleport(spawn);
    }

    public void deannounceVote() {
        if (announcerID == -1) return;
        Bukkit.getScheduler().cancelTask(announcerID);
        announcerID = -1;
    }

    public List<ItemStack> getLobbyItems() {
        List<ItemStack> list = new ArrayList<>();

        return list;
    }

    public void announceVote() {
        if (announcerID != -1) return;
        announcerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbyssalGames.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (AbyssalGames.getPlugin().getMatchManager().isLobby() && AbyssalGames.getPlugin().getMatchManager().getAllPlayers().size() > 0) {
                    Bukkit.broadcastMessage(MessageManager.VOTE_MESSAGE);
                    MatchManager matchManager = AbyssalGames.getPlugin().getMatchManager();
                    matchManager.playSound(Sound.CLICK);
                }
            }
            
        }, 0L, 3600L);
    }
}
