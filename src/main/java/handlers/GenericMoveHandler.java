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
            PlaceHandler.getPlaceHandler().naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        if (!PlaceHandler.getPlaceHandler().checkHasPlayedQueen(player)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        if (!checkContactExists(toQ,toR)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This move results in the tile having no contact");
        }
        if (!validCreatureSpecific(tile,fromQ,fromR,toQ,toR)) {
            PlaceHandler.getPlaceHandler().naivePlayTile(tile,fromQ,fromR);
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

    private ArrayList<HashMap<String,Integer>> getAllSuccessors(HashMap<String,Integer> location) {
        ArrayList<HashMap<String,Integer>>  successors = new ArrayList<>();
        HashMap<String,Integer> topLeft = parseCoordinatesToHashMap(location.get("q"),location.get("r")-1);
        successors.add(topLeft);

        HashMap<String,Integer> topRight = parseCoordinatesToHashMap(location.get("q")+1,location.get("r")-1);
        successors.add(topRight);

        HashMap<String,Integer> right = parseCoordinatesToHashMap(location.get("q")+1,location.get("r"));
        successors.add(right);

        HashMap<String,Integer> bottomRight = parseCoordinatesToHashMap(location.get("q"),location.get("r")+1);
        successors.add(bottomRight);

        HashMap<String,Integer> bottomLeft = parseCoordinatesToHashMap(location.get("q")-1,location.get("r")+1);
        successors.add(bottomLeft);

        HashMap<String,Integer> left = parseCoordinatesToHashMap(location.get("q")-1,location.get("r"));
        successors.add(left);
        return successors;
    }

    private ArrayList<HashMap<String,Integer>> getFilledSuccessors(HashMap<String,Integer> location) {
        ArrayList<HashMap<String,Integer>>  successors = new ArrayList<>();
        HashMap<String,Integer> topLeft = parseCoordinatesToHashMap(location.get("q"),location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topLeft.get("q"),topLeft.get("r")) != null) {
            successors.add(topLeft);
        }

        HashMap<String,Integer> topRight = parseCoordinatesToHashMap(location.get("q")+1,location.get("r")-1);
        if (Board.getBoardInstance().getPosition(topRight.get("q"),topRight.get("r")) != null) {
            successors.add(topRight);
        }

        HashMap<String,Integer> right = parseCoordinatesToHashMap(location.get("q")+1,location.get("r"));
        if (Board.getBoardInstance().getPosition(right.get("q"),right.get("r")) != null) {
            successors.add(right);
        }

        HashMap<String,Integer> bottomRight = parseCoordinatesToHashMap(location.get("q"),location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomRight.get("q"),bottomRight.get("r")) != null) {
            successors.add(bottomRight);
        }

        HashMap<String,Integer> bottomLeft = parseCoordinatesToHashMap(location.get("q")-1,location.get("r")+1);
        if (Board.getBoardInstance().getPosition(bottomLeft.get("q"),bottomLeft.get("r")) != null) {
            successors.add(bottomLeft);
        }

        HashMap<String,Integer> left = parseCoordinatesToHashMap(location.get("q")-1,location.get("r"));
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
            ArrayList<HashMap<String,Integer>> successors = getFilledSuccessors(locationNode);
            for (HashMap<String,Integer> child: successors) {
                if (!visited.contains(child)) {
                    queue.add(child);
                }
            }
        }
        return totalCount;
    }

    private HashMap<String, Integer> parseCoordinatesToHashMap(int q, int r) {
        HashMap<String,Integer> location = new HashMap<>();
        location.put("q",q);
        location.put("r",r);
        return location;
    }

    private boolean isGoal(HashMap<String, Integer> location, HashMap<String, Integer> goal) {
        return location.get("q").equals(goal.get("q")) && location.get("r").equals(goal.get("r"));
    }

    private ArrayList<HashMap<String,Integer>> orderSuccessorsWithHeuristics(ArrayList<HashMap<String,Integer>> successors, HashMap<String,Integer> goal) {
        for(int count = 0; count <= successors.size(); count++) {
            for(int i2 = 1; i2<successors.size(); i2++) {
                HashMap<String,Integer> successor2 = successors.get(i2);
                int i1 = i2-1;
                HashMap<String,Integer> successor1 = successors.get(i1);
                if(successor1.equals(successor2)) {
                    continue;
                }
                HashMap<String,Integer> closest = getClosestToGoal(successor1,successor2,goal);
                if(successor2.equals(closest)) {
                    successors.set(i1,successor2);
                    successors.set(i2,successor1);
                }
            }
        }
        return successors;
    }

    private HashMap<String, Integer> getClosestToGoal(HashMap<String, Integer> s1, HashMap<String, Integer> s2, HashMap<String, Integer> goal) {
        int score1 = getScore(s1,goal);
        int score2 = getScore(s2,goal);
        if(score1 <= score2) {
            return s1;
        }
        return s2;
    }

    private int getScore(HashMap<String, Integer> s,HashMap<String, Integer> goal) {
        int deltaQ = Math.abs(Math.abs(s.get("q")) - Math.abs(goal.get("q")));
        int deltaR = Math.abs(Math.abs(s.get("r")) - Math.abs(goal.get("r")));
        if(deltaQ ==0) {deltaQ = 1;}
        if(deltaR ==0) {deltaR = 1;}
        return deltaQ * deltaR;
    }

    public ArrayList<ArrayList<HashMap<String, Integer>>> findPathToLocation(int fromQ, int fromR, Hive.Player player, Hive.Tile creature, ArrayList<HashMap<String, Integer>> path, HashMap<String, Integer> goal, int depth,int maxDepth) {
        if(path == null) {path = new ArrayList<HashMap<String, Integer>>();}
        HashMap<String, Integer> location = parseCoordinatesToHashMap(fromQ,fromR);
        path.add(location);
        if (isGoal(location,goal)) {
            ArrayList<ArrayList<HashMap<String, Integer>>> subPaths = new ArrayList<>();
            subPaths.add(path);
            return subPaths;
        }
        if (maxDepth <= depth) {
            return new ArrayList<>();
        }
        ArrayList<ArrayList<HashMap<String, Integer>>> paths = new ArrayList<>();
        //order successors based on heuristics
        ArrayList<HashMap<String,Integer>> orderedSuccessors = orderSuccessorsWithHeuristics(getAllSuccessors(location),goal);
        for (HashMap<String,Integer> locationToGo: orderedSuccessors) {
            if (!path.contains(locationToGo)) {
                //Check of locationToGo valid is for the creature
                int q = locationToGo.get("q");
                int r = locationToGo.get("r");
                ArrayList<ArrayList<HashMap<String, Integer>>> newPaths = findPathToLocation(q,r,player,creature,path,goal,depth+1,maxDepth);
                if(newPaths.size() == 0) {
                    path.remove(path.size()-1);
                }
                for (ArrayList<HashMap<String, Integer>> newPath: newPaths) {
                    paths.add(newPath);
                }
            }
        }
        path.remove(path.size()-1);
        return paths;
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
