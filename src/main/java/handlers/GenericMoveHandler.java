package handlers;


import creatures.Tile;
import game.Board;
import game.Hive;

import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

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

    protected boolean validCreatureSpecific(Tile tileToMove, int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove {
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
                throw new Hive.IllegalMove("No valid creature was specified");
        }
        return creatureMoveHandler.isValidMove(fromQ, fromR, toQ, toR);
    }

    public void moveTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        int tileCountBefore = getTotalTileCount(player);
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (tile == null) {
            throw new Hive.IllegalMove("There is no tile to move");
        }
        if (tile.getPlayedByPlayer() != player) {
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        if (!PlaceHandler.getPlaceHandler().checkHasPlayedQueen(player)) {
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        if (!checkContactExists(toQ,toR)) {
            throw new Hive.IllegalMove("This move results in the tile having no contact");
        }
        if (!validCreatureSpecific(tile,fromQ,fromR,toQ,toR)) {
            throw new Hive.IllegalMove("This creature cannot make this move");
        }
        PlaceHandler.getPlaceHandler().naivePlayTile(tile,toQ,toR);
        int tileCountAfter = getTotalTileCount(player);
        if (tileCountBefore != tileCountAfter) {
            Tile revertTile = Board.getBoardInstance().removeTopTileAtPosition(toQ,toR);
            PlaceHandler.getPlaceHandler().naivePlayTile(revertTile,fromQ,fromR);
            throw new Hive.IllegalMove("This move results in at least 2 separate islands");
        }
    }

    public void slideTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = Board.getBoardInstance().removeTopTileAtPosition(fromQ,fromR);
        if (!checkSlideDistanceIsOne(fromQ,fromR,toQ,toR)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("Slide move is bigger then one");
        }
        if (!checkCommonNeighboursSize(fromQ, fromR, toQ, toR)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("This slide is blocked");
        }
        if (!checkWhileSlidingKeepContact(fromQ, fromR, toQ, toR)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("This slide does not keep contact");
        }
        PlaceHandler.getPlaceHandler().naivePlayTile(tile, fromQ, fromR);
        moveTile(fromQ, fromR, toQ, toR, player);
    }

    public boolean checkSlideDistanceIsOne(int fromQ, int fromR, int toQ, int toR) {
        return Math.abs(Math.abs(fromQ)-Math.abs(toQ)) <= 1 && Math.abs(Math.abs(fromR)-Math.abs(toR)) <= 1;
        //2 - (-1) = 3
        //-2 - (-2) = 0
        //3 + 0 = 3

        //-3 - (-2) = -1
        //0 - (2) = -2
        // -1 + (-2) = -3 = 3

        //0 - 2 = -2
        //0 - 0 = 0
        //-2 + 0 = -2 = 2

        //0 - 2 = -2
        //0 - (-2) = 2
        // -2 + 2 = 0
        //------
        //min(0,0) = 0
        //max(3,-3) = 3
        //abs(0-3) = 3

        //min(0,0) = 0
        //max(3,0) = 3
        //abs(0-3) = 3

        //min(0,0) = 0
        //max(-3,2) = 2
        //abs(0-2) = 2
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

    public boolean checkCommonNeighboursSize(int fromQ, int fromR, int toQ, int toR) {
        ArrayList<Integer[]> commonNeighbours = Board.getBoardInstance().getCommonNeighbours(fromQ,fromR, toQ, toR);
        if (commonNeighbours.size() == 2) {
            int n1 = Board.getBoardInstance().getSizeAtPosition(commonNeighbours.get(0)[0], commonNeighbours.get(0)[1]);
            int n2 = Board.getBoardInstance().getSizeAtPosition(commonNeighbours.get(1)[0], commonNeighbours.get(1)[1]);
            int a = Board.getBoardInstance().getSizeAtPosition(fromQ, fromR);
            int b = Board.getBoardInstance().getSizeAtPosition(toQ, toR);
            return Math.min(n1, n2) <= Math.max(a, b);
        }
        return true;
    }

    public boolean checkWhileSlidingKeepContact(int fromQ, int fromR, int toQ, int toR) {
        if(Board.getBoardInstance().getSizeAtPosition(toQ,toR) > 0) {
            return true;
        }
        ArrayList<Integer[]> commonNeighbours = Board.getBoardInstance().getCommonNeighbours(fromQ,fromR, toQ, toR);

        commonNeighbours.removeIf(location -> Board.getBoardInstance().getSizeAtPosition(location[0],location[1]) == 0);
        return commonNeighbours.size() >= 1;
    }

    private ArrayList<HashMap<String,Integer>> successors(HashMap<String,Integer> location) {
        ArrayList<HashMap<String,Integer>>  successors = new ArrayList<>();
        HashMap<String,Integer> topLeft = new HashMap<>();
        topLeft.put("q",location.get("q"));
        topLeft.put("r",location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topLeft.get("q"),topLeft.get("r")) != null) {
            successors.add(topLeft);
        }

        HashMap<String,Integer> topRight = new HashMap<>();
        topRight.put("q",location.get("q")+1);
        topRight.put("r",location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topRight.get("q"),topRight.get("r")) != null) {
            successors.add(topRight);
        }

        HashMap<String,Integer> right = new HashMap<>();
        right.put("q",location.get("q")+1);
        right.put("r",location.get("r"));
        if (Board.getBoardInstance().getPosition(right.get("q"),right.get("r")) != null) {
            successors.add(right);
        }

        HashMap<String,Integer> bottomRight = new HashMap<>();
        bottomRight.put("q",location.get("q"));
        bottomRight.put("r",location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomRight.get("q"),bottomRight.get("r")) != null) {
            successors.add(bottomRight);
        }

        HashMap<String,Integer> bottomLeft = new HashMap<>();
        bottomLeft.put("q",location.get("q")-1);
        bottomLeft.put("r",location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomLeft.get("q"),bottomLeft.get("r")) != null) {
            successors.add(bottomLeft);
        }

        HashMap<String,Integer> left = new HashMap<>();
        left.put("q",location.get("q")-1);
        left.put("r",location.get("r"));
        if (Board.getBoardInstance().getPosition(left.get("q"),left.get("r")) != null) {
            successors.add(left);
        }
        return successors;
    }

    public int getTotalTileCount(Hive.Player playerThatPlayedQueen) {
        Board board = Board.getBoardInstance();
        HashMap<String,Integer> startLocation = PlaceHandler.getPlaceHandler().getQueenLocation(playerThatPlayedQueen);
        ArrayList<HashMap<String,Integer>> visited = new ArrayList<>();
        int total = bfs(startLocation,visited);
        return total;
    }

    private int bfs(HashMap<String,Integer> locationNode, ArrayList<HashMap<String,Integer>> visited) {
        Stack<HashMap<String,Integer>> queue = new Stack<>();
        queue.add(locationNode);
        int totalCount = 0;
        while(queue.size() > 0) {
            locationNode = queue.pop();
            if (visited.contains(locationNode)) {
                continue;
            }
            visited.add(locationNode);
            totalCount += Board.getBoardInstance().getPosition(locationNode.get("q"),locationNode.get("r")).size();
            ArrayList<HashMap<String,Integer>> successors = successors(locationNode);
            for (HashMap<String,Integer> child: successors) {
                if (!visited.contains(child)) {
                    queue.add(child);
                }
            }
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
        PlaceHandler.getPlaceHandler().naivePlayTile(tile,toQ,toR);
    }
}
