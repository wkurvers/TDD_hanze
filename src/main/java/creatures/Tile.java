package creatures;

import game.Hive;

public class Tile {
    private Hive.Player playedByPlayer;
    private Hive.Tile creature;

    public Tile() {

    }
    public void setCreature(Hive.Tile creature) {
        this.creature = creature;
    }

    public void setPlayedByPlayer(Hive.Player playedByPlayer) {
        this.playedByPlayer = playedByPlayer;
    }

    public Hive.Tile getCreature() {
        return creature;
    }

    public Hive.Player getPlayedByPlayer() {
        return playedByPlayer;
    }
}
