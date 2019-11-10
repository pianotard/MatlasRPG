public class Point {
    
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point toUnitVector() {
        double dist = new Point(0, 0).distance(this);
        if (Math.abs(dist) < 1E-9) {
            return new Point(0, 0);
        }
        return new Point(x / dist, y / dist);
    }

    public Point getVectorTo(Point p) {
        return new Point(p.x - this.x, p.y - this.y);
    }

    public Point getUnitVectorTo(Point p) {
        double dx = p.x - x;
        double dy = p.y - y;
        double dist = this.distance(p);
        return new Point(dx / dist, dy / dist);
    }

    public Point scale(double scale) {
        return new Point(x * scale, y * scale);
    }

    public Point translate(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    public java.awt.Point toAWT() {
        return new java.awt.Point((int) Math.round(x), (int) Math.round(y));
    }

    public double distance(Point p) {
        double dx = p.getX() - this.x;
        double dy = p.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int degreesTo(Point p) {
        return (int) Math.toDegrees(Math.atan2(p.y - y, p.x - x));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Point) {
            Point p = (Point) o;
            return p.toAWT().equals(this.toAWT());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%.1f", x) + ", " + String.format("%.1f", y);
    }
}
