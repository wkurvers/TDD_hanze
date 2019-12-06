package handlers;

import creatures.Tile;
import game.HiveGame;
import nl.hanze.hive.Hive;

import java.util.ArrayList;
import java.util.HashMap;

public class AntMoveHandler implements CreatureMoveHandler {
    private HiveGame game;
    private GenericMoveHandler moveHandler;
    public AntMoveHandler() {

    }

    public void setGame(HiveGame game) {
        this.game = game;
    }

    public void setMoveHandler(GenericMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        boolean hasContact = hasContact(toQ,toR);
        boolean isEmptySpot = isEmptySpot(toQ,toR);
        game.getCurrentBoard().placeTileAtPosition(fromQ,fromR,tile);
        return hasContact && isEmptySpot;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return true; //Ant can move infinite amount of spaces
    }

    private boolean hasContact(int toQ, int toR) {
        ArrayList<Tile> neighbours = game.getCurrentBoard().getNeighbours(toQ, toR);
        for (Tile tile : neighbours) {
            if (tile != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptySpot(int toQ, int toR) {
        return game.getCurrentBoard().getSizeAtPosition(toQ,toR) == 0;
    }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        HashMap<String, int[]> coordinates = game.getCurrentBoard().getNeighbouringCoordinates(fromQ,fromR);
        for(String directions: coordinates.keySet()) {
            int[] coordinate = coordinates.get(directions);
            if(game.getCurrentBoard().getSizeAtPosition(coordinate[0],coordinate[1]) == 0) {
                if(moveHandler.canSlideTile(fromQ,fromR,coordinate[0],coordinate[1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
