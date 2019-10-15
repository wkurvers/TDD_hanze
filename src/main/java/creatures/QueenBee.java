package creatures;

import game.Hive;

public class QueenBee extends Tile {
    public QueenBee(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.QUEEN_BEE);
        setPlayedByPlayer(playedByPlayer);
    }
}
