package creatures;

import game.Hive;

public class Ant extends Tile {
    public Ant(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.SOLDIER_ANT);
        setPlayedByPlayer(playedByPlayer);
    }
}
