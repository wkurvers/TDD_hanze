package handlers;

import creatures.Tile;
import game.Board;
import game.Hive;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;

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
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        ArrayList<int[]> allPossibleMoves = calculatePossibleLocations(fromQ,fromR);
        int exceptionCount = 0;
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths;
        for(int[] location: allPossibleMoves) {
            try{
                HashMap<String, Integer> goal = new HashMap<>();
                goal.put("q",location[0]);
                goal.put("r",location[1]);
                validPaths = GenericMoveHandler.getGenericMoveHandler().findPathToLocation(fromQ,fromR,player, Hive.Tile.SPIDER,null,goal,0,3);
                GenericMoveHandler.getGenericMoveHandler().tryMakeSlidingMove(validPaths,player);
                Tile tile = Board.getBoardInstance().removeTopTileAtPosition(location[0],location[1]);
                Board.getBoardInstance().placeTileAtPosition(fromQ,fromR,tile);
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
