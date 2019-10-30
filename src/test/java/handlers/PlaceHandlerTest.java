package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceHandlerTest {

    @AfterEach
    void resetHandler() {
        PlaceHandler.getPlaceHandler().reset();
        Board.getBoardInstance().resetBoard();
    }

    @Test
    void testCanPlayerPlayTileIfTileLeftToPlay() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        assertTrue(placeHandler.canPlayerPlayTile(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
    }

    @Test
    void testCanPlayerPlayTileIfNoTileLeftToPlay() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        placeHandler.removeTileFromTilesToPlay(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        assertFalse(placeHandler.canPlayerPlayTile(Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
    }

    @Test
    void testCheckIsLocationEmptyWhenEmpty() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        assertTrue(placeHandler.checkLocationEmpty(0,0));
    }

    @Test
    void testCheckIsLocationEmptyWhenNotEmpty() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Board gameBoard = Board.getBoardInstance();
        Tile tileToPlace = new Tile();
        gameBoard.placeTileAtPosition(0,0,tileToPlace);
        assertFalse(placeHandler.checkLocationEmpty(0,0));
    }

    @Test
    void testCanPlayTileWithoutContact() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        assertFalse(placeHandler.checkContactExists(0,0));
    }

    @Test
    void testCanPlayTileWithContact() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Board gameBoard = Board.getBoardInstance();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        gameBoard.placeTileAtPosition(0,0,tileToPlace);
        assertTrue(placeHandler.checkContactExists(-1,0));
    }

    @Test
    void testCanPlayTileWithOpponentContact() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.BLACK);
        assertFalse(placeHandler.isNotOpponent(tileToPlace));
    }

    @Test
    void testCanPlayTileWithoutOpponentContact() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        assertTrue(placeHandler.isNotOpponent(tileToPlace));
    }

    @Test
    void checkMustPlayQueenIfQueenHasNotBeenPlayed() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
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
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
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