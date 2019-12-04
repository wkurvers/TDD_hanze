package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpiderMoveHandlerTest {
    private HiveGame game;
    @BeforeEach
    void resetHandler() {
        game = new HiveGame();
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

        game.getPlaceHandler().naivePlayTile(queenTile,-1,0);
        game.getPlaceHandler().naivePlayTile(spiderTile,0,0);

        game.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,0,1);
        game.getPlaceHandler().naivePlayTile(otherTile,0,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,0);
        assertTrue(game.getGenericMoveHandler().getSpiderMoveHandler().canMakeAnyMove(0,0, Hive.Player.WHITE));
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

        game.getPlaceHandler().naivePlayTile(queenTile,-1,0);
        game.getPlaceHandler().naivePlayTile(spiderTile,0,0);

        game.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,1,-2);
        game.getPlaceHandler().naivePlayTile(otherTile,2,-2);
        game.getPlaceHandler().naivePlayTile(otherTile,2,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,2,0);
        game.getPlaceHandler().naivePlayTile(otherTile,1,1);
        game.getPlaceHandler().naivePlayTile(otherTile,0,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,0);

        game.getPlaceHandler().naivePlayTile(otherTile,0,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,1,-2);
        game.getPlaceHandler().naivePlayTile(otherTile,2,-2);
        game.getPlaceHandler().naivePlayTile(otherTile,2,-1);
        game.getPlaceHandler().naivePlayTile(otherTile,2,0);
        game.getPlaceHandler().naivePlayTile(otherTile,1,1);
        game.getPlaceHandler().naivePlayTile(otherTile,0,1);
        game.getPlaceHandler().naivePlayTile(otherTile,-1,1);

        assertFalse(game.getGenericMoveHandler().getSpiderMoveHandler().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }
}
