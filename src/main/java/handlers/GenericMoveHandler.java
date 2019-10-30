package handlers;


import creatures.Tile;
import game.Board;
import game.Hive;

import java.util.ArrayList;
import java.util.HashMap;

public class GenericMoveHandler {
    private static GenericMoveHandler instance;
    /*
    This class implements all the generic requirements when moving a tile
    */
    public static GenericMoveHandler getGenericMoveHandler(){
        if (instance == null) {
            instance = new GenericMoveHandler();
        }
        return instance;
    }

    public void resetMoveHandler() {
        instance = null;
    }

    private GenericMoveHandler() {

    }

    protected boolean validateQueenBee() {
        /*
        Tiles can only be moved ones the queen bee of that player has been placed
         */
        return true;
    }

    protected boolean validateContact() {
        /*
        Tiles can only be moved if after the move they're still in contact with atleast one other tile
         */
        return true;
    }

    protected boolean validateNoIslands() {
        /*
        If a move results into 2 or more seperate 'islands' of tiles it is invalid
         */
        return true;
    }

    protected boolean valideCreatureSpecific(Tile tileToMove, int fromQ, int fromR, int toQ, int toR) throws Exception {
        /*
        Each creature has its own moving constraints
         */
        CreatureMoveHandler creatureMoveHandler;
        switch (tileToMove.getCreature()) {
            case SOLDIER_ANT:
                creatureMoveHandler = AntMoveHandler.getInstance();
                break;
            case GRASSHOPPER:
                creatureMoveHandler = GrasshopperMoveHandler.getInstance();
                break;
            case QUEEN_BEE:
                creatureMoveHandler = QueenBeeMoveHandler.getInstance();
                break;
            case SPIDER:
                creatureMoveHandler = SpiderMoveHandler.getInstance();
                break;
            case BEETLE:
                creatureMoveHandler = BeetleMoveHandler.getInstance();
                break;
            default:
                throw new Exception("No valid creature was specified");
        }
        return creatureMoveHandler.isValidMove(fromQ, fromR, toQ, toR);
    }

    public boolean canMakeMove(Tile tileToMove, int fromQ, int fromR, int toQ, int toR) {
        return true;
    }

    public void moveTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (tile == null) {
            throw new Hive.IllegalMove("There is no tile to move");
        }
        if (tile.getPlayedByPlayer() != player) {
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        if (PlaceHandler.getPlaceHandler().checkHasPlayedQueen(player)) {
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }

    private boolean checkContactExists(int q, int r) {
        Board gameBoard = Board.getBoardInstance();
        ArrayList<Tile> neighbours = gameBoard.getNeighbours(q, r);
        for (Tile tile : neighbours) {
            if (tile != null) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<HashMap<String,Integer>> succesors(HashMap<String,Integer> location) {
        ArrayList<HashMap<String,Integer>>  succesors = new ArrayList<>();
        HashMap<String,Integer> topLeft = new HashMap<>();
        topLeft.put("q",location.get("q"));
        topLeft.put("r",location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topLeft.get("q"),topLeft.get("r")) != null) {
            succesors.add(topLeft);
        }

        HashMap<String,Integer> topRight = new HashMap<>();
        topRight.put("q",location.get("q")+1);
        topRight.put("r",location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topRight.get("q"),topRight.get("r")) != null) {
            succesors.add(topRight);
        }

        HashMap<String,Integer> right = new HashMap<>();
        right.put("q",location.get("q")+1);
        right.put("r",location.get("r"));
        if (Board.getBoardInstance().getPosition(right.get("q"),right.get("r")) != null) {
            succesors.add(right);
        }

        HashMap<String,Integer> bottomRight = new HashMap<>();
        bottomRight.put("q",location.get("q"));
        bottomRight.put("r",location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomRight.get("q"),bottomRight.get("r")) != null) {
            succesors.add(bottomRight);
        }

        HashMap<String,Integer> bottomLeft = new HashMap<>();
        bottomLeft.put("q",location.get("q")-1);
        bottomLeft.put("r",location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomLeft.get("q"),bottomLeft.get("r")) != null) {
            succesors.add(bottomLeft);
        }

        HashMap<String,Integer> left = new HashMap<>();
        left.put("q",location.get("q")-1);
        left.put("r",location.get("r"));
        if (Board.getBoardInstance().getPosition(left.get("q"),left.get("r")) != null) {
            succesors.add(left);
        }
        return succesors;
    }

    public int getTotalTileCount(Hive.Player playerThatPlayedQueen) {
        Board board = Board.getBoardInstance();
        HashMap<String,Integer> startLocation = PlaceHandler.getPlaceHandler().getQueenLocation(playerThatPlayedQueen);
        int totalCount = board.getPosition(startLocation.get("q"),startLocation.get("r")).size();
        ArrayList<HashMap<String,Integer>> visited = new ArrayList<>();
        int total = dfs(startLocation,visited,totalCount);
        return total;
    }

    private int dfs(HashMap<String,Integer> locationNode, ArrayList<HashMap<String,Integer>> visited,int totalCount) {
        visited.add(locationNode);

        ArrayList<HashMap<String,Integer>> succesors = succesors(locationNode);
        for(HashMap<String,Integer> locationChild: succesors) {
            if (!visited.contains(locationChild)) {
                System.out.println("Not in visited");
                int subCount = dfs(locationChild,visited,totalCount);
                System.out.println("Subcount is " + subCount);
                totalCount += subCount;
                System.out.println("Totalcount is "+ totalCount);
            }
            return Board.getBoardInstance().getPosition(locationNode.get("q"),locationNode.get("r")).size();
        }

        return totalCount;

    }

    //------------------------------------------------- TEST METHODS

    public void moveTileTestNoTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (tile == null) {
            throw new Hive.IllegalMove("There is no tile to move");
        }
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestWrongPlayer(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (tile.getPlayedByPlayer() != player) {
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestNoQueen(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (!PlaceHandler.getPlaceHandler().checkHasPlayedQueen(player)) {
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestContact(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (!this.checkContactExists(toQ,toR)) {
            throw new Hive.IllegalMove("The new location results in zero contact");
        }
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTest(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        Board.getBoardInstance().placeTileAtPosition(toQ,toR,tile);
    }
}
