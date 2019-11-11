package handlers;

import game.Board;
import game.Hive;

import java.util.HashMap;

public class QueenBeeMoveHandler implements CreatureMoveHandler {
    private static QueenBeeMoveHandler instance;

    public static QueenBeeMoveHandler getInstance() {
        if (instance == null) {
            instance = new QueenBeeMoveHandler();
        }
        return instance;
    }

    private QueenBeeMoveHandler() { }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return Board.getBoardInstance().getSizeAtPosition(toQ,toR) == 0;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return depth==1; //Queen can move 1 space
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
