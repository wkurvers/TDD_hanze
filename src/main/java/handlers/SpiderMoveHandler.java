package handlers;

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
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
