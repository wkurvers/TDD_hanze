package handlers;

import game.Board;

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
}
