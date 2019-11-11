package handlers;

import creatures.Grasshopper;
import creatures.Tile;
import game.Board;
import game.HiveGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class GrasshopperMoveHandlerTest {
    @AfterEach
    void resetHandler() {
        HiveGame.getGame().resetGame();
    }
    @Test
    void testIsStraightLineDiagonalLeft() {
        assertTrue(GrasshopperMoveHandler.getInstance().isStraightLine(0,0,0,-3));
    }

    @Test
    void testIsStraightLineDiagonalRight() {
        assertTrue(GrasshopperMoveHandler.getInstance().isStraightLine(0,0,3,-3));
    }

    @Test
    void testIsStraightLineHorizontal() {
        assertTrue(GrasshopperMoveHandler.getInstance().isStraightLine(0,0,3,0));
    }

    @Test
    void testIsNotStraightLine() {
        assertFalse(GrasshopperMoveHandler.getInstance().isStraightLine(0,0,-2,1));
    }

    @Test
    void testJumpIsMoreThenOne() {
        assertFalse(GrasshopperMoveHandler.getInstance().isJumpOne(0,0,3,0));
    }

    @Test
    void testJumpIsOne() {
        assertTrue(GrasshopperMoveHandler.getInstance().isJumpOne(0,0,0,-1));
    }

    @Test
    void testLocationsFilledDiagonalLeft() {
        Board.getBoardInstance().placeTileAtPosition(0,-1,new Tile());
        Board.getBoardInstance().placeTileAtPosition(0,-2,new Tile());
        assertTrue(GrasshopperMoveHandler.getInstance().areBetweenLocationsFilled(0,0,0,-3));
    }

    @Test
    void testLocationsFilledDiagonalRight() {
        Board.getBoardInstance().placeTileAtPosition(1,-1,new Tile());
        Board.getBoardInstance().placeTileAtPosition(2,-2,new Tile());
        assertTrue(GrasshopperMoveHandler.getInstance().areBetweenLocationsFilled(0,0,3,-3));
    }

    @Test
    void testLocationsFilledHorizontal() {
        Board.getBoardInstance().placeTileAtPosition(1,0,new Tile());
        Board.getBoardInstance().placeTileAtPosition(2,0,new Tile());
        assertTrue(GrasshopperMoveHandler.getInstance().areBetweenLocationsFilled(0,0,3,0));
    }
}
