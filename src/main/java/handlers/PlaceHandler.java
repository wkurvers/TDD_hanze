package handlers;

import game.Hive;

public class PlaceHandler {
    private static PlaceHandler instance;
    private Hive.Player player;
    /*
    This class implements all the requirements when placing a tile
    */
    public static PlaceHandler getPlaceHandler(Hive.Player player){
        if (instance == null) {
            instance = new PlaceHandler(player);
        }
        return instance;
    }

    private PlaceHandler(Hive.Player player) {
        this.player = player;
    }

    private void getGameContext() {

    }

    protected boolean checkCanPlayTile() {
        return  checkTileInStock() &&
                checkLocationEmpty() &&
                checkContactExists() &&
                checkNotNextToOpponent() &&
                checkHasPlayedQueen() &&
                mustPlayQueen();
    }

    protected boolean checkTileInStock() {
        /*
            A player can only play tiles he hasn't placed yet
         */
        return true;
    }

    protected boolean checkLocationEmpty() {
        /*
            A tile can only be placed on an empty location
         */
        return true;
    }

    protected boolean checkContactExists() {
        /*
            A player can only play tiles if the placed tile has contact with other tiles, but this must not be an opponent tile
         */
        checkNotNextToOpponent();
        return true;
    }

    private boolean checkNotNextToOpponent() {
        /*
            A placed tile must not have contact with an opponent
         */
        return true;
    }

    protected boolean checkHasPlayedQueen() {
        /*
            A player can only play 3 tiles before he must play the queen
         */
        return true;
    }

    private boolean mustPlayQueen() {
        /*
            If a player has played 3 tiles and not yet the queen, the 4th tile must be the queen
         */
        checkHasPlayedQueen();
        return true;
    }

}
