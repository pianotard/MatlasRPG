public class TestMap extends AbstractMap {
   
    private static final int WALL_WIDTH = 2;

    public TestMap() {
        super("TestMap");
        this.setDefaultSpawnPoint(150, 150);
        this.addObstacle("table", Obstacle.rectangle(200, 200, 80, 80));
        this.addObstacle("plant", Obstacle.circle(400, 500, 10));
        this.addObstacle("north_wall", Obstacle.rectangle(100, 98, 900, WALL_WIDTH));
        this.addObstacle("west_wall", Obstacle.rectangle(98, 100, WALL_WIDTH, 450));
        this.addObstacle("south_wall", Obstacle.rectangle(100, 550, 900, WALL_WIDTH));
        this.addObstacle("east_wall", Obstacle.rectangle(1000, 100, WALL_WIDTH, 450));
        this.addPortal("test_map_2", new Portal(700, 540, 100, 20, "test_map_2", 
                    new Point(305, 115)));
    }
}
