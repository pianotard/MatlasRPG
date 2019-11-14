import javax.swing.ImageIcon;

public abstract class RangeAttack extends AbstractAttack {
    
    protected Point direction = new Point(0, 0);
    protected boolean hitObstacle = false;

    private double moved = 0;
    private double distance = 1;
    private double speed = 5;

    public RangeAttack(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void next(Point target, java.util.Collection<Obstacle> obstacles) {
        Rectangle nextPos = this.getBounds().translate(this.direction);
        for (Obstacle o : obstacles) {
            if (o.getBounds().intersects(nextPos)) {
                this.hitObstacle = true;
                return;
            }
        }
        this.moved += this.speed;
        this.translate(this.direction);
    }

    @Override
    public boolean finished(Point target) {
        return this.moved > this.distance || this.hitObstacle;
    }

    @Override
    public int getAttackInterval() {
        return (int) (this.attackDuration / this.speed);
    }

    @Override
    public AbstractAttack setPath(Line path) {
        super.setPath(path);
        this.setCentre(this.source);
        this.direction = path.getUnitVector().scale(this.speed);
        this.distance = path.length();
        this.setAttackDuration((int) (this.distance / this.speed));
        this.setImages(path.getAngleDegrees() + "");
        return this;
    }

    public double getSpeed() {
        return this.speed;
    }

    protected void setSpeed(double spd) {
        this.speed = spd;
    }

    protected void setImages(ImageIcon[] icons) {
        for (int i = 0; i < 360; i++) {
            super.setImages(i + "", icons[i]);
        }
    }
}
