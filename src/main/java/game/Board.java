package game;

import creatures.Tile;

import java.util.Stack;

public class Board {
    private Stack<Tile>[][] board = new Stack [][] {};

    private Stack[][] getBoard() {
        return board;
    }

    public Stack<Tile> getPosition(int q, int r) {
        return board[q][r];
    }

    public Tile getTopTileAtPosition(int q, int r) {
        return board[q][r].pop();
    }

    public Stack<Tile> getNeighbours(int q, int r) {
        return null;
    }

    public void placeTileAtPosition(int q, int r, Tile tile) throws Hive.IllegalMove {
        /*
            Do all the tests
         */
        if (false) {
            throw new Hive.IllegalMove("Cannot place the Tile");
        } else {
            board[q][r].push(tile);
        }
    }
}
