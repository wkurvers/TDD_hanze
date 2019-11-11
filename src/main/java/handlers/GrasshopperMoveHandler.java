package handlers;

import game.Board;
import game.Hive;

public class GrasshopperMoveHandler implements CreatureMoveHandler {
    private static GrasshopperMoveHandler instance;

    public static GrasshopperMoveHandler getInstance() {
        if (instance == null) {
            instance = new GrasshopperMoveHandler();
        }
        return instance;
    }

    private GrasshopperMoveHandler() { }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        return false;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return !isJumpOne(fromQ, fromR, toQ, toR) && isStraightLine(fromQ, fromR, toQ, toR) && isEmptySpot(toQ, toR) && areBetweenLocationsFilled(fromQ, fromR, toQ, toR);
    }

    public boolean isJumpOne(int fromQ, int fromR, int toQ, int toR) {
        return Math.abs(Math.abs(fromQ)-Math.abs(toQ)) <= 1 && Math.abs(Math.abs(fromR)-Math.abs(toR)) <= 1;
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
        return Board.getBoardInstance().getSizeAtPosition(toQ,toR) == 0;
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
                        if(Board.getBoardInstance().getSizeAtPosition(i,fromR) == 0) {
                            return false;
                        }
                    }
                } else {
                    for(int i=fromQ-1; i > toQ; i--){
                        if(Board.getBoardInstance().getSizeAtPosition(i,fromR) == 0) {
                            return false;
                        }
                    }
                }
                return true;
            case "diagLeft":
                if(toR > fromR) {
                    for(int i=fromR+1; i < toR; i++){
                        if(Board.getBoardInstance().getSizeAtPosition(fromQ,i) == 0) {
                            return false;
                        }
                    }
                } else {
                    for(int i=fromR-1; i > toR; i--){
                        if(Board.getBoardInstance().getSizeAtPosition(fromQ,i) == 0) {
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
                        if(Board.getBoardInstance().getSizeAtPosition(i,r) == 0) {
                            return false;
                        }
                    }
                } else {
                    int r = fromR;
                    for(int i = fromQ-1; i > toQ; i--) {
                        r += 1;
                        if(Board.getBoardInstance().getSizeAtPosition(i,r) == 0) {
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
