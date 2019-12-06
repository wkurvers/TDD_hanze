package handlers;

import creatures.Tile;
import game.HiveGame;
import nl.hanze.hive.Hive;

import java.util.ArrayList;
import java.util.HashMap;

public class SpiderMoveHandler implements CreatureMoveHandler {
    private HiveGame game;
    private GenericMoveHandler moveHandler;

    public SpiderMoveHandler() {}

    public void setGame(HiveGame game) {
        this.game = game;
    }

    public void setMoveHandler(GenericMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        ArrayList<int[]> allPossibleMoves = calculatePossibleLocations(fromQ,fromR);
        int exceptionCount = 0;
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths;
        for(int[] location: allPossibleMoves) {
            try{
                HashMap<String, Integer> goal = new HashMap<>();
                goal.put("q",location[0]);
                goal.put("r",location[1]);
                validPaths = this.moveHandler.findPathToLocation(fromQ,fromR,player, Hive.Tile.SPIDER,null,goal,0,3);
                this.moveHandler.tryMakeSlidingMove(validPaths,player);
                Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(location[0],location[1]);
                this.game.getCurrentBoard().placeTileAtPosition(fromQ,fromR,tile);
            } catch (Hive.IllegalMove ex) {
                exceptionCount++;
            }
        }
        return exceptionCount < allPossibleMoves.size();
    }

    public ArrayList<int[]> calculatePossibleLocations(int fromQ, int fromR) {
        ArrayList<int[]> possibleLocations = new ArrayList<>();
        for(int q = fromQ-3; q <= fromQ+3; q++) {
            for(int r = fromR-3; r <= fromR+3; r++) {
                int[] location = new int[2];
                location[0] = q;
                location[1] = r;
                possibleLocations.add(location);
            }
        }
        return possibleLocations;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        boolean hasContact = hasContact(toQ,toR);
        boolean isEmptySpot = isEmptySpot(toQ,toR);
        this.game.getCurrentBoard().placeTileAtPosition(fromQ,fromR,tile);
        return hasContact && isEmptySpot;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return depth == 3; //Spider can move 3 spaces
    }

    private boolean hasContact(int toQ, int toR) {
        ArrayList<Tile> neighbours = this.game.getCurrentBoard().getNeighbours(toQ, toR);
        for (Tile tile : neighbours) {
            if (tile != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptySpot(int toQ, int toR) {
        return this.game.getCurrentBoard().getSizeAtPosition(toQ,toR) == 0;
    }
}
