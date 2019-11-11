package handlers;

import game.Board;
import game.Hive;

import java.util.HashMap;

public class BeetleMoveHandler implements CreatureMoveHandler {
    private static BeetleMoveHandler instance;

    public static BeetleMoveHandler getInstance() {
        if (instance == null) {
            instance = new BeetleMoveHandler();
        }
        return instance;
    }

    private BeetleMoveHandler() { }

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
