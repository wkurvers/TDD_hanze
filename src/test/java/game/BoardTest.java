package game;

import creatures.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testGetBoardReturnsBoard() {
        Board gameBoard = Board.getBoardInstance();
        assertEquals(gameBoard.getBoard().getClass().getName(),"[[Ljava.util.Stack;");
    }

    @Test
    void testGetEmptyLocation() {
        Board gameBoard = Board.getBoardInstance();
        assertNull(gameBoard.getPosition(0,0));
    }

    @Test
    void testGetFilledLocation() throws Hive.IllegalMove, ArrayIndexOutOfBoundsException {
        Board gameBoard = Board.getBoardInstance();
        gameBoard.placeTileAtPosition(0,0, new Tile());
        assertEquals(gameBoard.getPosition(0,0).getClass().getName(),"java.util.Stack");
    }

    @Test
    void testGetTileAtLocation() throws Hive.IllegalMove {
        Board gameBoard = Board.getBoardInstance();
        Tile newTile = new Tile();
        gameBoard.placeTileAtPosition(0,0,newTile);
        assertEquals(gameBoard.getTopTileAtPosition(0,0),newTile);
    }

    @Test
    void testPlaceTileAtLocation() throws Hive.IllegalMove {
        Board gameBoard = Board.getBoardInstance();
        Tile newTile = new Tile();
        gameBoard.placeTileAtPosition(-3,1,newTile);
        assertEquals(gameBoard.getTopTileAtPosition(-3,1),newTile);
    }

    private ArrayList setupBoardForTestGetNeighboursForLocation() throws Hive.IllegalMove {
        Board gameBoard = Board.getBoardInstance();

        Tile topLeftTile = new Tile();
        Tile topRightTile = new Tile();
        Tile rightTile = new Tile();
        Tile bottomRightTile = new Tile();
        Tile bottomLeftTile = new Tile();
        Tile leftTile = new Tile();

        ArrayList<Tile> neighboursList = new ArrayList<>();
        neighboursList.add(topLeftTile);
        neighboursList.add(topRightTile);
        neighboursList.add(rightTile);
        neighboursList.add(bottomRightTile);
        neighboursList.add(bottomLeftTile);
        neighboursList.add(leftTile);

        gameBoard.placeTileAtPosition(0,-1,topLeftTile);
        gameBoard.placeTileAtPosition(1,-1,topRightTile);
        gameBoard.placeTileAtPosition(1,0,rightTile);
        gameBoard.placeTileAtPosition(0,1,bottomRightTile);
        gameBoard.placeTileAtPosition(-1,1,bottomLeftTile);
        gameBoard.placeTileAtPosition(-1,0,leftTile);

        ArrayList returnDataList = new ArrayList();
        returnDataList.add(gameBoard);
        returnDataList.add(neighboursList);
        return returnDataList;
    }
    @Test
    void testGetNeighboursForLocation() throws Hive.IllegalMove {
        ArrayList dataList = setupBoardForTestGetNeighboursForLocation();
        Board gameBoard = (Board) dataList.get(0);
        ArrayList<Tile> neighboursList = (ArrayList<Tile>) dataList.get(1);

        ArrayList<Tile> resultNeighboursList = gameBoard.getNeighbours(0,0);
        for(int index = 0; index<6; index++) {
            assertEquals(neighboursList.get(index),resultNeighboursList.get(index));
        }
    }
}