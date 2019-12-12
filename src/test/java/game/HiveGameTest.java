package game;

import creatures.Tile;
import handlers.PlaceHandler;
import nl.hanze.hive.Hive;
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
    void testBlackWinsWhenShouldWin() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackTile = new Tile();
        blackTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);

        placeHandler.playTileTest(whiteQueenTile.getPlayedByPlayer(),whiteQueenTile,0,0);

        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,-1,0);
        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,0,-1);
        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,1,-1);
        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,1,0);
        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,0,1);
        placeHandler.playTileTest(blackTile.getPlayedByPlayer(),blackTile,-1,1);
        assertTrue(game.isWinner(Hive.Player.BLACK));
    }

    @Test
    void testIsDraw() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackQueenTile = new Tile();
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackTile = new Tile();
        blackTile.setPlayedByPlayer(Hive.Player.BLACK);
        blackTile.setCreature(Hive.Tile.GRASSHOPPER);

        Tile whiteTile = new Tile();
        whiteTile.setPlayedByPlayer(Hive.Player.WHITE);
        whiteTile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.naivePlayTile(blackQueenTile,-2,0);

        placeHandler.naivePlayTile(whiteQueenTile,1,0);

        placeHandler.naivePlayTile(whiteTile,-2,-1);
        placeHandler.naivePlayTile(whiteTile,-1,-1);
        placeHandler.naivePlayTile(whiteTile,-1,0);
        placeHandler.naivePlayTile(whiteTile,-2,1);
        placeHandler.naivePlayTile(whiteTile,-3,1);
        placeHandler.naivePlayTile(whiteTile,-3,0);

        placeHandler.naivePlayTile(blackTile,0,1);
        placeHandler.naivePlayTile(blackTile,1,1);
        placeHandler.naivePlayTile(blackTile,2,0);
        placeHandler.naivePlayTile(blackTile,2,-1);
        placeHandler.naivePlayTile(blackTile,1,-1);
        placeHandler.naivePlayTile(blackTile,0,0);
        assertTrue(game.isDraw());
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
            game.play(Hive.Tile.QUEEN_BEE,1,0); //WHITE
            game.play(Hive.Tile.BEETLE,-2,0); //BLACK
            game.move(1,0,1,-1); //WHITE
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

    @Test
    void testPlayQueenLaterThenTurnFour() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.GRASSHOPPER, 0,0);//WHITE
            game.play(Hive.Tile.GRASSHOPPER, -1,0);//BLACK
            game.play(Hive.Tile.GRASSHOPPER, 1,0);//WHITE
            game.play(Hive.Tile.GRASSHOPPER, -2,0);
        });
    }

    @Test
    void testMoveWithoutPlayedQueen() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.GRASSHOPPER, 0,0);//WHITE
            game.play(Hive.Tile.GRASSHOPPER, -1,0);//BLACK
            game.move(0,0,-2,0);//WHITE
        });
    }

    @Test
    void testMoveBeetleOnStack() {
        assertDoesNotThrow(()-> {
            game.play(Hive.Tile.QUEEN_BEE,0,0);
            game.play(Hive.Tile.QUEEN_BEE,-1,0);
            game.play(Hive.Tile.BEETLE,1,-1);
            game.play(Hive.Tile.BEETLE,-1,-1);
            game.move(1,-1,0,0);
        });
    }

    @Test
    void testMoveOpposingQueen() {
        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.BEETLE,0,0);
            game.play(Hive.Tile.BEETLE,-1,0);
            game.play(Hive.Tile.QUEEN_BEE,1,0);
            game.play(Hive.Tile.QUEEN_BEE,-2,0);
            game.move(-2,0,0,-1);
        });
    }

    @Test
    void testSlideAntToSurroundedLocation() {
        Tile blackTile = new Tile();
        blackTile.setPlayedByPlayer(Hive.Player.BLACK);
        blackTile.setCreature(Hive.Tile.BEETLE);

        Tile whiteSoldierTile = new Tile();
        whiteSoldierTile.setPlayedByPlayer(Hive.Player.WHITE);
        whiteSoldierTile.setCreature(Hive.Tile.SOLDIER_ANT);
        PlaceHandler placeHandler = game.getPlaceHandler();

        placeHandler.naivePlayTile(blackTile,0,0);
        placeHandler.naivePlayTile(blackTile,-1,0);
        placeHandler.naivePlayTile(blackTile,-1,-1);
        placeHandler.naivePlayTile(blackTile,0,-2);
        placeHandler.naivePlayTile(blackTile,1,-2);
        placeHandler.naivePlayTile(blackTile,1,-1);

        placeHandler.naivePlayTile(whiteSoldierTile,1,0);

        assertThrows(Hive.IllegalMove.class, ()-> {
           game.move(1,0,0,-1);
        });
    }

    @Test
    void testSlideQueenBeeSituation1() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackAntTile,0,-2);
        placeHandler.naivePlayTile(blackQueenTile,-1,-1);
        placeHandler.naivePlayTile(blackGrasshopperTile,-2,0);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-2,1);
        placeHandler.naivePlayTile(whiteAntTile,-1,1);
        placeHandler.naivePlayTile(whiteBeetleTile,0,1);
        placeHandler.naivePlayTile(whiteQueenTile,0,0);
        placeHandler.naivePlayTile(blackBeetleTile,1,0);
        placeHandler.naivePlayTile(blackSpiderTile,1,-1);
        placeHandler.naivePlayTile(whiteSpiderTile,1,-2);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.GRASSHOPPER,-2,2);//WHITE
            game.move(-1,-1,0,-1);
        });
    }

    @Test
    void testSlideQueenBeeSituation2() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackAntTile,0,-2);
        placeHandler.naivePlayTile(blackQueenTile,-2,-1);
        placeHandler.naivePlayTile(blackGrasshopperTile,-2,0);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-2,1);
        placeHandler.naivePlayTile(whiteAntTile,-1,1);
        placeHandler.naivePlayTile(whiteBeetleTile,0,1);
        placeHandler.naivePlayTile(whiteQueenTile,0,0);
        placeHandler.naivePlayTile(blackBeetleTile,1,0);
        placeHandler.naivePlayTile(blackSpiderTile,1,-1);
        placeHandler.naivePlayTile(whiteSpiderTile,1,-2);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.GRASSHOPPER,-2,2);//WHITE
        });

        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(-2,-1,-1,-2);
        });
    }

    @Test
    void testSlideBeetleSituation1() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        placeHandler.naivePlayTile(whiteBeetleTile,2,-2);
        placeHandler.naivePlayTile(whiteSpiderTile,1,-2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,1,-1);
        placeHandler.naivePlayTile(whiteQueenTile,0,-1);
        placeHandler.naivePlayTile(blackQueenTile,1,0);
        placeHandler.naivePlayTile(blackBeetleTile,2,0);

        assertDoesNotThrow(() -> {
            game.move(2,-2,1,-2);
        });
    }

    @Test
    void testSlideBeetleSituation2() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        placeHandler.naivePlayTile(whiteAntTile,2,-3);
        placeHandler.naivePlayTile(whiteAntTile,2,-3);
        placeHandler.naivePlayTile(whiteBeetleTile,2,-2);
        placeHandler.naivePlayTile(whiteSpiderTile,1,-2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,1,-1);
        placeHandler.naivePlayTile(whiteGrasshopperTile,1,-1);
        placeHandler.naivePlayTile(whiteQueenTile,0,-1);
        placeHandler.naivePlayTile(blackQueenTile,1,0);
        placeHandler.naivePlayTile(blackBeetleTile,2,0);

        assertThrows(Hive.IllegalMove.class,() -> {
            game.move(2,-2,1,-2);
        });
    }

    @Test
    void testJumpGrasshopperSituation1() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        placeHandler.naivePlayTile(whiteSpiderTile,0,0);
        placeHandler.naivePlayTile(whiteAntTile,1,-1);
        placeHandler.naivePlayTile(blackAntTile,1,-2);
        placeHandler.naivePlayTile(blackQueenTile,0,-2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-1,-1);
        placeHandler.naivePlayTile(whiteBeetleTile,-2,0);
        placeHandler.naivePlayTile(blackGrasshopperTile,-1,0);
        placeHandler.naivePlayTile(whiteQueenTile,-1,1);
        placeHandler.naivePlayTile(blackBeetleTile,0,1);
        placeHandler.naivePlayTile(blackSpiderTile,0,2);
        placeHandler.naivePlayTile(whiteSpiderTile,-1,3);
        placeHandler.naivePlayTile(blackSpiderTile,-2,3);

        assertDoesNotThrow(() -> {
            game.move(-1,-1,-1,2);
        });
    }

    @Test
    void testJumpGrasshopperSituation2() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        placeHandler.naivePlayTile(whiteSpiderTile,0,0);
        placeHandler.naivePlayTile(whiteAntTile,1,-1);
        placeHandler.naivePlayTile(blackAntTile,1,-2);
        placeHandler.naivePlayTile(blackQueenTile,0,-2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-1,-1);
        placeHandler.naivePlayTile(whiteBeetleTile,-2,0);
        placeHandler.naivePlayTile(blackGrasshopperTile,-1,0);
        placeHandler.naivePlayTile(whiteQueenTile,-1,1);
        placeHandler.naivePlayTile(blackBeetleTile,0,1);
        placeHandler.naivePlayTile(blackSpiderTile,0,2);
        placeHandler.naivePlayTile(whiteSpiderTile,-1,3);
        placeHandler.naivePlayTile(blackSpiderTile,-2,3);

        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(-1,-1,2,-1);
        });
    }

    @Test
    void testSpiderSlideSituation1() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackSpiderTile,3,-2);
        placeHandler.naivePlayTile(whiteSpiderTile,3,-1);
        placeHandler.naivePlayTile(whiteQueenTile,3,0);
        placeHandler.naivePlayTile(blackBeetleTile,2,1);
        placeHandler.naivePlayTile(whiteAntTile,1,2);
        placeHandler.naivePlayTile(blackGrasshopperTile,0,2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-1,2);
        placeHandler.naivePlayTile(blackQueenTile,-1,1);
        placeHandler.naivePlayTile(blackAntTile,0,0);
        placeHandler.naivePlayTile(whiteBeetleTile,1,-1);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.SOLDIER_ANT,1,3);
        });

        assertDoesNotThrow(() -> {
            game.move(3,-2,0,1);
        });
    }

    @Test
    void testSpiderSlideSituation2() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackSpiderTile = new Tile();
        blackSpiderTile.setCreature(Hive.Tile.SPIDER);
        blackSpiderTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteSpiderTile = new Tile();
        whiteSpiderTile.setCreature(Hive.Tile.SPIDER);
        whiteSpiderTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteAntTile = new Tile();
        whiteAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        whiteAntTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackGrasshopperTile = new Tile();
        blackGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        blackGrasshopperTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackSpiderTile,3,-2);
        placeHandler.naivePlayTile(whiteSpiderTile,3,-1);
        placeHandler.naivePlayTile(whiteQueenTile,3,0);
        placeHandler.naivePlayTile(blackBeetleTile,2,1);
        placeHandler.naivePlayTile(whiteAntTile,1,2);
        placeHandler.naivePlayTile(blackGrasshopperTile,0,2);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-1,2);
        placeHandler.naivePlayTile(blackQueenTile,-1,1);
        placeHandler.naivePlayTile(blackAntTile,0,0);
        placeHandler.naivePlayTile(whiteBeetleTile,1,-1);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.SOLDIER_ANT,1,3);
        });

        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(3,-2,0,-1);
        });
    }

    @Test
    void testAntSlideSituation1() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackQueenTile,-1,-1);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-2,0);
        placeHandler.naivePlayTile(whiteBeetleTile,-2,1);
        placeHandler.naivePlayTile(blackAntTile,-2,2);
        placeHandler.naivePlayTile(blackBeetleTile,-1,1);
        placeHandler.naivePlayTile(whiteQueenTile,0,0);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.SOLDIER_ANT,1,-1);
        });

        assertDoesNotThrow(() -> {
            game.move(-2,2,0,-1);
        });
    }

    @Test
    void testAntSlideSituation2() {
        PlaceHandler placeHandler = game.getPlaceHandler();

        Tile blackQueenTile = new Tile();
        blackQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        blackQueenTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteGrasshopperTile = new Tile();
        whiteGrasshopperTile.setCreature(Hive.Tile.GRASSHOPPER);
        whiteGrasshopperTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile whiteBeetleTile = new Tile();
        whiteBeetleTile.setCreature(Hive.Tile.BEETLE);
        whiteBeetleTile.setPlayedByPlayer(Hive.Player.WHITE);

        Tile blackAntTile = new Tile();
        blackAntTile.setCreature(Hive.Tile.SOLDIER_ANT);
        blackAntTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile blackBeetleTile = new Tile();
        blackBeetleTile.setCreature(Hive.Tile.BEETLE);
        blackBeetleTile.setPlayedByPlayer(Hive.Player.BLACK);

        Tile whiteQueenTile = new Tile();
        whiteQueenTile.setCreature(Hive.Tile.QUEEN_BEE);
        whiteQueenTile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(blackQueenTile,-1,-1);
        placeHandler.naivePlayTile(whiteGrasshopperTile,-2,0);
        placeHandler.naivePlayTile(whiteBeetleTile,-2,1);
        placeHandler.naivePlayTile(blackAntTile,-2,2);
        placeHandler.naivePlayTile(blackBeetleTile,-1,1);
        placeHandler.naivePlayTile(whiteQueenTile,0,0);

        assertDoesNotThrow(() -> {
            game.play(Hive.Tile.SOLDIER_ANT,1,-1);
        });

        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(-2,2,2,0);
        });
    }
}