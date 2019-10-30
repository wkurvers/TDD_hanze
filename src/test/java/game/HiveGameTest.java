package game;

import creatures.Tile;
import handlers.PlaceHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiveGameTest {
    @AfterEach
    void resetGame() {
        HiveGame.getGame().resetGame();
    }

    @Test
    void testFirstPlayerIsWhite() {
        HiveGame game = HiveGame.getGame();
        assertEquals(Hive.Player.WHITE,game.getCurrentPlayer());
    }

    @Test
    void testSwitchPlayerToBlackAfterFirstMove() {
        HiveGame game = HiveGame.getGame();
        game.switchPlayer();
        assertEquals(Hive.Player.BLACK,game.getCurrentPlayer());
    }

    @Test
    void testWhiteWinsWhenShouldWin() {
        HiveGame game = HiveGame.getGame();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();

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
    void testWhiteWinsWhenShouldNotWin() {
        HiveGame game = HiveGame.getGame();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();

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

    @Test
    void testCanPlayTile() {
        HiveGame game = HiveGame.getGame();
        try {
            game.play(Hive.Tile.SPIDER,0,0);
            assertEquals(Board.getBoardInstance().getTopTileAtPosition(0,0).getCreature(),Hive.Tile.SPIDER);
        } catch (Hive.IllegalMove ex) {
            ex.printStackTrace();
            fail("IllegalMove exception thrown");
        }
    }


    private void play2Tiles() throws Hive.IllegalMove {
        HiveGame game = HiveGame.getGame();
        game.play(Hive.Tile.SPIDER,0,0);
        game.play(Hive.Tile.SPIDER,-1,0);
    }

    private void play3Tiles() throws Hive.IllegalMove {
        HiveGame game = HiveGame.getGame();
        game.play(Hive.Tile.SPIDER,0,0);
        game.play(Hive.Tile.SPIDER,-1,0);
        game.play(Hive.Tile.SPIDER,-2,0);
    }

    @Test
    void testBlackCanPlayTileNextToOpponentAfterFirstMove() {
        assertDoesNotThrow(this::play2Tiles);
    }

    @Test
    void testWhiteCanNotPlayTileNextToOpponentAfterTwoMoves() {
        assertThrows(Hive.IllegalMove.class,this::play3Tiles);
    }
}