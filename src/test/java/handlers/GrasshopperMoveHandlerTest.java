package handlers;

import creatures.Grasshopper;
import creatures.Tile;
import game.Board;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class GrasshopperMoveHandlerTest {
    private HiveGame game;
    @BeforeEach
    void resetHandler() {
        game = new HiveGame();
    }
    @Test
    void testIsStraightLineDiagonalLeft() {
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().isStraightLine(0,0,0,-3));
    }

    @Test
    void testIsStraightLineDiagonalRight() {
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().isStraightLine(0,0,3,-3));
    }

    @Test
    void testIsStraightLineHorizontal() {
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().isStraightLine(0,0,3,0));
    }

    @Test
    void testIsNotStraightLine() {
        assertFalse(game.getGenericMoveHandler().getGrasshopperMoveHandler().isStraightLine(0,0,-2,1));
    }

    @Test
    void testJumpIsMoreThenOne() {
        assertFalse(game.getGenericMoveHandler().getGrasshopperMoveHandler().isJumpOne(0,0,3,0));
    }

    @Test
    void testJumpIsOne() {
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().isJumpOne(0,0,0,-1));
    }

    @Test
    void testLocationsFilledDiagonalLeft() {
        game.getCurrentBoard().placeTileAtPosition(0,-1,new Tile());
        game.getCurrentBoard().placeTileAtPosition(0,-2,new Tile());
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().areBetweenLocationsFilled(0,0,0,-3));
    }

    @Test
    void testLocationsFilledDiagonalRight() {
        game.getCurrentBoard().placeTileAtPosition(1,-1,new Tile());
        game.getCurrentBoard().placeTileAtPosition(2,-2,new Tile());
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().areBetweenLocationsFilled(0,0,3,-3));
    }

    @Test
    void testLocationsFilledHorizontal() {
        game.getCurrentBoard().placeTileAtPosition(1,0,new Tile());
        game.getCurrentBoard().placeTileAtPosition(2,0,new Tile());
        assertTrue(game.getGenericMoveHandler().getGrasshopperMoveHandler().areBetweenLocationsFilled(0,0,3,0));
    }

    @Test
    void testJumpMoreThenOne() {
        assertFalse(game.getGenericMoveHandler().getGrasshopperMoveHandler().isJumpOne(-1,-1,-1,2));
    }
}
