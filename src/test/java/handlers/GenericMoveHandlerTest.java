package handlers;
import creatures.Tile;
import game.Board;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class GenericMoveHandlerTest {

    @AfterEach
    void resetHandler() {
        HiveGame.getGame().resetGame();
    }


    @Test
    void testValidateTileHasAnyContact() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        assertTrue(genericMoveHandler.validateContact());
    }

    @Test
    void testValidateNoIslandsExists() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        assertTrue(genericMoveHandler.validateNoIslands());
    }

    @Test
    void testIfCanMakeValidMove() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        genericMoveHandler.canMakeMove(new Tile(),0,0,1,-1);
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
    void testDfsTileCountSearch() {
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
}
