package handlers;


import game.Hive;

public class GenericMoveHandler {
    private static GenericMoveHandler playerWhite;
    private static GenericMoveHandler playerBlack;
    private Hive.Player player;
    /*
    This class implements all the generic requirements when moving a tile, each instance is unique to one player (BLACK or WHITE)
    */
    public static GenericMoveHandler getGenericMoveHandler(Hive.Player player){
        if (player == Hive.Player.WHITE) {
            if (playerWhite == null) {
                playerWhite = new GenericMoveHandler(Hive.Player.WHITE);
            }
            return playerWhite;
        } else if (player == Hive.Player.BLACK) {
            if (playerBlack == null) {
                playerBlack = new GenericMoveHandler(Hive.Player.BLACK);
            }
            return playerBlack;
        }
        return null;
    }

    private GenericMoveHandler(Hive.Player player) {
        this.player = player;
    }

    private void getGameContext() {
        /*
        Retrieves the current game context, as in the board
        */
    }

    protected Boolean validateQueenBee() {
        /*
        Tiles can only be moved ones the queen bee of that player has been placed
         */
        return true;
    }

    protected Boolean validateContact() {
        /*
        Tiles can only be moved if after the move they're still in contact with atleast one other tile
         */
        return true;
    }

    protected Boolean validateNoIslands() {
        /*
        If a move results into 2 or more seperate 'islands' of tiles it is invalid
         */
        return true;
    }

    protected Boolean valideCreatureSpecific() {
        /*
        Each creature has its own moving constraints
         */
        return true;
    }

    public Boolean canMakeMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
