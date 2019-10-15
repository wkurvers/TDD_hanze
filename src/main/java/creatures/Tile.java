package creatures;

import game.Hive;

public class Tile {
    private Hive.Player playedByPlayer;
    private Hive.Tile creature;

    public Tile() {

    }
    protected void setCreature(Hive.Tile creature) {
        this.creature = creature;
    }

    protected void setPlayedByPlayer(Hive.Player playedByPlayer) {
        this.playedByPlayer = playedByPlayer;
    }

    public Hive.Tile getCreature() {
        return creature;
    }
}
