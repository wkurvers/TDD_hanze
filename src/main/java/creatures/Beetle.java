package creatures;

import nl.hanze.hive.Hive;

public class Beetle extends Tile {
    public Beetle(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.BEETLE);
        setPlayedByPlayer(playedByPlayer);
    }
}
