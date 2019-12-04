package handlers;

import game.Board;
import game.Hive;
import game.HiveGame;

import java.util.HashMap;

public class QueenBeeMoveHandler implements CreatureMoveHandler {
    private HiveGame game;
    private GenericMoveHandler moveHandler;

    public QueenBeeMoveHandler() {

    }

    public void setGame(HiveGame game) {
        this.game = game;
    }

    public void setMoveHandler(GenericMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return this.game.getCurrentBoard().getSizeAtPosition(toQ,toR) == 0;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return depth==1; //Queen can move 1 space
    }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        HashMap<String, int[]> coordinates = this.game.getCurrentBoard().getNeighbouringCoordinates(fromQ,fromR);
        for(String directions: coordinates.keySet()) {
            int[] coordinate = coordinates.get(directions);
            if(this.game.getCurrentBoard().getSizeAtPosition(coordinate[0],coordinate[1]) == 0) {
                if(this.moveHandler.canSlideTile(fromQ,fromR,coordinate[0],coordinate[1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
