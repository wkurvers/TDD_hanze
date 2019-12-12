package handlers;

import game.Board;
import game.HiveGame;
import nl.hanze.hive.Hive;

import java.util.HashMap;

public class GrasshopperMoveHandler implements CreatureMoveHandler {
    private HiveGame game;
    private GenericMoveHandler moveHandler;

    public GrasshopperMoveHandler() {

    }

    public void setGame(HiveGame game) {
        this.game = game;
    }

    public void setMoveHandler(GenericMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        return false;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return !isJumpOne(fromQ, fromR, toQ, toR) && isStraightLine(fromQ, fromR, toQ, toR) && isEmptySpot(toQ, toR) && areBetweenLocationsFilled(fromQ, fromR, toQ, toR);
    }

    public boolean isJumpOne(int fromQ, int fromR, int toQ, int toR) {
        HashMap<String, int[]> neighbours = game.getCurrentBoard().getNeighbouringCoordinates(fromQ,fromR);
        for(String direction : neighbours.keySet()) {
            int[] coordinate = neighbours.get(direction);
            if(coordinate[0] == toQ && coordinate[1] == toR) {
                return true;
            }
        }
        return false;
    }

    public boolean isStraightLine(int fromQ, int fromR, int toQ, int toR) {
        int fromTotal = fromQ + fromR;
        int toTotal = toQ + toR;
        return (fromQ == toQ || fromR == toR || fromTotal == toTotal);
    }

    @Override
    public boolean validatePathSize(int depth) {
        return true; //grasshopper has no sliding movement
    }

    private boolean isEmptySpot(int toQ, int toR) {
        return this.game.getCurrentBoard().getSizeAtPosition(toQ,toR) == 0;
    }

    public boolean areBetweenLocationsFilled(int fromQ, int fromR, int toQ, int toR) {
        int fromTotal = fromQ + fromR;
        int toTotal = toQ + toR;
        if(fromQ == toQ) {
            return checkLocations("diagLeft", fromQ,fromR,toQ,toR);
        }
        if(fromR == toR) {
            return checkLocations("horizontal", fromQ,fromR,toQ,toR);
        }
        if(fromTotal == toTotal) {
            return checkLocations("diagRight", fromQ,fromR,toQ,toR);
        }
        return false;
    }

    private boolean checkLocations(String direction, int fromQ, int fromR, int toQ, int toR) {
        switch(direction) {
            case "horizontal":
                if(toQ > fromQ) {
                    for(int i=fromQ+1; i < toQ; i++){
                        if(this.game.getCurrentBoard().getSizeAtPosition(i,fromR) == 0) {
                            return false;
                        }
                    }
                } else {
                    for(int i=fromQ-1; i > toQ; i--){
                        if(this.game.getCurrentBoard().getSizeAtPosition(i,fromR) == 0) {
                            return false;
                        }
                    }
                }
                return true;
            case "diagLeft":
                if(toR > fromR) {
                    for(int i=fromR+1; i < toR; i++){
                        if(this.game.getCurrentBoard().getSizeAtPosition(fromQ,i) == 0) {
                            return false;
                        }
                    }
                } else {
                    for(int i=fromR-1; i > toR; i--){
                        if(this.game.getCurrentBoard().getSizeAtPosition(fromQ,i) == 0) {
                            return false;
                        }
                    }
                }
                return true;
            case "diagRight":
                if(toQ > fromQ) {
                    int r = fromR;
                    for(int i = fromQ+1; i < toQ; i++) {
                        r -= 1;
                        if(this.game.getCurrentBoard().getSizeAtPosition(i,r) == 0) {
                            return false;
                        }
                    }
                } else {
                    int r = fromR;
                    for(int i = fromQ-1; i > toQ; i--) {
                        r += 1;
                        if(this.game.getCurrentBoard().getSizeAtPosition(i,r) == 0) {
                            return false;
                        }
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
