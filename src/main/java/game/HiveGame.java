package game;

import creatures.Tile;
import handlers.GenericMoveHandler;
import handlers.PlaceHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class HiveGame implements Hive {
    private Player currentPlayer;
    private Player opponent;
    private static HiveGame instance;


    public static HiveGame getGame(){
        if (instance == null) {
            instance = new HiveGame();
        }
        return instance;
    }

    public void resetGame() {
        instance = null;
        PlaceHandler.getPlaceHandler().reset();
        GenericMoveHandler.getGenericMoveHandler().resetMoveHandler();
        Board.getBoardInstance().resetBoard();
    }
    private HiveGame() {
        currentPlayer = Player.WHITE;
        opponent = Player.BLACK;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        if (currentPlayer == Player.WHITE) {
            currentPlayer = Player.BLACK;
            opponent = Player.WHITE;
        } else if(currentPlayer == Player.BLACK) {
            currentPlayer = Player.WHITE;
            opponent = Player.BLACK;
        }
    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        creatures.Tile tileToPlace = TileFactory.makeTile(tile,currentPlayer);
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        placeHandler.playTile(tileToPlace,q,r);
        switchPlayer();
    }

    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {

        switchPlayer();
    }

    @Override
    public void pass() throws IllegalMove {

    }

    @Override
    public boolean isWinner(Player player) {
        PlaceHandler placeHandler = PlaceHandler.getPlaceHandler();
        Board board = Board.getBoardInstance();
        if(placeHandler.checkHasPlayedQueen(opponent)) {
            HashMap<String, Integer> queenLocation = placeHandler.getQueenLocation(opponent);

            ArrayList<creatures.Tile> neighbours = board.getNeighbours(queenLocation.get("q"),queenLocation.get("r"));
            for(creatures.Tile tile: neighbours) {
                if (tile==null) {
                    return false;
                }
                if (tile.getPlayedByPlayer() == opponent) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean isDraw() {
        return false;
    }
}
