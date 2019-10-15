package game;

import creatures.*;

public class TileFactory {
    public static Tile makeTile(Hive.Tile creature, Hive.Player playedByPlayer) {
        Tile tile = null;
        switch (creature) {
            case SOLDIER_ANT:
                return new Ant(playedByPlayer);
            case SPIDER:
                return new Spider(playedByPlayer);
            case QUEEN_BEE:
                return new QueenBee(playedByPlayer);
            case GRASSHOPPER:
                return new Grasshopper(playedByPlayer);
            case BEETLE:
                return new Beetle(playedByPlayer);
            default:
                return tile;
        }
    }
}
