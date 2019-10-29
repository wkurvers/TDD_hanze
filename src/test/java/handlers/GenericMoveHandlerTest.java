package handlers;
import creatures.Tile;
import game.Hive;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class GenericMoveHandlerTest {

    @AfterEach
    void resetHandler() {
        GenericMoveHandler.getGenericMoveHandler().resetMoveHandler();
    }


    @Test
    public void testValidateTileHasAnyContact() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        assertTrue(genericMoveHandler.validateContact());
    }

    @Test
    public void testValidateNoIslandsExists() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        assertTrue(genericMoveHandler.validateNoIslands());
    }

    @Test
    public void testIfCanMakeValidMove() {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        genericMoveHandler.canMakeMove(new Tile(),0,0,1,-1);
    }
}
