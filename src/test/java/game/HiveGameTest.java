package game;

import creatures.Tile;
import handlers.PlaceHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiveGameTest {
    @AfterEach
    void resetGame() {HiveGame.getGame().resetGame();}

    @Test
    public void testFirstPlayerIsWhite() {
        HiveGame game = HiveGame.getGame();
        assertEquals(Hive.Player.WHITE,game.getCurrentPlayer());
    }

    @Test
    public void testSwitchPlayerToBlackAfterFirstMove() {
        HiveGame game = HiveGame.getGame();
        game.switchPlayer();
        assertEquals(Hive.Player.BLACK,game.getCurrentPlayer());
    }

    @Test
    public void testWhiteWinsWhenShouldWin() {
        HiveGame game = HiveGame.getGame();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Board board = Board.getBoardInstance();

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteTile = new Tile();
        whiteTile.setPlayedByPlayer(Hive.Player.WHITE);
        whiteTile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(blackQueenTile.getPlayedByPlayer(),blackQueenTile,0,0);

        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,-1,0);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,0,-1);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,1,-1);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,1,0);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,0,1);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,-1,1);
        assertTrue(game.isWinner(Hive.Player.WHITE));
    }

    @Test
    public void testWhiteWinsWhenShouldNotWin() {
        HiveGame game = HiveGame.getGame();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Board board = Board.getBoardInstance();

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteTile = new Tile();
        whiteTile.setPlayedByPlayer(Hive.Player.WHITE);
        whiteTile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(blackQueenTile.getPlayedByPlayer(),blackQueenTile,0,0);

        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,-1,0);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,0,-1);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,1,-1);
        placeHandler.playTileTest(blackQueenTile.getPlayedByPlayer(),blackQueenTile,1,0);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,0,1);
        placeHandler.playTileTest(whiteTile.getPlayedByPlayer(),whiteTile,-1,1);

        assertFalse(game.isWinner(Hive.Player.WHITE));
    }
}