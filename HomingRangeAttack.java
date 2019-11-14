public abstract class HomingRangeAttack extends RangeAttack {
    
    public HomingRangeAttack(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public boolean finished(Point target) {
        return this.hitObstacle || this.getBounds().contains(target);
    }

    @Override
    public void next(Point target, java.util.Collection<Obstacle> obstacles) {
        super.setPath(new Line(this.getCentre(), target));
        Rectangle nextPos = this.getBounds().translate(this.direction);
        for (Obstacle o : obstacles) {
            if (o.getBounds().intersects(nextPos)) {
                this.hitObstacle = true;
                return;
            }
        }
        this.translate(this.direction);
    }
}
