public class TestMapTwo extends AbstractMap {
    
    private static final int WALL_WIDTH = 4;

    public TestMapTwo() {
        super("TestMap 2");
        this.setDefaultSpawnPoint(150, 150);
        this.addObstacle("north_wall", Obstacle.rectangle(100, 100, 400, WALL_WIDTH));
        this.addObstacle("south_wall", Obstacle.rectangle(100, 450, 400, WALL_WIDTH));
        this.addObstacle("west_wall", Obstacle.rectangle(96, 100, WALL_WIDTH, 350));
        this.addObstacle("east_wall", Obstacle.rectangle(500, 100, WALL_WIDTH, 350));
        this.addObstacle("pillar", Obstacle.rectangle(200, 200, 100, 100));
        this.addPortal("test_map", new Portal(300, 90, 100, 20, "test_map", new Point(305, 455))
                .setExitMapTranslation(new Point(-400, -50)));
        this.setMobSpawnDelay(100);
        this.setMobCap(1);
        this.addMob("bowman", new BowmanMob(), new Point(320, 400));
        this.addMob("slime", new SlimeMob(), new Point(120, 400), new Point(200, 400));
    }
}
