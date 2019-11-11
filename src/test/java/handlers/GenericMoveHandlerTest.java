package handlers;
import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import game.TileFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
public class GenericMoveHandlerTest {

    @AfterEach
    void resetHandler() {
        HiveGame.getGame().resetGame();
    }

    @Test
    void testCanMoveATile() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        Tile tileToPlace = new Tile();
        PlaceHandler.getPlaceHandler().playTileTest(Hive.Player.WHITE,tileToPlace,0,0);
        int oldStackSizeBefore = Board.getBoardInstance().getPosition(0,0).size();

        int newStackSizeBefore;
        if (Board.getBoardInstance().getPosition(-1,0) == null) {
            newStackSizeBefore = 0;
        } else {
            newStackSizeBefore = Board.getBoardInstance().getPosition(-1,0).size();
        }

        genericMoveHandler.moveTileTest(0,0,-1,0);
        int oldStackSizeAfter;
        if (Board.getBoardInstance().getPosition(0,0) == null) {
            oldStackSizeAfter = 0;
        } else {
            oldStackSizeAfter = Board.getBoardInstance().getPosition(0,0).size();
        }

        int newStackSizeAfter = Board.getBoardInstance().getPosition(-1,0).size();

        boolean movedFromOldPlace = oldStackSizeAfter == oldStackSizeBefore - 1;
        boolean movedToNewPlace = newStackSizeBefore + 1 == newStackSizeAfter;

        assertTrue(movedFromOldPlace && movedToNewPlace);
    }

    void moveATile() throws Hive.IllegalMove {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        genericMoveHandler.moveTileTestNoTile(0,0,-1,1, Hive.Player.WHITE);
    }

    void blackMovesWhiteTile() throws Hive.IllegalMove {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        PlaceHandler.getPlaceHandler().playTileTest(Hive.Player.WHITE,tileToPlace,0,0);
        genericMoveHandler.moveTileTestWrongPlayer(0,0,-1,1, Hive.Player.BLACK);
    }

    void moveTileWhenQueenNotPlaced() throws Hive.IllegalMove {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        PlaceHandler.getPlaceHandler().playTileTest(Hive.Player.WHITE,tileToPlace,0,0);
        genericMoveHandler.moveTileTestNoQueen(0,0,-1,1, Hive.Player.WHITE);
    }

    void moveTileToNoContactLocation() throws Hive.IllegalMove {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        Tile tileToPlace = new Tile();
        tileToPlace.setPlayedByPlayer(Hive.Player.WHITE);
        PlaceHandler.getPlaceHandler().playTileTest(Hive.Player.WHITE,tileToPlace,0,0);
        genericMoveHandler.moveTileTestContact(0,0,-1,1, Hive.Player.WHITE);
    }
    @Test
    void testCannotEmptyLocation() {
        assertThrows(Hive.IllegalMove.class,this::moveATile);
    }

    @Test
    void testBlackCannotMoveWhiteTile() {
        assertThrows(Hive.IllegalMove.class,this::blackMovesWhiteTile);
    }

    @Test
    void testCannotMoveTileWhenQueenNotPlaced() {
        assertThrows(Hive.IllegalMove.class,this::moveTileWhenQueenNotPlaced);
    }

    @Test
    void testCannotMoveToNoContactLocation() {
        assertThrows(Hive.IllegalMove.class,this::moveTileToNoContactLocation);
    }

    @Test
    void testBfsTileCountSearch() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile queenBeeTile = new Tile();
        queenBeeTile.setPlayedByPlayer(Hive.Player.WHITE);
        queenBeeTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.WHITE);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        placeHandler.playTileTest(Hive.Player.WHITE,queenBeeTile,0,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-2,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,1,0);

        assertEquals(4,genericMoveHandler.getTotalTileCount(Hive.Player.WHITE));
    }

    @Test
    void testNoIslandExistsWhenDont() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile queenBeeTile = new Tile();
        queenBeeTile.setPlayedByPlayer(Hive.Player.WHITE);
        queenBeeTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.WHITE);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        placeHandler.playTileTest(Hive.Player.WHITE,queenBeeTile,0,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-2,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,1,0);

        int tileCountBefore = genericMoveHandler.getTotalTileCount(Hive.Player.WHITE);

        genericMoveHandler.moveTileTest(-2,0,2,0);

        int tileCountAfter = genericMoveHandler.getTotalTileCount(Hive.Player.WHITE);

        assertEquals(tileCountBefore,tileCountAfter);
    }

    @Test
    void testNoIslandExistsWhenDo() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile queenBeeTile = new Tile();
        queenBeeTile.setPlayedByPlayer(Hive.Player.WHITE);
        queenBeeTile.setCreature(Hive.Tile.QUEEN_BEE);

        Tile otherTile = new Tile();
        otherTile.setPlayedByPlayer(Hive.Player.WHITE);
        otherTile.setCreature(Hive.Tile.SOLDIER_ANT);
        placeHandler.playTileTest(Hive.Player.WHITE,queenBeeTile,0,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,-2,0);
        placeHandler.playTileTest(Hive.Player.WHITE,otherTile,1,0);

        int tileCountBefore = genericMoveHandler.getTotalTileCount(Hive.Player.WHITE);

        genericMoveHandler.moveTileTest(0,0,2,0);

        int tileCountAfter = genericMoveHandler.getTotalTileCount(Hive.Player.WHITE);

        assertNotEquals(tileCountBefore,tileCountAfter);
    }

    @Test
    void testSlideIsOneMove() {
        assertTrue(GenericMoveHandler.getGenericMoveHandler().checkSlideDistanceIsOne(0,2,-1,2));
    }

    @Test
    void testSlideIsBiggerThenOneMove() {
        assertFalse(GenericMoveHandler.getGenericMoveHandler().checkSlideDistanceIsOne(0,2,-2,2));
    }

    @Test
    void testSlideWhenDirectionBlocked() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setPlayedByPlayer(Hive.Player.WHITE);
        tile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,0);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,1);

        assertFalse(genericMoveHandler.checkCommonNeighboursSize(0,0,-1,1));
    }

    @Test
    void testSlideWhenDirectionNotBlocked() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setPlayedByPlayer(Hive.Player.WHITE);
        tile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,0);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,1);

        assertTrue(genericMoveHandler.checkCommonNeighboursSize(0,0,-1,1));
    }

    @Test
    void testSlideWhenLoseContact() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setPlayedByPlayer(Hive.Player.WHITE);
        tile.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,0);

        placeHandler.playTileTest(Hive.Player.WHITE,tile,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,0,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,-2,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile,-2,2);
        Tile tile2 = Board.getBoardInstance().removeTopTileAtPosition(-2,2);
        assertFalse(genericMoveHandler.checkWhileSlidingKeepContact(-2,2,-1,2));
    }

    @Test
    void testSlideWhenKeepContact() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile1 = new Tile();
        Tile tile2 = new Tile();
        Tile tile3 = new Tile();
        Tile tile4 = new Tile();
        Tile tile5 = new Tile();
        Tile tile6 = new Tile();
        tile1.setPlayedByPlayer(Hive.Player.WHITE);
        tile1.setCreature(Hive.Tile.SOLDIER_ANT);

        tile2.setPlayedByPlayer(Hive.Player.WHITE);
        tile2.setCreature(Hive.Tile.SOLDIER_ANT);

        tile3.setPlayedByPlayer(Hive.Player.WHITE);
        tile3.setCreature(Hive.Tile.SOLDIER_ANT);

        tile4.setPlayedByPlayer(Hive.Player.WHITE);
        tile4.setCreature(Hive.Tile.SOLDIER_ANT);

        tile5.setPlayedByPlayer(Hive.Player.WHITE);
        tile5.setCreature(Hive.Tile.SOLDIER_ANT);

        tile6.setPlayedByPlayer(Hive.Player.WHITE);
        tile6.setCreature(Hive.Tile.SOLDIER_ANT);

        placeHandler.playTileTest(Hive.Player.WHITE,tile1,0,0);

        placeHandler.playTileTest(Hive.Player.WHITE,tile2,-1,0);
        placeHandler.playTileTest(Hive.Player.WHITE,tile3,0,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile4,-1,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile5,-2,1);
        placeHandler.playTileTest(Hive.Player.WHITE,tile6,-2,2);
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(-2,2);
        assertTrue(genericMoveHandler.checkWhileSlidingKeepContact(-2,2,-1,2));
    }


    @Test
    void testValidSlideAnt() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setCreature(Hive.Tile.SOLDIER_ANT);
        tile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(tile,0,0);
        placeHandler.naivePlayTile(tile,-1,1);
        placeHandler.naivePlayTile(tile,-1,2);
        placeHandler.naivePlayTile(tile,-1,3);
        placeHandler.naivePlayTile(tile,0,2);
        HashMap<String, Integer> goal = new HashMap<>();
        goal.put("q",1);
        goal.put("r",1);
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths = genericMoveHandler.findPathToLocation(-1,3, tile.getPlayedByPlayer(),tile.getCreature(),null,goal,0,100);
        assertEquals(3,validPaths.size());
    }

    @Test
    void testInvalidSlideAnt() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setCreature(Hive.Tile.SOLDIER_ANT);
        tile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(tile,0,0);
        placeHandler.naivePlayTile(tile,-1,1);
        placeHandler.naivePlayTile(tile,-1,2);
        placeHandler.naivePlayTile(tile,-1,3);
        placeHandler.naivePlayTile(tile,0,2);
        placeHandler.naivePlayTile(tile,1,1);
        HashMap<String, Integer> goal = new HashMap<>();
        goal.put("q",-1);
        goal.put("r",2);
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths = genericMoveHandler.findPathToLocation(-1,3, tile.getPlayedByPlayer(),tile.getCreature(),null,goal,0,100);
        assertEquals(0,validPaths.size());
    }

    @Test
    void testValidSlideSpider() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setCreature(Hive.Tile.SPIDER);
        tile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(tile,-1,-1);
        placeHandler.naivePlayTile(tile,-2,0);
        placeHandler.naivePlayTile(tile,-3,1);
        placeHandler.naivePlayTile(tile,-3,2);
        placeHandler.naivePlayTile(tile,-2,2);
        placeHandler.naivePlayTile(tile,-1,2);
        placeHandler.naivePlayTile(tile,-0,1);
        placeHandler.naivePlayTile(tile,1,0);
        placeHandler.naivePlayTile(tile,1,-1);
        placeHandler.naivePlayTile(tile,1,-2);
        HashMap<String, Integer> goal = new HashMap<>();
        goal.put("q",-2);
        goal.put("r",1);
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths = genericMoveHandler.findPathToLocation(1,-2, tile.getPlayedByPlayer(),tile.getCreature(),null,goal,0,3);
        assertEquals(1,validPaths.size());
    }

    @Test
    void testInvalidSlideSpider() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Tile tile = new Tile();
        tile.setCreature(Hive.Tile.SPIDER);
        tile.setPlayedByPlayer(Hive.Player.WHITE);

        placeHandler.naivePlayTile(tile,-1,-1);
        placeHandler.naivePlayTile(tile,-2,0);
        placeHandler.naivePlayTile(tile,-3,1);
        placeHandler.naivePlayTile(tile,-3,2);
        placeHandler.naivePlayTile(tile,-2,2);
        placeHandler.naivePlayTile(tile,-1,2);
        placeHandler.naivePlayTile(tile,-0,1);
        placeHandler.naivePlayTile(tile,1,0);
        placeHandler.naivePlayTile(tile,1,-1);
        placeHandler.naivePlayTile(tile,1,-2);
        HashMap<String, Integer> goal = new HashMap<>();
        goal.put("q",-3);
        goal.put("r",0);
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths = genericMoveHandler.findPathToLocation(1,-2, tile.getPlayedByPlayer(),tile.getCreature(),null,goal,0,3);
        assertEquals(0,validPaths.size());
    }
}
