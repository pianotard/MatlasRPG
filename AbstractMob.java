import java.math.BigInteger;
import java.util.Optional;

public abstract class AbstractMob extends MapElement {
   
    private static final java.util.Random RNG = new java.util.Random();

    private String name;

    private double speed = 1;
    private double detectionRadius = 100;
    private double knockBack = 20;
    private int bodyDamage = 5;

    private Optional<Point> randomDirection = Optional.empty();
    private Optional<Point> playerDirection = Optional.empty();

    public AbstractMob(String name, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.name = name;
        this.panel.setBackground(java.awt.Color.MAGENTA);
    }

    public abstract AbstractMob clone();

    public Point getPlayerDirection() {
        return this.playerDirection.orElse(new Point(0, 0));
    }

    public void clearPlayerDirectionIfPresent() {
        if (this.playerDirection.isEmpty()) {
            return;
        }
        this.playerDirection = Optional.empty();
    }

    public void setPlayerDirection(Point playerLoc) {
        this.playerDirection = Optional
            .of(new Line(this.location, playerLoc).getUnitVector().scale(this.speed));
    }

    public Point getRandomDirection() {
        return this.randomDirection.orElse(new Point(0, 0));
    }

    public void changeRandomDirection() {
        this.randomDirection = Optional.of(new Line(this.location, this.generateRandomPoint())
                .getUnitVector().scale(this.speed));
    }

    public void initRandomDirectionIfAbsent() {
        this.randomDirection = this.randomDirection
            .or(() -> Optional
                    .of(new Line(this.location, this.generateRandomPoint())
                        .getUnitVector().scale(this.speed)));
    }

    private Point generateRandomPoint() {
        double dx = RNG.nextInt((int) (this.detectionRadius * 2 + 1)) - this.detectionRadius;
        double dy = RNG.nextInt((int) (this.detectionRadius * 2 + 1)) - this.detectionRadius;
        return this.location.translate(dx, dy);
    }

    public int getBodyDamage() {
        return this.bodyDamage;
    }

    protected void setBodyDamage(int dmg) {
        this.bodyDamage = dmg;
    }

    public double getKnockBack() {
        return this.knockBack;
    }

    protected void setKnockBack(double kb) {
        this.knockBack = kb;
    }

    public double getDetectionRadius() {
        return this.detectionRadius;
    }

    protected void setDetectionRadius(double radius) {
        this.detectionRadius = radius;
    }

    public double getSpeed() {
        return this.speed;
    }

    protected void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Mob: " + this.name + " @ " + this.location + " (" + this.bounds + ")";
    }
}
