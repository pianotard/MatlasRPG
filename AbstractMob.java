import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public abstract class AbstractMob extends MapElement {
   
    private static final java.util.Random RNG = new java.util.Random();

    private String name;

    private boolean attacking = false;
    private double speed = 1;
    private double detectionRadius = 200;
    private double attackRadius = 20;
    private int attackDelayMS = 2000;
    private AbstractAttack attack;

    private Optional<Point> randomDirection = Optional.empty();
    private Optional<Point> playerDirection = Optional.empty();

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private AttackTimerRunner attackTimer = new AttackTimerRunner(this);

    public AbstractMob(String name, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.name = name;
        this.panel.setBackground(java.awt.Color.MAGENTA);
    }

    public abstract AbstractMob clone();

    public Point getPlayerDirection() {
        return this.playerDirection.orElse(new Point(0, 0));
    }

    public void setPlayerDirection(Point playerCentre) {
        this.playerDirection = Optional
            .of(new Line(this.getCentre(), playerCentre).getUnitVector().scale(this.getSpeed()));
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public void setAttacking(boolean set) {
        this.attacking = set;
        if (set) {
            this.executor.submit(this.attackTimer);
        }
    }

    public boolean targetSighted(Point loc) {
        return this.bounds.getCentre().distance(loc) <= this.attackRadius;
    }

    public void clearPlayerDirectionIfPresent() {
        if (this.playerDirection.isEmpty()) {
            return;
        }
        this.playerDirection = Optional.empty();
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

    public AbstractAttack getAttack() {
        // modify location before return
        return this.attack.clone();
    }

    protected void setAttack(AbstractAttack attack) {
        this.attack = attack;
    }

    public double getAttackRadius() {
        return this.attackRadius;
    }

    protected void setAttackRadius(double set) {
        this.attackRadius = set;
    }

    public int getAttackDelayMS() {
        return this.attackDelayMS;
    }

    protected void setAttackDelayMS(int delay) {
        this.attackDelayMS = delay;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Mob: " + this.name + " @ " + this.location + ", ");
        builder.append("Spd: " + this.speed + ", ");
        builder.append("AtkRad: " + this.attackRadius + ", ");
        builder.append("DetectRad: " + this.detectionRadius + ", AtkDelay: " + this.attackDelayMS);
        builder.append("ms");
        return builder + "";
    }
}

class AttackTimerRunner extends PausableRunner {
    
    private AbstractMob mob;

    public AttackTimerRunner(AbstractMob mob) {
        this.mob = mob;
    }

    @Override
    public void run() {
        this.pause(mob.getAttackDelayMS());
        this.mob.setAttacking(false);
    }
}
