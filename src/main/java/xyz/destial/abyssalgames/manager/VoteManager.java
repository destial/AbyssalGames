package xyz.destial.abyssalgames.manager;

import xyz.destial.abyssalgames.AbyssalGames;
import xyz.destial.abyssalgames.map.Map;

import java.util.ArrayList;
import java.util.Random;

public class VoteManager {
    private final ArrayList<Map> maps;

    public VoteManager() {
        maps = new ArrayList<>();
        createMaps();
    }

    public void createMaps() {
        for (Map map : maps) {
            map.clearVotes();
        }
        maps.clear();
        MapManager mapManager = AbyssalGames.getPlugin().getMapManager();
        Random random = new Random();
        if (mapManager.getMaps().size() > 5) {
            while (maps.size() < 5) {
                maps.add((Map) mapManager.getMaps().values().toArray()[random.nextInt(mapManager.getMaps().size() - 1)]);
            }
        } else {
            maps.addAll(mapManager.getMaps().values());
        }
    }

    public Map vote(int index) {
        if (index > 0 && index <= maps.size()) {
            Map map = maps.get(index - 1);
            map.addVote();
            return map;
        }
        return null;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public Map getVotedMap() {
        Map votedMap = null;
        for (Map map : getMaps()) {
            if (votedMap == null) {
                votedMap = map;
                continue;
            }
            if (map.getVotes() > votedMap.getVotes()) {
                votedMap = map;
            }
        }
        return votedMap;
    }
}
