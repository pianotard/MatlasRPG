public abstract class MeleeAttack extends AbstractAttack {
    
    public MeleeAttack(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void next(Point attack, java.util.Collection<Obstacle> obstacles) {
        this.label.setIcon(this.icons.get(nowShowingName)[this.nowShowingIndex++]);
    }

    @Override
    public boolean finished(Point target) {
        return this.nowShowingIndex >= this.getNumberIcons();
    }

    @Override
    public int getAttackInterval() {
        return this.attackDuration / this.icons.get(nowShowingName).length;
    }

    @Override
    public AbstractAttack setPath(Line path) {
        super.setPath(path);
        int degree = path.getAngleDegrees() - 45;
        if (degree < 90) {
            this.setImages("right");
        } else if (degree < 180) {
            this.setImages("down");
        } else if (degree < 270) {
            this.setImages("left");
        } else {
            this.setImages("up");
        }
        this.setCentre(this.getTarget());
        return this;
    }
}
