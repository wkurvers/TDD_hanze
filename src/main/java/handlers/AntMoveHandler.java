package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;

import java.util.ArrayList;
import java.util.HashMap;

public class AntMoveHandler implements CreatureMoveHandler {
    private static AntMoveHandler instance;

    public static AntMoveHandler getInstance() {
        if (instance == null) {
            instance = new AntMoveHandler();
        }
        return instance;
    }

    private AntMoveHandler() { }

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
        return true; //Ant can move infinite amount of spaces
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

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        HashMap<String, int[]> coordinates = Board.getBoardInstance().getNeighbouringCoordinates(fromQ,fromR);
        for(String directions: coordinates.keySet()) {
            int[] coordinate = coordinates.get(directions);
            if(Board.getBoardInstance().getSizeAtPosition(coordinate[0],coordinate[1]) == 0) {
                if(GenericMoveHandler.getGenericMoveHandler().canSlideTile(fromQ,fromR,coordinate[0],coordinate[1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
