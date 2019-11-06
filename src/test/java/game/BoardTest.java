package game;

import creatures.Tile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @AfterEach
    void resetBoard() {
        Board.getBoardInstance().resetBoard();
    }

    @Test
    void testGetEmptyLocation() {
        Board gameBoard = Board.getBoardInstance();

        assertNull(gameBoard.getPosition(0,0));
    }

    @Test
    void testGetFilledLocation() throws ArrayIndexOutOfBoundsException {
        Board gameBoard = Board.getBoardInstance();
        gameBoard.placeTileAtPosition(0,0, new Tile());
        //assertEquals(gameBoard.getPosition(0,0).getClass().getName(),"java.util.Stack");
    }

    @Test
    void testGetTileAtLocation(){
        Board gameBoard = Board.getBoardInstance();
        Tile newTile = new Tile();
        gameBoard.placeTileAtPosition(0,0,newTile);
        assertEquals(gameBoard.getTopTileAtPosition(0,0),newTile);
    }

    @Test
    void testPlaceTileAtLocation(){
        Board gameBoard = Board.getBoardInstance();
        Tile newTile = new Tile();
        gameBoard.placeTileAtPosition(-3,1,newTile);
        assertEquals(gameBoard.getTopTileAtPosition(-3,1),newTile);
    }

    private ArrayList setupBoardForTestGetNeighboursForLocation(){
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
    void testGetNeighboursForLocation() {
        ArrayList dataList = setupBoardForTestGetNeighboursForLocation();
        Board gameBoard = (Board) dataList.get(0);
        ArrayList<Tile> neighboursList = (ArrayList<Tile>) dataList.get(1);

        ArrayList<Tile> resultNeighboursList = gameBoard.getNeighbours(0,0);
        for(int index = 0; index<6; index++) {
            assertEquals(neighboursList.get(index),resultNeighboursList.get(index));
        }
    }

    @Test
    void testGetNeighbouringCoordinatesForSpecificLocation() {
        HashMap<String, int[]> coordinates = Board.getBoardInstance().getNeighbouringCoordinates(0,0);
        for(String direction: coordinates.keySet()) {
            int[] coordinate = coordinates.get(direction);
            switch(direction) {
                case "topLeft":
                    assertTrue(coordinate[0] == 0 && coordinate[1] == -1,"Direction: topLeft is wrong");
                    break;
                case "topRight":
                    assertTrue(coordinate[0] == 1 && coordinate[1] == -1,"Direction: topRight is wrong");
                    break;
                case "right":
                    assertTrue(coordinate[0] == 1 && coordinate[1] == 0,"Direction: right is wrong");
                    break;
                case "bottomRight":
                    assertTrue(coordinate[0] == 0 && coordinate[1] == 1,"Direction: bottomRight is wrong");
                    break;
                case "bottomLeft":
                    assertTrue(coordinate[0] == -1 && coordinate[1] == 1,"Direction: bottomLeft is wrong");
                    break;
                case "left":
                    assertTrue(coordinate[0] == -1 && coordinate[1] == 0,"Direction: Left is wrong");
                    break;
                default:
                    fail("Direction is not know");
            }
        }
    }

    @Test
    void testGetNeighbouringPositionsSizeIsCorrect() {
        Board gameBoard = Board.getBoardInstance();
        Tile newTile = new Tile();

        gameBoard.placeTileAtPosition(1,-1,newTile);
        gameBoard.placeTileAtPosition(1,-1,newTile);

        ArrayList<Integer> neighboursSizeList = gameBoard.getNeighbouringPositionsSize(0,0);
        assertEquals(neighboursSizeList.get(1), 2);
    }
}