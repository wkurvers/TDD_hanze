package handlers;

import creatures.Tile;
import game.Board;

import java.util.ArrayList;

public class SpiderMoveHandler implements CreatureMoveHandler {
    private static SpiderMoveHandler instance;

    public static SpiderMoveHandler getInstance() {
        if (instance == null) {
            instance = new SpiderMoveHandler();
        }
        return instance;
    }

    private SpiderMoveHandler() { }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        boolean hasContact = hasContact(toQ,toR);
        boolean isEmptySpot = isEmptySpot(toQ,toR);
        Board.getBoardInstance().placeTileAtPosition(fromQ,fromR,tile);
        return hasContact && isEmptySpot;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return depth == 3; //Spider can move 3 spaces
    }

    private boolean hasContact(int toQ, int toR) {
        Board gameBoard = Board.getBoardInstance();
        ArrayList<Tile> neighbours = gameBoard.getNeighbours(toQ, toR);
        for (Tile tile : neighbours) {
            if (tile != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptySpot(int toQ, int toR) {
        return Board.getBoardInstance().getSizeAtPosition(toQ,toR) == 0;
    }
}
