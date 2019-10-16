package handlers;

public class AntMoveHandler implements CreatureMoveHandler {
    private static AntMoveHandler instance;

    public static AntMoveHandler getInstance() {
        if (instance == null) {
            instance = new AntMoveHandler();
        }
        return instance;
    }

    private AntMoveHandler() { }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
