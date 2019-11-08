public class Line {
    
    private Point p1, p2;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean intersects(Line l) {
        Point v1 = p1.getVectorTo(p2);
        Point v2 = l.toVector();
        double x00 = this.p1.getX(), y00 = this.p1.getY();
        double x10 = l.p1.getX(), y10 = l.p1.getY();
        double x01 = v1.getX(), y01 = v1.getY();
        double x11 = v2.getX(), y11 = v2.getY();
        double d = x11 * y01 - x01 * y11;
        if (Math.abs(d) < 1E-9) {
            return false;
        }
        double s = ((x00 - x10) * y01 - (y00 - y10) * x01) / d;
        double t = ((x00 - x10) * y11 - (y00 - y10) * x11) / d;
        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }

    public Point toVector() {
        return this.p1.getVectorTo(this.p2);
    }

    public Point getUnitVector() {
        return this.p1.getUnitVectorTo(p2);
    }

    public double length() {
        return p1.distance(p2);
    }
}
