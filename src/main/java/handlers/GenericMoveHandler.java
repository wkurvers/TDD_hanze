package handlers;


import game.Hive;

public class GenericMoveHandler {
    private static GenericMoveHandler instance;
    private Hive.Player player;
    /*
    This class implements all the generic requirements when moving a tile
    */
    public static GenericMoveHandler getGenericMoveHandler(Hive.Player player){
        if (instance == null) {
            instance = new GenericMoveHandler(player);
        }
        return instance;
    }

    private GenericMoveHandler(Hive.Player player) {
        this.player = player;
    }

    private void getGameContext() {
        /*
        Retrieves the current game context, as in the board
        */
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

    protected boolean valideCreatureSpecific() {
        /*
        Each creature has its own moving constraints
         */
        return true;
    }

    public boolean canMakeMove(int fromQ, int fromR, int toQ, int toR) {
        return true;
    }
}
