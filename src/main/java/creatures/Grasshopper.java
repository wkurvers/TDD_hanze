package creatures;

import nl.hanze.hive.Hive;

public class Grasshopper extends Tile {
    public Grasshopper(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.GRASSHOPPER);
        setPlayedByPlayer(playedByPlayer);
    }
}
