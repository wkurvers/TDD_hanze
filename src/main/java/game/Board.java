package game;

import creatures.Tile;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class Board {
    private HashMap<Integer, HashMap<Integer, Stack<Tile>>> gameBoard = new HashMap<>();
    private static Board instance;

    public static Board getBoardInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public void resetBoard() {
        this.gameBoard = new HashMap<>();
    }

    private Board() {}

    public HashMap<Integer, HashMap<Integer, Stack<Tile>>> getBoard() {
        return gameBoard;
    }

    public Stack<Tile> getPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public Tile getTopTileAtPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r).peek();
        } catch(NullPointerException|EmptyStackException ex) {
            return null;
        }
    }

    public ArrayList<Tile> getNeighbours(int q, int r) {
        ArrayList<Tile> neighboursList = new ArrayList<>();
        Tile topLeftTile = getTopTileAtPosition(q,r-1);
        neighboursList.add(topLeftTile);

        Tile topRightTile = getTopTileAtPosition(q+1,r-1);
        neighboursList.add(topRightTile);

        Tile rightTile = getTopTileAtPosition(q+1,r);
        neighboursList.add(rightTile);

        Tile bottomRightTile = getTopTileAtPosition(q,r+1);
        neighboursList.add(bottomRightTile);

        Tile bottomLeftTile = getTopTileAtPosition(q-1,r+1);
        neighboursList.add(bottomLeftTile);

        Tile leftTile = getTopTileAtPosition(q-1,r);
        neighboursList.add(leftTile);

        return neighboursList;
    }

    public void placeTileAtPosition(int q, int r, Tile tile){
        if (gameBoard.get(q) == null) {
            gameBoard.put(q,new HashMap<Integer, Stack<Tile>>());
            gameBoard.get(q).put(r, new Stack<Tile>());
        } else if (gameBoard.get(q).get(r) == null) {
            gameBoard.get(q).put(r, new Stack<Tile>());
        }
        gameBoard.get(q).get(r).push(tile);
    }
}
