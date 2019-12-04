package game;

import creatures.QueenBee;
import creatures.Tile;
import handlers.PlaceHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiveGameTest {
    private HiveGame game;
    @BeforeEach
    void resetGame() {
        game = new HiveGame();
    }

    @Test
    void testFirstPlayerIsWhite() {
        assertEquals(Hive.Player.WHITE,game.getCurrentPlayer());
    }

    @Test
    void testSwitchPlayerToBlackAfterFirstMove() {
        game.switchPlayer();
        assertEquals(Hive.Player.BLACK,game.getCurrentPlayer());
    }

    @Test
    void testWhiteWinsWhenShouldWin() {
        PlaceHandler placeHandler = game.getPlaceHandler();

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
        PlaceHandler placeHandler = game.getPlaceHandler();

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
        try {
            game.play(Hive.Tile.SPIDER,0,0);
            assertEquals(game.getCurrentBoard().getTopTileAtPosition(0,0).getCreature(),Hive.Tile.SPIDER);
        } catch (Hive.IllegalMove ex) {
            ex.printStackTrace();
            fail("IllegalMove exception thrown");
        }
    }

    @Test
    void testBlackCanPlayTileNextToOpponentAfterFirstMove() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.SPIDER,0,0);
            game.play(Hive.Tile.SPIDER,-1,0);
        });
    }

    @Test
    void testWhiteCanNotPlayTileNextToOpponentAfterTwoMoves() {
        assertThrows(Hive.IllegalMove.class,() -> {
            game.play(Hive.Tile.SPIDER,0,0);
            game.play(Hive.Tile.SPIDER,-1,0);
            game.play(Hive.Tile.SPIDER,-2,0);
        });
    }

    @Test
    void testBlackCanMoveWhenValidMove() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.SOLDIER_ANT,1,-1); //WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-1,-1); //BLACK
            game.play(Hive.Tile.SOLDIER_ANT,1,0); //WHITE

            game.move(-1,-1,-2,0); //BLACK
        });
    }

    @Test
    void testBlackCannotMoveWhenInvalidMove() {
        assertThrows(Hive.IllegalMove.class,() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.SOLDIER_ANT,1,-1); //WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-1,-1); //BLACK
            game.play(Hive.Tile.SOLDIER_ANT,1,0); //WHITE

            game.move(-1,0,-2,0); //BLACK
        });
    }

    @Test
    void testValidBeetleSlide() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.BEETLE,1,-1); //WHITE
            game.play(Hive.Tile.BEETLE,-2,0); //BLACK
            game.move(1,-1,1,0); //WHITE
        });
    }

    @Test
    void testInvalidBeetleSlide() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.BEETLE,1,-1); //WHITE
            game.play(Hive.Tile.BEETLE,-2,0); //BLACK
            game.move(1,-1,2,0); //WHITE
        });
    }

    @Test
    void testValidQueenSlide() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.BEETLE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.QUEEN_BEE,0,1); //WHITE
            game.play(Hive.Tile.BEETLE,-1,-1); //BLACK
            game.move(0,1,-1,1); //WHITE
        });
    }

    @Test
    void testInvalidQueenSlide() {
        assertThrows(Hive.IllegalMove.class,() -> {
            game.play(Hive.Tile.BEETLE,0,0); //WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0); //BLACK
            game.play(Hive.Tile.QUEEN_BEE,0,1); //WHITE
            game.play(Hive.Tile.BEETLE,-1,-1); //BLACK
            game.move(0,1,0,0); //WHITE
        });
    }

    @Test
    void testValidSoldierAntSlide() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,1);//BLACK
            game.play(Hive.Tile.BEETLE,1,0);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-1,2);//BLACK
            game.play(Hive.Tile.BEETLE,1,1);//WHITE

            game.move(-1,2,1,-1); //BLACK
        });
    }

    @Test
    void testInvalidSoldierAntSlide() {
        assertThrows(Hive.IllegalMove.class,() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,1);//BLACK
            game.play(Hive.Tile.BEETLE,1,0);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-1,2);//BLACK
            game.play(Hive.Tile.BEETLE,1,1);//WHITE

            game.move(-1,2,-3,0); //BLACK
        });
    }

    @Test
    void testValidSpiderSlide() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.QUEEN_BEE,1,-1);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,1,0);//BLACK
            game.play(Hive.Tile.SPIDER,1,-2);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,0,1);//BLACK

            game.move(1,-2,2,0); //WHITE
        });
    }

    @Test
    void testInvalidSpiderSlide() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.QUEEN_BEE,1,-1);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,1,0);//BLACK
            game.play(Hive.Tile.SPIDER,1,-2);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,0,1);//BLACK

            game.move(1,-2,1,1); //WHITE
        });
    }

    @Test
    void testValidGrasshopperMove() {
        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0);//BLACK
            game.play(Hive.Tile.GRASSHOPPER,1,0);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-2,0);//BLACK

            game.move(1,0,-3,0); //WHITE
        });
    }

    @Test
    void testInvalidGrasshopperMove() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.QUEEN_BEE,0,0);//WHITE
            game.play(Hive.Tile.QUEEN_BEE,-1,0);//BLACK
            game.play(Hive.Tile.GRASSHOPPER,1,0);//WHITE
            game.play(Hive.Tile.SOLDIER_ANT,-1,-1);//BLACK

            game.move(1,0,-3,0); //WHITE
        });
    }
}