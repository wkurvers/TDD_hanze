package game;

import creatures.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileFactoryTest {

    @Test
    void testCanCreateSoldierAnt(){
        Tile createdTile = TileFactory.makeTile(Hive.Tile.SOLDIER_ANT, Hive.Player.BLACK);
        assertEquals(Hive.Tile.SOLDIER_ANT,createdTile.getCreature());
    }

    @Test
    void testCanCreateBeetle(){
        Tile createdTile = TileFactory.makeTile(Hive.Tile.BEETLE, Hive.Player.BLACK);
        assertEquals(Hive.Tile.BEETLE,createdTile.getCreature());
    }

    @Test
    void testCanCreateGrasshopper(){
        Tile createdTile = TileFactory.makeTile(Hive.Tile.GRASSHOPPER, Hive.Player.BLACK);
        assertEquals(Hive.Tile.GRASSHOPPER,createdTile.getCreature());
    }

    @Test
    void testCanCreateQueenBee(){
        Tile createdTile = TileFactory.makeTile(Hive.Tile.QUEEN_BEE, Hive.Player.BLACK);
        assertEquals(Hive.Tile.QUEEN_BEE,createdTile.getCreature());
    }

    @Test
    void testCanCreateSpider(){
        Tile createdTile = TileFactory.makeTile(Hive.Tile.SPIDER, Hive.Player.BLACK);
        assertEquals(Hive.Tile.SPIDER,createdTile.getCreature());
    }

}