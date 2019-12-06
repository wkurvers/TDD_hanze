package handlers;

import creatures.Tile;
import game.HiveGame;
import nl.hanze.hive.Hive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AntMoveHandlerTest {
    private HiveGame game;
    @BeforeEach
    void resetHandler() {
        game = new HiveGame();
    }
    @Test
    void testAntCanNotMakeAnyMove() {
        Tile antTile = new Tile();
        antTile.setPlayedByPlayer(Hive.Player.WHITE);
        antTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        game.getCurrentBoard().placeTileAtPosition(0,0,antTile);

        game.getCurrentBoard().placeTileAtPosition(0,-1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(0,-1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(1,-1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(1,-1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(1,0,otherTile);
        game.getCurrentBoard().placeTileAtPosition(1,0,otherTile);

        game.getCurrentBoard().placeTileAtPosition(0,1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(0,1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(-1,1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(-1,1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(-1,0,otherTile);
        game.getCurrentBoard().placeTileAtPosition(-1,0,otherTile);

        assertFalse(game.getGenericMoveHandler().getAntMoveHandler().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }

    @Test
    void testAntCanMakeAnyMove() {
        Tile beeTile = new Tile();
        beeTile.setPlayedByPlayer(Hive.Player.WHITE);
        beeTile.setCreature(Hive.Tile.SOLDIER_ANT);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        game.getCurrentBoard().placeTileAtPosition(0,0,beeTile);

        game.getCurrentBoard().placeTileAtPosition(0,-1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(0,-1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(1,-1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(1,-1,otherTile);

        game.getCurrentBoard().placeTileAtPosition(1,0,otherTile);
        game.getCurrentBoard().placeTileAtPosition(1,0,otherTile);

        game.getCurrentBoard().placeTileAtPosition(0,1,otherTile);
        game.getCurrentBoard().placeTileAtPosition(0,1,otherTile);

        assertTrue(game.getGenericMoveHandler().getAntMoveHandler().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }
}
