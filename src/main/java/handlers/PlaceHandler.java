package handlers;

import creatures.Tile;
import game.Hive;
import game.Board;
import game.HiveGame;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceHandler {
    private static PlaceHandler instance;
    private HashMap<Hive.Tile, Integer> whiteTilesToPlay = new HashMap<>();
    private HashMap<Hive.Tile, Integer> blackTilesToPlay = new HashMap<>();
    private boolean whiteHasPlayedQueen = false;
    private boolean blackHasPlayedQueen = false;
    private int whiteTileCountPlayed = 0;
    private int blackTileCountPlayed = 0;
    private HashMap<String, Integer> whiteQueenLocation = new HashMap<>();
    private HashMap<String, Integer> blackQueenLocation = new HashMap<>();

    /*
    This class implements all the requirements when placing a tile
    */
    public static PlaceHandler getPlaceHandler() {
        if (instance == null) {
            instance = new PlaceHandler();
        }
        return instance;
    }

    public void reset() {
        instance = null;
    }

    private PlaceHandler() {
        this.whiteTilesToPlay.put(Hive.Tile.QUEEN_BEE, 1);
        this.whiteTilesToPlay.put(Hive.Tile.SPIDER, 2);
        this.whiteTilesToPlay.put(Hive.Tile.BEETLE, 2);
        this.whiteTilesToPlay.put(Hive.Tile.SOLDIER_ANT, 3);
        this.whiteTilesToPlay.put(Hive.Tile.GRASSHOPPER, 3);

        this.blackTilesToPlay.put(Hive.Tile.QUEEN_BEE, 1);
        this.blackTilesToPlay.put(Hive.Tile.SPIDER, 2);
        this.blackTilesToPlay.put(Hive.Tile.BEETLE, 2);
        this.blackTilesToPlay.put(Hive.Tile.SOLDIER_ANT, 3);
        this.blackTilesToPlay.put(Hive.Tile.GRASSHOPPER, 3);
    }

    private HashMap<Hive.Tile, Integer> getTilesToPlay(Hive.Player player) {
        HashMap<Hive.Tile, Integer> playerTilesToPlay;
        switch (player) {
            case BLACK:
                playerTilesToPlay = blackTilesToPlay;
                break;
            case WHITE:
                playerTilesToPlay = whiteTilesToPlay;
                break;
            default:
                System.out.println("Option " + player + " unknown, reverting to WHITE");
                playerTilesToPlay = whiteTilesToPlay;
        }
        return playerTilesToPlay;
    }

    public boolean canPlayerPlayTile(Hive.Player player, Hive.Tile tile) {
        int tileCountLeft;
        HashMap<Hive.Tile, Integer> playerTilesToPlay = getTilesToPlay(player);
        tileCountLeft = playerTilesToPlay.get(tile);
        return tileCountLeft > 0;
    }

    public void removeTileFromTilesToPlay(Hive.Player player, Hive.Tile tile) {
        HashMap<Hive.Tile, Integer> playerTilesToPlay = getTilesToPlay(player);
        int tileCount = playerTilesToPlay.get(tile);
        tileCount -= 1;
        playerTilesToPlay.put(tile, tileCount);
    }

    private boolean checkCanPlayTile(Hive.Player player, Hive.Tile tile, int q, int r) {
        if (whiteTileCountPlayed + blackTileCountPlayed > 0) {
            return canPlayerPlayTile(player,tile) && checkLocationEmpty(q,r) && checkContactExists(q,r);
        }
        return canPlayerPlayTile(player,tile) && checkLocationEmpty(q,r);
    }

    public void playTileTest(Hive.Player player, Tile tile, int q, int r) {
        Board gameBoard = Board.getBoardInstance();
        tile.setPlayedByPlayer(player);

        updateTileCountPlayed(player);
        if(tile.getCreature() == Hive.Tile.QUEEN_BEE) {
            updateHasPlayedQueen(player,q,r);
        }
        gameBoard.placeTileAtPosition(q, r, tile);
    }

    public void playTile(Tile tile, int q, int r) throws Hive.IllegalMove {
        Hive.Player player = tile.getPlayedByPlayer();
        if (checkCanPlayTile(player,tile.getCreature(),q,r)) {
            Board gameBoard = Board.getBoardInstance();
            tile.setPlayedByPlayer(player);

            removeTileFromTilesToPlay(player, tile.getCreature());
            updateTileCountPlayed(player);
            if(tile.getCreature() == Hive.Tile.QUEEN_BEE) {
                updateHasPlayedQueen(player, q,r);
            }
            gameBoard.placeTileAtPosition(q, r, tile);
        } else {
            throw new Hive.IllegalMove("This is an illegal move");
        }
    }

    protected boolean checkLocationEmpty(int q, int r) {
        /*
            A tile can only be placed on an empty location
         */
        Board board = Board.getBoardInstance();
        return board.getTopTileAtPosition(q,r) == null;
    }

    protected boolean checkContactExists(int q, int r) {
        /*
            A player can only play tiles if the placed tile has contact with other tiles, but this must not be an opponent tile
         */
        Board gameBoard = Board.getBoardInstance();
        ArrayList<Tile> neighbours = gameBoard.getNeighbours(q, r);
        for (Tile tile : neighbours) {
            if (tile != null) {
                if (whiteTileCountPlayed > 0 && blackTileCountPlayed > 0) {
                    return isNotOpponent(tile);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isNotOpponent(Tile tile) {
        /*
            A placed tile must not have contact with an opponent
         */
        Hive.Player currentPlayer = HiveGame.getGame().getCurrentPlayer();
        return tile.getPlayedByPlayer().equals(currentPlayer);
    }

    private void updateTileCountPlayed(Hive.Player player) {
        switch (player) {
            case WHITE:
                whiteTileCountPlayed += 1;
                break;
            case BLACK:
                blackTileCountPlayed += 1;
                break;
            default:
                System.out.println("Option " + player + " unknown reverting to WHITE");
                break;
        }
    }

    private void updateHasPlayedQueen(Hive.Player player, int q, int r) {
        switch (player) {
            case WHITE:
                whiteHasPlayedQueen = true;
                whiteQueenLocation.put("q",q);
                whiteQueenLocation.put("r",r);
                break;
            case BLACK:
                blackHasPlayedQueen = true;
                blackQueenLocation.put("q",q);
                blackQueenLocation.put("r",r);
                break;
            default:
                System.out.println("Option " + player + " unknown reverting to WHITE");
                break;
        }
    }

    public boolean checkHasPlayedQueen(Hive.Player player) {
        /*
            A player can only play 3 tiles before he must play the queen
         */
        boolean hasPlayedQueen;
        switch (player) {
            case WHITE:
                hasPlayedQueen = whiteHasPlayedQueen;
                break;
            case BLACK:
                hasPlayedQueen = blackHasPlayedQueen;
                break;
            default:
                System.out.println("Option " + player + " unknown reverting to WHITE");
                hasPlayedQueen = whiteHasPlayedQueen;
                break;
        }
        return hasPlayedQueen;
    }

    public boolean mustPlayQueen(Hive.Player player) {
        /*
            If a player has played 3 tiles and not yet the queen, the 4th tile must be the queen
         */
        if (!checkHasPlayedQueen(player)) { //Player has not played queen yet
            int tileCountPlayed;
            switch (player) {
                case WHITE:
                    tileCountPlayed = whiteTileCountPlayed;
                    break;
                case BLACK:
                    tileCountPlayed = blackTileCountPlayed;
                    break;
                default:
                    System.out.println("Option " + player + " unknown reverting to WHITE");
                    tileCountPlayed = whiteTileCountPlayed;
                    break;
            }
            return tileCountPlayed <= 3;
        }
        return false;
    }

    public HashMap<String, Integer> getQueenLocation(Hive.Player player) {
        switch (player) {
            case WHITE:
                return whiteQueenLocation;
            case BLACK:
                return blackQueenLocation;
            default:
                System.out.println("Option " + player + " unknown");
                return null;
        }
    }


}
