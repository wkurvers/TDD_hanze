package handlers;


import creatures.Tile;
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
}
