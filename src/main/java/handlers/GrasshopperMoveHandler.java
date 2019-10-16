package handlers;

public class GrasshopperMoveHandler implements CreatureMoveHandler {
    private static GrasshopperMoveHandler instance;

    public static GrasshopperMoveHandler getInstance() {
        if (instance == null) {
            instance = new GrasshopperMoveHandler();
        }
        return instance;
    }

    private GrasshopperMoveHandler() { }

    @Override
    public boolean isValidMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
