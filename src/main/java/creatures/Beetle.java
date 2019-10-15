package creatures;

import game.Hive;

public class Beetle extends Tile {
    public Beetle(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.BEETLE);
        setPlayedByPlayer(playedByPlayer);
    }
}
