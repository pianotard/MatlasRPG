public abstract class RangeAttack extends AbstractAttack {
    
    private Point direction = new Point(0, 0);
    private double moved = 0;
    private double distance = 1;
    private double speed = 5;

    public RangeAttack(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void next() {
        this.moved += this.speed;
        this.translate(this.direction);
    }

    @Override
    public boolean finished() {
        return this.moved > this.distance;
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
}
