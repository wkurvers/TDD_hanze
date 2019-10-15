package handlers;

import game.Hive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceHandlerTest {

    @Test
    void testCheckCanPlayFirstTile() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.BLACK);
        assertTrue(placeHandler.checkCanPlayTile());
    }

    @Test
    void testCheckCanPlay4thTileWithoutQueen() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.BLACK);
        assertTrue(placeHandler.checkCanPlayTile());
    }

    @Test
    void testCheckIsLocationEmptyWhenEmpty() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.WHITE);
        assertTrue(placeHandler.checkLocationEmpty());
    }

    @Test
    void testCheckIsLocationEmptyWhenNotEmpty() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.WHITE);
        assertTrue(placeHandler.checkLocationEmpty());
    }
    @Test
    void testCheckContactExists() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.BLACK);
        assertTrue(placeHandler.checkContactExists());
    }

    @Test
    void checkHasPlayedQueenWhenQueenHasNotBeenPlayed() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.BLACK);
        assertTrue(placeHandler.checkHasPlayedQueen());
    }

    @Test
    void checkHasPlayedQueenWhenQueenHasBeenPlayed() {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler(Hive.Player.WHITE);
        assertTrue(placeHandler.checkHasPlayedQueen());
    }
}