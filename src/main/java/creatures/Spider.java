package creatures;

import game.Hive;

public class Spider extends Tile {
    public Spider(Hive.Player playedByPlayer) {
        setCreature(Hive.Tile.SPIDER);
        setPlayedByPlayer(playedByPlayer);
    }
}
