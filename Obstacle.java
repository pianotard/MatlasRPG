public abstract class Obstacle extends MapElement {

    public Obstacle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public static Obstacle rectangle(double x, double y, double width, double height) {
        if (width <= 0 || height <= 0) {
            System.out.println("Obstacle: Rectangle: Negative dimensions provided!");
            return null;
        }
        return new RectangleObstacle(x, y, width, height);
    }

    public static Obstacle circle(double x, double y, double radius) {
        if (radius <= 0) {
            System.out.println("Obstacle: Circle: negative dimensions provided!");
            return null;
        }
        return new CircleObstacle(x, y, radius);
    }

    public boolean intersects(Line l) {
        Line top = new Line(this.bounds.getTopLeft(), this.bounds.getTopRight());
        if (top.intersects(l)) {
            return true;
        }
        Line left = new Line(this.bounds.getTopLeft(), this.bounds.getBtmLeft());
        if (left.intersects(l)) {
            return true;
        }
        Line right = new Line(this.bounds.getTopRight(), this.bounds.getBtmRight());
        if (right.intersects(l)) {
            return true;
        }
        Line btm = new Line(this.bounds.getBtmLeft(), this.bounds.getBtmRight());
        if (btm.intersects(l)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Obstacle: " + this.bounds;
    }
}

class CircleObstacle extends Obstacle {
    
    private double x, y, radius;

    public CircleObstacle(double x, double y, double radius) {
        super(x - radius / 2, y - radius / 2, radius, radius);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.panel.setBackground(java.awt.Color.CYAN);
    }
}

class RectangleObstacle extends Obstacle {
    
    private double x, y, width, height;

    public RectangleObstacle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panel.setBackground(java.awt.Color.RED);
    }
}
