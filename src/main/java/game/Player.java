package game;

import java.util.ArrayList;

public class Player {
    private ArrayList<Tile> tilesToPlay;
    private ArrayList<Tile> playedTiles;

    public Player() {
        this.tilesToPlay = new ArrayList<Tile>();
        tilesToPlay.add(new Tile("queen"));
        tilesToPlay.add(new Tile("spider"));
        tilesToPlay.add(new Tile("spider"));
        tilesToPlay.add(new Tile("beetle"));
        tilesToPlay.add(new Tile("beetle"));
        tilesToPlay.add(new Tile("soldierAnts"));
        tilesToPlay.add(new Tile("soldierAnts"));
        tilesToPlay.add(new Tile("soldierAnts"));
        tilesToPlay.add(new Tile("grasshopper"));
        tilesToPlay.add(new Tile("grasshopper"));
        tilesToPlay.add(new Tile("grasshopper"));
    }

    public void play(Tile tile) {
        try {
            removeTileFromTilesToPlay(tile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // rest of the move
    }

    public ArrayList<Tile> getTilesToPlay() {
        return tilesToPlay;
    }

    public ArrayList<Tile> getPlayedTiles() {
        return playedTiles;
    }

    private Boolean removeTileFromTilesToPlay(String name) throws Exception {
        for(Tile tile :tilesToPlay){
            if (name.equals(tile.getName())){
                tilesToPlay.remove(tile);
                return true;
            }
        }
        throw new Exception("could not remove tilesToPlay because tile did not exist");

    }
}
