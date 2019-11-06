package game;

import creatures.Tile;

import java.util.*;

public class Board {
    private HashMap<Integer, HashMap<Integer, Stack<Tile>>> gameBoard = new HashMap<>();
    private static Board instance;

    public static Board getBoardInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public void resetBoard() {
        this.gameBoard = new HashMap<>();
    }

    private Board() {}

    public HashMap<Integer, HashMap<Integer, Stack<Tile>>> getBoard() {
        return gameBoard;
    }

    public Stack<Tile> getPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public int getSizeAtPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r).size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public Tile getTopTileAtPosition(int q, int r) {
        try {
            return gameBoard.get(q).get(r).peek();
        } catch(NullPointerException|EmptyStackException ex) {
            return null;
        }
    }

    public Tile removeTopTileAtPosition(int q, int r) {
        try {
            Tile tile = gameBoard.get(q).get(r).pop();
            if (gameBoard.get(q).get(r).isEmpty()) {
                gameBoard.get(q).put(r,null);
            }
            return tile;
        } catch(NullPointerException|EmptyStackException ex) {
            return null;
        }
    }

    public HashMap<String, int[]> getNeighbouringCoordinates(int q, int r) {
        HashMap<String, int[]> coordinates = new HashMap<>();
        int[] coordinate = new int[2];
        coordinate[0] = q;
        coordinate[1] = r-1;
        coordinates.put("topLeft", coordinate);

        coordinate = new int[2];
        coordinate[0] = q+1;
        coordinate[1] = r-1;
        coordinates.put("topRight",coordinate);

        coordinate = new int[2];
        coordinate[0] = q+1;
        coordinate[1] = r;
        coordinates.put("right",coordinate);

        coordinate = new int[2];
        coordinate[0] = q;
        coordinate[1] = r+1;
        coordinates.put("bottomRight",coordinate);

        coordinate = new int[2];
        coordinate[0] = q-1;
        coordinate[1] = r+1;
        coordinates.put("bottomLeft", coordinate);

        coordinate = new int[2];
        coordinate[0] = q-1;
        coordinate[1] = r;
        coordinates.put("left",coordinate);
        return coordinates;
    }

    public ArrayList<Tile> getNeighbours(int q, int r) {
        HashMap<String, int[]> coordinates = getNeighbouringCoordinates(q,r);
        ArrayList<Tile> neighboursList = new ArrayList<>();

        Tile topLeftTile = getTopTileAtPosition(coordinates.get("topLeft")[0],coordinates.get("topLeft")[1]);
        neighboursList.add(topLeftTile);

        Tile topRightTile = getTopTileAtPosition(coordinates.get("topRight")[0],coordinates.get("topRight")[1]);
        neighboursList.add(topRightTile);

        Tile rightTile = getTopTileAtPosition(coordinates.get("right")[0],coordinates.get("right")[1]);
        neighboursList.add(rightTile);

        Tile bottomRightTile = getTopTileAtPosition(coordinates.get("bottomRight")[0],coordinates.get("bottomRight")[1]);
        neighboursList.add(bottomRightTile);

        Tile bottomLeftTile = getTopTileAtPosition(coordinates.get("bottomLeft")[0],coordinates.get("bottomLeft")[1]);
        neighboursList.add(bottomLeftTile);

        Tile leftTile = getTopTileAtPosition(coordinates.get("left")[0],coordinates.get("left")[1]);
        neighboursList.add(leftTile);

        return neighboursList;
    }

    public ArrayList<Integer> getNeighbouringPositionsSize(int q, int r) {
        HashMap<String, int[]> coordinates = getNeighbouringCoordinates(q,r);
        ArrayList<Integer> neighboursSizeList = new ArrayList<>();

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("topLeft")[0],coordinates.get("topLeft")[1]));

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("topRight")[0],coordinates.get("topRight")[1]));

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("right")[0],coordinates.get("right")[1]));

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("bottomRight")[0],coordinates.get("bottomRight")[1]));

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("bottomLeft")[0],coordinates.get("bottomLeft")[1]));

        neighboursSizeList.add(getSizeAtPosition(coordinates.get("left")[0],coordinates.get("left")[1]));
        return neighboursSizeList;
    }

//    protected void peekAtNeighbours(int q, int r) {
//        HashMap<String, int[]> coordinates = getNeighbouringCoordinates(q,r);
//        for(String direction: coordinates.keySet()) {
//            peekAtPosition(coordinates.get(direction)[0],coordinates.get(direction)[1]);
//        }
//    }
//
    private void peekAtPosition(int q, int r) {
        if (gameBoard.get(q) == null) {
            gameBoard.put(q,new HashMap<Integer, Stack<Tile>>());
            gameBoard.get(q).put(r, new Stack<Tile>());
        } else if (gameBoard.get(q).get(r) == null) {
            gameBoard.get(q).put(r, new Stack<Tile>());
        }
    }

    public void placeTileAtPosition(int q, int r, Tile tile){
        peekAtPosition(q,r);
        gameBoard.get(q).get(r).push(tile);
    }

    private ArrayList<Integer[]> convertCoordinatesArray(Collection<int[]> coordinates) {
        ArrayList<Integer[]> returnList = new ArrayList<>();
        for(int[] coordinate: coordinates) {
            Integer[] newCoordinate = new Integer[2];
            newCoordinate[0] = coordinate[0];
            newCoordinate[1] = coordinate[1];
            returnList.add(newCoordinate);
        }
        return returnList;
    }

    public ArrayList<Integer[]> getCommonNeighbours(int fromQ, int fromR, int toQ, int toR) {
        Collection<int[]> valuesA = Board.getBoardInstance().getNeighbouringCoordinates(fromQ, fromR).values();
        Collection<int[]> valuesB = Board.getBoardInstance().getNeighbouringCoordinates(toQ, toR).values();
        ArrayList<Integer[]> neighboursA = convertCoordinatesArray(valuesA);
        ArrayList<Integer[]> neighboursB = convertCoordinatesArray(valuesB);
        ArrayList<Integer[]> commonNeighbours = new ArrayList<>();
        for(Integer[] locationA: neighboursA) {
            for(Integer[] locationB: neighboursB) {
                if(locationA[0].equals(locationB[0]) && locationA[1].equals(locationB[1])) {
                    commonNeighbours.add(locationA);
                }
            }
        }
        return commonNeighbours;
    }
}
