package handlers;
import creatures.Tile;
import game.Hive;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class GenericMoveHandlerTest {

    @Test
    public void testValidateQueenBeeHasBeenPlaced() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler(Hive.Player.BLACK);
        assertTrue(genericMoveHandler.validateQueenBee());
    }

    @Test
    public void testValidateTileHasAnyContact() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler(Hive.Player.WHITE);
        assertTrue(genericMoveHandler.validateContact());
    }

    @Test
    public void testValidateNoIslandsExists() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler(Hive.Player.BLACK);
        assertTrue(genericMoveHandler.validateNoIslands());
    }

    @Test
    public void testIfCanMakeValidMove() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler(Hive.Player.WHITE);
        genericMoveHandler.canMakeMove(new Tile(),0,0,1,-1);
    }
}
