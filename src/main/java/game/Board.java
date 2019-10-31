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

    public int getStackSizeAtPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r).size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public Tile getTopTileAtPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r).peek();
        } catch(NullPointerException|EmptyStackException ex) {
            return null;
        }
    }

    public Tile removeTopTileAtPosition(int q, int r) {
        try {
            Tile tile = gameBoard.get(q).get(r).pop();
            if (gameBoard.get(q).get(r).isEmpty()) {
                gameBoard.get(q).put(r,null);
            }
            return tile;
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

    public ArrayList<Stack<Tile>> getNeighbouringPositions(int q, int r) {
        ArrayList<Stack<Tile>> neighboursList = new ArrayList<>();
        peekAtPosition(q,r-1);
        Stack<Tile> topLeft = gameBoard.get(q).get(r-1);
        neighboursList.add(topLeft);

        peekAtPosition(q+1,r-1);
        Stack<Tile> topRight = gameBoard.get(q+1).get(r-1);
        neighboursList.add(topRight);

        peekAtPosition(q+1,r);
        Stack<Tile> right = gameBoard.get(q+1).get(r);
        neighboursList.add(right);

        peekAtPosition(q,r+1);
        Stack<Tile> bottomRight = gameBoard.get(q).get(r+1);
        neighboursList.add(bottomRight);

        peekAtPosition(q-1,r+1);
        Stack<Tile> bottomLeft = gameBoard.get(q-1).get(r+1);
        neighboursList.add(bottomLeft);

        peekAtPosition(q-1,r);
        Stack<Tile> left = gameBoard.get(q-1).get(r);
        neighboursList.add(left);
        return neighboursList;
    }

    public void peekAtPosition(int q, int r) {
        if (gameBoard.get(q) == null) {
            gameBoard.put(q,new HashMap<Integer, Stack<Tile>>());
            gameBoard.get(q).put(r, new Stack<Tile>());
        } else if (gameBoard.get(q).get(r) == null) {
            gameBoard.get(q).put(r, new Stack<Tile>());
        }
    }

    public void placeTileAtPosition(int q, int r, Tile tile){
        peekAtPosition(q,r);
        gameBoard.get(q).get(r).push(tile);
    }
}
