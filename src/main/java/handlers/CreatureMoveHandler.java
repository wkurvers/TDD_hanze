package handlers;

public interface CreatureMoveHandler {
    boolean isValidMove(int fromQ, int fromR, int toQ, int toR);

    boolean validatePathSize(int depth);
}
