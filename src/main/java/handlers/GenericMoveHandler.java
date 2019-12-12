package handlers;


import creatures.Tile;
import game.Board;
import game.HiveGame;
import nl.hanze.hive.Hive;

import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class GenericMoveHandler {
    private HiveGame game;
    private AntMoveHandler antMoveHandler;
    private GrasshopperMoveHandler grasshopperMoveHandler;
    private QueenBeeMoveHandler queenBeeMoveHandler;
    private SpiderMoveHandler spiderMoveHandler;
    private BeetleMoveHandler beetleMoveHandler;
    private PlaceHandler placeHandler;

    public GenericMoveHandler() {
        this.antMoveHandler = new AntMoveHandler();
        this.antMoveHandler.setMoveHandler(this);

        this.grasshopperMoveHandler = new GrasshopperMoveHandler();
        this.grasshopperMoveHandler.setMoveHandler(this);

        this.queenBeeMoveHandler = new QueenBeeMoveHandler();
        this.queenBeeMoveHandler.setMoveHandler(this);

        this.spiderMoveHandler = new SpiderMoveHandler();
        this.spiderMoveHandler.setMoveHandler(this);

        this.beetleMoveHandler = new BeetleMoveHandler();
        this.beetleMoveHandler.setMoveHandler(this);
    }

    public void setGame(HiveGame game) {
        this.game = game;
        this.antMoveHandler.setGame(game);
        this.grasshopperMoveHandler.setGame(game);
        this.queenBeeMoveHandler.setGame(game);
        this.spiderMoveHandler.setGame(game);
        this.beetleMoveHandler.setGame(game);
    }

    public void setPlaceHandler(PlaceHandler placeHandler) {
        this.placeHandler = placeHandler;
    }

    public AntMoveHandler getAntMoveHandler() {
        return antMoveHandler;
    }

    public GrasshopperMoveHandler getGrasshopperMoveHandler() {
        return grasshopperMoveHandler;
    }

    public QueenBeeMoveHandler getQueenBeeMoveHandler() {
        return queenBeeMoveHandler;
    }

    public SpiderMoveHandler getSpiderMoveHandler() {
        return spiderMoveHandler;
    }

    public BeetleMoveHandler getBeetleMoveHandler() {
        return beetleMoveHandler;
    }

    protected boolean validCreatureSpecific(Tile tileToMove, int fromQ, int fromR, int toQ, int toR) throws Hive.IllegalMove {
        /*
        Each creature has its own moving constraints
         */
        return getCreatureMoveHandler(tileToMove.getCreature()).isValidMove(fromQ, fromR, toQ, toR);
    }

    private CreatureMoveHandler getCreatureMoveHandler(Hive.Tile creature) throws Hive.IllegalMove {
        switch (creature) {
            case SOLDIER_ANT:
                return this.antMoveHandler;
            case GRASSHOPPER:
                return this.grasshopperMoveHandler;
            case QUEEN_BEE:
                return this.queenBeeMoveHandler;
            case SPIDER:
                return this.spiderMoveHandler;
            case BEETLE:
                return this.beetleMoveHandler;
            default:
                throw new Hive.IllegalMove("This creature does not exists");
        }
    }

    public void moveTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (tile == null) {
            throw new Hive.IllegalMove("There is no tile to move");
        }
        if (!this.placeHandler.checkHasPlayedQueen(player)) {
            this.placeHandler.naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        int tileCountBefore = getTotalTileCount(player);
        if (tile.getCreature() != Hive.Tile.QUEEN_BEE) {
            tileCountBefore += 1;
        }
        if (tile.getPlayedByPlayer() != player) {
            this.placeHandler.naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        if(!checkActuallyMoves(fromQ,fromR,toQ,toR)) {
            this.placeHandler.naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This is not a move");
        }
        if (!checkContactExists(toQ,toR)) {
            this.placeHandler.naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This move results in the tile having no contact");
        }
        if (!validCreatureSpecific(tile,fromQ,fromR,toQ,toR)) {
            this.placeHandler.naivePlayTile(tile,fromQ,fromR);
            throw new Hive.IllegalMove("This creature cannot make this move");
        }
        this.placeHandler.naivePlayTile(tile,toQ,toR);
        int tileCountAfter = getTotalTileCount(player);
        if (tileCountBefore != tileCountAfter) {
            Tile revertTile = this.game.getCurrentBoard().removeTopTileAtPosition(toQ,toR);
            this.placeHandler.naivePlayTile(revertTile,fromQ,fromR);
            throw new Hive.IllegalMove("This move results in at least 2 separate islands");
        }
    }

    public void slideTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (!checkSlideDistanceIsOne(fromQ,fromR,toQ,toR)) {
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("Slide move is bigger then one");
        }
        if (!checkCommonNeighboursSize(fromQ, fromR, toQ, toR)) {
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("This slide is blocked");
        }
        if (!checkWhileSlidingKeepContact(fromQ, fromR, toQ, toR)) { //! FLIP
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            throw new Hive.IllegalMove("This slide does not keep contact");
        }
        this.placeHandler.naivePlayTile(tile, fromQ, fromR);
        moveTile(fromQ, fromR, toQ, toR, player);
    }

    //Special boolean method that checks whether or not a tile can slide, this is not used in slideTile() so slideTile() can return accurate error messages
    public boolean canSlideTile(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (!checkSlideDistanceIsOne(fromQ,fromR,toQ,toR)) {
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            return false;
        }
        if (!checkCommonNeighboursSize(fromQ, fromR, toQ, toR)) {
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            return false;
        }
        if (!checkWhileSlidingKeepContact(fromQ, fromR, toQ, toR)) {
            this.placeHandler.naivePlayTile(tile, fromQ, fromR);
            return false;
        }
        this.placeHandler.naivePlayTile(tile, fromQ, fromR);
        return true;
    }

    public boolean checkSlideDistanceIsOne(int fromQ, int fromR, int toQ, int toR) {
        HashMap<String, int[]> neighbours = this.game.getCurrentBoard().getNeighbouringCoordinates(fromQ,fromR);
        for(String direction : neighbours.keySet()) {
            int[] coordinate = neighbours.get(direction);
            if(coordinate[0] == toQ && coordinate[1] == toR) {
                return true;
            }
        }
        return false;
    }

    private boolean checkContactExists(int q, int r) {
        ArrayList<Tile> neighbours = this.game.getCurrentBoard().getNeighbours(q, r);
        for (Tile tile : neighbours) {
            if (tile != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkActuallyMoves(int fromQ, int fromR, int toQ, int toR) {
        return fromQ != toQ || fromR != toR;
    }

    public boolean checkCommonNeighboursSize(int fromQ, int fromR, int toQ, int toR) {
        ArrayList<Integer[]> commonNeighbours = this.game.getCurrentBoard().getCommonNeighbours(fromQ,fromR, toQ, toR);
        if (commonNeighbours.size() == 2) {
            int n1 = this.game.getCurrentBoard().getSizeAtPosition(commonNeighbours.get(0)[0], commonNeighbours.get(0)[1]);
            int n2 = this.game.getCurrentBoard().getSizeAtPosition(commonNeighbours.get(1)[0], commonNeighbours.get(1)[1]);
            int a = this.game.getCurrentBoard().getSizeAtPosition(fromQ, fromR);
            int b = this.game.getCurrentBoard().getSizeAtPosition(toQ, toR);
            return Math.min(n1, n2) <= Math.max(a, b);
        }
        return true;
    }

    public boolean checkWhileSlidingKeepContact(int fromQ, int fromR, int toQ, int toR) {
        if(this.game.getCurrentBoard().getSizeAtPosition(toQ,toR) > 0) {
            return true;
        }
        ArrayList<Integer[]> commonNeighbours = this.game.getCurrentBoard().getCommonNeighbours(fromQ,fromR, toQ, toR);

        commonNeighbours.removeIf(location -> this.game.getCurrentBoard().getSizeAtPosition(location[0],location[1]) == 0);
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
        if (this.game.getCurrentBoard().getPosition(topLeft.get("q"),topLeft.get("r")) != null) {
            successors.add(topLeft);
        }

        HashMap<String,Integer> topRight = parseCoordinatesToHashMap(location.get("q")+1,location.get("r")-1);
        if (this.game.getCurrentBoard().getPosition(topRight.get("q"),topRight.get("r")) != null) {
            successors.add(topRight);
        }

        HashMap<String,Integer> right = parseCoordinatesToHashMap(location.get("q")+1,location.get("r"));
        if (this.game.getCurrentBoard().getPosition(right.get("q"),right.get("r")) != null) {
            successors.add(right);
        }

        HashMap<String,Integer> bottomRight = parseCoordinatesToHashMap(location.get("q"),location.get("r")+1);
        if (this.game.getCurrentBoard().getPosition(bottomRight.get("q"),bottomRight.get("r")) != null) {
            successors.add(bottomRight);
        }

        HashMap<String,Integer> bottomLeft = parseCoordinatesToHashMap(location.get("q")-1,location.get("r")+1);
        if (this.game.getCurrentBoard().getPosition(bottomLeft.get("q"),bottomLeft.get("r")) != null) {
            successors.add(bottomLeft);
        }

        HashMap<String,Integer> left = parseCoordinatesToHashMap(location.get("q")-1,location.get("r"));
        if (this.game.getCurrentBoard().getPosition(left.get("q"),left.get("r")) != null) {
            successors.add(left);
        }
        return successors;
    }

    public int getTotalTileCount(Hive.Player playerThatPlayedQueen) {
        HashMap<String,Integer> startLocation = this.placeHandler.getQueenLocation(playerThatPlayedQueen);
        ArrayList<HashMap<String,Integer>> visited = new ArrayList<>();
        int total = calculateTotalTiles(startLocation,visited);
        return total;
    }

    public boolean canMakeAnyMove(Hive.Player player) {
        if(!this.placeHandler.checkHasPlayedQueen(player)) { return true; }
        HashMap<String, Integer> queenLocation = this.placeHandler.getQueenLocation(player);
        return checkIfPossibleMovesForAllTiles(queenLocation, new ArrayList<>(), player);
    }

    private boolean checkIfPossibleMovesForAllTiles(HashMap<String,Integer> locationNode, ArrayList<HashMap<String,Integer>> visited, Hive.Player player) {
        Deque<HashMap<String, Integer>> queue = new ArrayDeque<>();
        queue.add(locationNode);
        while(queue.size() > 0) {
            locationNode = queue.pop();
            Tile tile = this.game.getCurrentBoard().getTopTileAtPosition(locationNode.get("q"), locationNode.get("r"));
            if (visited.contains(locationNode)) {
                continue;
            }
            if(tile.getPlayedByPlayer() != player) {
                continue; // Not this player's tile
            }
            if(tile.getCreature() == Hive.Tile.GRASSHOPPER) {
                return true;
            }
            // check can tile move
            try {
                CreatureMoveHandler creatureMoveHandler = getCreatureMoveHandler(tile.getCreature());
                if(creatureMoveHandler.canMakeAnyMove(locationNode.get("q"),locationNode.get("r"),player)) {
                    return true;
                }
            } catch(Hive.IllegalMove ex) {
                ex.printStackTrace();
            }
            visited.add(locationNode);
            ArrayList<HashMap<String,Integer>> successors = getFilledSuccessors(locationNode);
            for (HashMap<String,Integer> child: successors) {
                if (!visited.contains(child)) {
                    queue.addFirst(child);
                }
            }
        }
        return false; //True = has possible moves, False = No possible moves
    }

    private int calculateTotalTiles(HashMap<String,Integer> locationNode, ArrayList<HashMap<String,Integer>> visited) {
        Stack<HashMap<String,Integer>> queue = new Stack<>();
        queue.add(locationNode);
        int totalCount = this.game.getCurrentBoard().getSizeAtPosition(locationNode.get("q"),locationNode.get("r"));
        if(totalCount == 0) {
            totalCount +=1;
        }
        while(queue.size() > 0) {
            locationNode = queue.pop();
            if (visited.contains(locationNode)) {
                continue;
            }
            visited.add(locationNode);
            if(visited.size() != 1) {
                totalCount += this.game.getCurrentBoard().getSizeAtPosition(locationNode.get("q"),locationNode.get("r"));
            }
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
            try {
                if(getCreatureMoveHandler(creature).validatePathSize(depth)) {
                    return subPaths;
                }
            } catch (Hive.IllegalMove ex) {
                ex.printStackTrace();
            }
            path.remove(path.size()-1);
            return new ArrayList<>();
        }
        if (maxDepth <= depth) {
            path.remove(path.size()-1);
            return new ArrayList<>();
        }
        ArrayList<ArrayList<HashMap<String, Integer>>> paths = new ArrayList<>();
        ArrayList<HashMap<String,Integer>> orderedSuccessors = orderSuccessorsWithHeuristics(getAllSuccessors(location),goal);
        for (HashMap<String,Integer> locationToGo: orderedSuccessors) {
            if (!path.contains(locationToGo)) {
                //Check of locationToGo valid is for the creature
                try{
                    CreatureMoveHandler creatureMoveHandler = getCreatureMoveHandler(creature);
                    int q = locationToGo.get("q");
                    int r = locationToGo.get("r");
                    if(!creatureMoveHandler.isValidMove(fromQ,fromR,q,r)) {
                        continue;
                    }
                    ArrayList<ArrayList<HashMap<String, Integer>>> newPaths = findPathToLocation(q,r,player,creature,path,goal,depth+1,maxDepth);
                    Iterator<ArrayList<HashMap<String, Integer>>> iterator = newPaths.iterator();
                    while(iterator.hasNext()) {
                        paths.add( (ArrayList<HashMap<String, Integer>>) iterator.next().clone());
                    }
                } catch (Hive.IllegalMove ex) {
                    ex.printStackTrace();
                }
            }
        }
        path.remove(path.size()-1);
        return paths;
    }

    public void resetMove(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(toQ,toR);
        this.game.getCurrentBoard().placeTileAtPosition(fromQ,fromR, tile);
    }

    public void tryMakeSlidingMove(ArrayList<ArrayList<HashMap<String, Integer>>> validPaths, Hive.Player currentPlayer) throws Hive.IllegalMove {
        boolean succesfullMove = false;
        if(validPaths.size() > 0) {
            for(ArrayList<HashMap<String, Integer>> validPath: validPaths) {
                    for(int i=0; i<validPath.size()-1;i++) {
                        if(succesfullMove) {
                            continue;
                        }
                        HashMap<String, Integer> location = validPath.get(i);
                        HashMap<String, Integer> locationToMoveTo = validPath.get(i+1);
                        try {
                            this.slideTile(location.get("q"),location.get("r"),locationToMoveTo.get("q"),locationToMoveTo.get("r"),currentPlayer);

                            succesfullMove = true;
                        } catch (Hive.IllegalMove ex) {
                            this.resetMove(location.get("q"),location.get("r"),locationToMoveTo.get("q"),locationToMoveTo.get("r"));
                            continue;
                        }
                }
            }
            if(!succesfullMove) {
                throw new Hive.IllegalMove("This tile has no valid sliding paths");
            }
        } else {
            throw new Hive.IllegalMove("This tile cannot move to that location");
        }
    }

    //------------------------------------------------- TEST METHODS

    public void moveTileTestNoTile(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (tile == null) {
            throw new Hive.IllegalMove("There is no tile to move");
        }
        this.game.getCurrentBoard().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestWrongPlayer(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (tile.getPlayedByPlayer() != player) {
            throw new Hive.IllegalMove("This tile does not belong to this player");
        }
        this.game.getCurrentBoard().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestNoQueen(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove{
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (!this.placeHandler.checkHasPlayedQueen(player)) {
            throw new Hive.IllegalMove("This player's queen has not been played yet");
        }
        this.game.getCurrentBoard().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTestContact(int fromQ, int fromR, int toQ, int toR, Hive.Player player) throws Hive.IllegalMove {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        if (!this.checkContactExists(toQ,toR)) {
            throw new Hive.IllegalMove("The new location results in zero contact");
        }
        this.game.getCurrentBoard().placeTileAtPosition(toQ,toR,tile);
    }

    public void moveTileTest(int fromQ, int fromR, int toQ, int toR) {
        Tile tile = this.game.getCurrentBoard().removeTopTileAtPosition(fromQ,fromR);
        this.placeHandler.naivePlayTile(tile,toQ,toR);
    }
}
