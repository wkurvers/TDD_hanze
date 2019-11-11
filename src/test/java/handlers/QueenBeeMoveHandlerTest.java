package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueenBeeMoveHandlerTest {
    @AfterEach
    void resetHandler() {
        HiveGame.getGame().resetGame();
    }
    @Test
    void testQueenBeeCanNotMakeAnyMove() {
        Tile beeTile = new Tile();
        beeTile.setPlayedByPlayer(Hive.Player.WHITE);
        beeTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        Board.getBoardInstance().placeTileAtPosition(0,0,beeTile);

        Board.getBoardInstance().placeTileAtPosition(0,-1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(0,-1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(1,-1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(1,-1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(1,0,otherTile);
        Board.getBoardInstance().placeTileAtPosition(1,0,otherTile);

        Board.getBoardInstance().placeTileAtPosition(0,1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(0,1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(-1,1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(-1,1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(-1,0,otherTile);
        Board.getBoardInstance().placeTileAtPosition(-1,0,otherTile);

        assertFalse(QueenBeeMoveHandler.getInstance().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }

    @Test
    void testQueenBeeCanMakeAnyMove() {
        Tile beeTile = new Tile();
        beeTile.setPlayedByPlayer(Hive.Player.WHITE);
        beeTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.BLACK);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        Board.getBoardInstance().placeTileAtPosition(0,0,beeTile);

        Board.getBoardInstance().placeTileAtPosition(0,-1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(0,-1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(1,-1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(1,-1,otherTile);

        Board.getBoardInstance().placeTileAtPosition(1,0,otherTile);
        Board.getBoardInstance().placeTileAtPosition(1,0,otherTile);

        Board.getBoardInstance().placeTileAtPosition(0,1,otherTile);
        Board.getBoardInstance().placeTileAtPosition(0,1,otherTile);

        assertTrue(QueenBeeMoveHandler.getInstance().canMakeAnyMove(0,0, Hive.Player.WHITE));
    }
}
