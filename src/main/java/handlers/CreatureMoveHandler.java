package handlers;

import game.Hive;

public interface CreatureMoveHandler {
    boolean isValidMove(int fromQ, int fromR, int toQ, int toR);

    boolean validatePathSize(int depth);

    boolean canMakeAnyMove(int fromQ, int fromR, Hive.Player player);
}
