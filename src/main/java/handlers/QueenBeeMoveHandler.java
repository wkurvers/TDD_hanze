package handlers;

public class QueenBeeMoveHandler implements CreatureMoveHandler {
    private static QueenBeeMoveHandler instance;

    public static QueenBeeMoveHandler getInstance() {
        if (instance == null) {
            instance = new QueenBeeMoveHandler();
        }
        return instance;
    }

    private QueenBeeMoveHandler() { }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
