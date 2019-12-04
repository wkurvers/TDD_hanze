package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceHandlerTest {
    private HiveGame game;
    @BeforeEach
    void resetHandler() {
        game = new HiveGame();
    }

    @Test
    void testCanPlayerPlayTileIfTileLeftToPlay() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        assertTrue(placeHandler.canPlayerPlayTile(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
    }

    @Test
    void testCanPlayerPlayTileIfNoTileLeftToPlay() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        placeHandler.removeTileFromTilesToPlay(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        assertFalse(placeHandler.canPlayerPlayTile(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
    }

    @Test
    void testCheckIsLocationEmptyWhenEmpty() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        assertTrue(placeHandler.checkLocationEmpty(0,0));
    }

    @Test
    void testCheckIsLocationEmptyWhenNotEmpty() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Board gameBoard = game.getCurrentBoard();
        Tile tileToPlace = new Tile();
        gameBoard.placeTileAtPosition(0,0,tileToPlace);
        assertFalse(placeHandler.checkLocationEmpty(0,0));
    }

    @Test
    void testCanPlayTileWithoutContact() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        assertFalse(placeHandler.checkContactExists(0,0));
    }

    @Test
    void testCanPlayTileWithContact() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Board gameBoard = game.getCurrentBoard();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        gameBoard.placeTileAtPosition(0,0,tileToPlace);
        assertTrue(placeHandler.checkContactExists(-1,0));
    }

    @Test
    void testCanPlayTileWithOpponentContact() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.BLACK);
        assertFalse(placeHandler.isNotOpponent(tileToPlace));
    }

    @Test
    void testCanPlayTileWithoutOpponentContact() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        assertTrue(placeHandler.isNotOpponent(tileToPlace));
    }

    @Test
    void checkMustPlayQueenIfQueenHasNotBeenPlayed() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        tileToPlace.setCreature(Hive.Tile.GRASSHOPPER);
        placeHandler.playTileTest(tileToPlace.getPlayedByPlayer(),tileToPlace,0,0);
        placeHandler.playTileTest(tileToPlace.getPlayedByPlayer(),tileToPlace,-1,0);
        placeHandler.playTileTest(tileToPlace.getPlayedByPlayer(),tileToPlace,1,0);
        assertTrue(placeHandler.mustPlayQueen(tileToPlace.getPlayedByPlayer()));
    }

    @Test
    void checkMustPlayedQueenWhenQueenHasBeenPlayed() {
        PlaceHandler placeHandler = game.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        tileToPlace.setCreature(Hive.Tile.GRASSHOPPER);

        Tile queenTile = new Tile();
        queenTile.setPlayedByPlayer(Hive.Player.WHITE);
        queenTile.setCreature(Hive.Tile.QUEEN_BEE);

        placeHandler.playTileTest(tileToPlace.getPlayedByPlayer(),tileToPlace,0,0);
        placeHandler.playTileTest(tileToPlace.getPlayedByPlayer(),tileToPlace,-1,0);
        placeHandler.playTileTest(queenTile.getPlayedByPlayer(),queenTile,1,0);
        assertFalse(placeHandler.mustPlayQueen(tileToPlace.getPlayedByPlayer()));
    }
}