package mazegame.server;

import mazegame.core.Map;
import mazegame.core.Tile;
import mazegame.core.Space;
import mazegame.core.Wall;
import mazegame.core.End;
import mazegame.core.Place;
import mazegame.core.Hero;

public class ServerSpecRandom implements ServerSpec {

    private Map map;
    private End end;
    private Hero hero;
    private int trailCapacity;

    public ServerSpecRandom(int rows, int cols, int trailCapacity) {
        if (rows <= 0) {
            throw new IllegalArgumentException("rows was <= 0");
        }
        if (cols <= 0) {
            throw new IllegalArgumentException("columns was <= 0");
        }
        if (trailCapacity < 0) {
            throw new IllegalArgumentException("trailCapacity < 0");
        }
        Tile[][] tiles = createTiles(rows, cols);
        this.map = new Map(tiles);
        this.end = new End(new Place(rows-1, cols-1, map));
        this.hero = new Hero(new Place(0, 0, map));
        this.trailCapacity = trailCapacity;
    }

    public Map generateMap() { return map; }
    public End generateEnd() { return end; }
    public Hero generateHero() { return hero; }
    public int generateTrailCapacity() { return trailCapacity; }

    public String toString() {
        return "RANDOM";
    }

    private Tile[][] createTiles(int rows, int cols) {
        Tile wall = new Wall();
        Tile space = new Space();
        Tile[][] tiles = new Tile[rows][cols];
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                tiles[r][c] = wall;
            }
        }
        tiles[0][0] = space;
        tiles[rows-1][cols-1] = space;
        return tiles;
    }
}
