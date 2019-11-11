package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpiderMoveHandlerTest {
    @AfterEach
    void resetHandler() {
        HiveGame.getGame().resetGame();
    }

    @Test
    void testSpiderCanMakeAnyMove() {
        Tile queenTile = new Tile();
        queenTile.setCreature(Hive.Tile.QUEEN_BEE);
        queenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile spiderTile = new Tile();
        spiderTile.setCreature(Hive.Tile.SPIDER);
        spiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);

        PlaceHandler.getPlaceHandler().naivePlayTile(queenTile,-1,0);
        PlaceHandler.getPlaceHandler().naivePlayTile(spiderTile,0,0);

        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,0);
        assertTrue(SpiderMoveHandler.getInstance().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }

    @Test
    void testSpiderCanNotMakeAnyMove() {
        Tile queenTile = new Tile();
        queenTile.setCreature(Hive.Tile.QUEEN_BEE);
        queenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile spiderTile = new Tile();
        spiderTile.setCreature(Hive.Tile.SPIDER);
        spiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);

        PlaceHandler.getPlaceHandler().naivePlayTile(queenTile,-1,0);
        PlaceHandler.getPlaceHandler().naivePlayTile(spiderTile,0,0);

        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,1,-2);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,-2);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,0);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,1,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,0);

        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,1,-2);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,-2);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,-1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,2,0);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,1,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,0,1);
        PlaceHandler.getPlaceHandler().naivePlayTile(otherTile,-1,1);

        assertFalse(SpiderMoveHandler.getInstance().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }
}
