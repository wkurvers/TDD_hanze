package game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayedTileRemovedFromTilesToPlay() {
        Player player = new Player();
        ArrayList<Tile> tilesToPlayBefore = new ArrayList<>(player.getTilesToPlay());
        Tile tileToRemove = new Tile("grasshopper");
        player.play(tileToRemove);
        ArrayList<Tile> tilesToPlayAfter = new ArrayList<>(player.getTilesToPlay());
        tilesToPlayBefore.removeAll(tilesToPlayAfter);
        if (tilesToPlayBefore.size() == 1) {
            Tile tileRemoved = tilesToPlayBefore.get(0);
            assertEquals(tileToRemove.getName(),tileRemoved.getName(),"tileToRemove and tileRemoved are not equal");
        } else {
            assertEquals(1,tilesToPlayBefore.size(),"A wrong number of tiles has been removed from tilesToPlay");
        }
    }
}