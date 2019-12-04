package handlers;

import game.Board;
import game.Hive;
import game.HiveGame;

import java.util.HashMap;

public class BeetleMoveHandler implements CreatureMoveHandler {
    private HiveGame game;
    private GenericMoveHandler moveHandler;

    public BeetleMoveHandler() {}

    public void setGame(HiveGame game) {
        this.game = game;
    }

    public void setMoveHandler(GenericMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }

    @Override
    public boolean validatePathSize(int depth) {
        return depth==1; //Beetle can move 1 space
    }

    @Override
    public boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player) {
        return true;
    }
}
