package game;

import handlers.GenericMoveHandler;
import handlers.PlaceHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class HiveGame implements Hive {
    private Player currentPlayer;
    private Player opponent;
    private Board gameBoard;
    private PlaceHandler placeHandler;
    private GenericMoveHandler genericMoveHandler;

    public HiveGame() {
        currentPlayer = Player.WHITE;
        opponent = Player.BLACK;
        gameBoard = new Board();
        placeHandler = new PlaceHandler();
        placeHandler.setGame(this);

        genericMoveHandler = new GenericMoveHandler();
        genericMoveHandler.setGame(this);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getCurrentBoard() { return gameBoard; }

    public void switchPlayer() {
        if (currentPlayer == Player.WHITE) {
            currentPlayer = Player.BLACK;
            opponent = Player.WHITE;
        } else if(currentPlayer == Player.BLACK) {
            currentPlayer = Player.WHITE;
            opponent = Player.BLACK;
        }
    }

//    public void updateGame() {
//        placeHandler.setGame(this);
//    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        creatures.Tile tileToPlace = TileFactory.makeTile(tile,currentPlayer);
        placeHandler.playTile(tileToPlace,q,r);
        switchPlayer();
    }



    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        GenericMoveHandler genericMoveHandler = GenericMoveHandler.getGenericMoveHandler();
        creatures.Tile tileToMove = this.gameBoard.getTopTileAtPosition(fromQ,fromR);
        HashMap<String, Integer> goal = new HashMap<>();
        goal.put("q",toQ);
        goal.put("r",toR);
        ArrayList<ArrayList<HashMap<String, Integer>>> validPaths;
        switch (tileToMove.getCreature()) {
            case BEETLE:
                genericMoveHandler.slideTile(fromQ,fromR,toQ,toR,currentPlayer);
                break;
            case QUEEN_BEE:
                genericMoveHandler.slideTile(fromQ,fromR,toQ,toR,currentPlayer);
                break;
            case SOLDIER_ANT:
                validPaths = genericMoveHandler.findPathToLocation(fromQ,fromR,currentPlayer,Tile.SOLDIER_ANT,null,goal,0,100);
                genericMoveHandler.tryMakeSlidingMove(validPaths,currentPlayer);
                break;
            case SPIDER:
                validPaths = genericMoveHandler.findPathToLocation(fromQ,fromR,currentPlayer,Tile.SPIDER,null,goal,0,3);
                genericMoveHandler.tryMakeSlidingMove(validPaths,currentPlayer);
                break;
            case GRASSHOPPER:
                genericMoveHandler.moveTile(fromQ,fromR,toQ,toR,currentPlayer);
                break;
        }
        switchPlayer();
    }

    @Override
    public void pass() throws IllegalMove {
        if(GenericMoveHandler.getGenericMoveHandler().canMakeAnyMove(currentPlayer) && placeHandler.hasTilesToPlay(currentPlayer)) {
            throw new IllegalMove("This player can still make a move");
        } else {
            switchPlayer();
        }
    }

    @Override
    public boolean isWinner(Player player) {
        if(placeHandler.checkHasPlayedQueen(opponent)) {
            HashMap<String, Integer> queenLocation = placeHandler.getQueenLocation(opponent);

            ArrayList<creatures.Tile> neighbours = this.gameBoard.getNeighbours(queenLocation.get("q"),queenLocation.get("r"));
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
