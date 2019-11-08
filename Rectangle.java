public class Rectangle {
    
    private double x, y, width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle setLocation(double x, double y) {
        return new Rectangle(x, y, this.width, this.height);
    }

    public Rectangle translate(Point p) {
        return this.translate(p.getX(), p.getY());
    }

    public Rectangle translate(double x, double y) {
        return new Rectangle(this.x + x, this.y + y, this.width, this.height);
    }

    public boolean intersects(Rectangle r) {
        Point r1 = this.getBtmRight();
        Point r2 = r.getBtmRight();
        if (x > r2.getX() || r.x > r1.getX()) {
            return false;
        }
        if (y > r2.getY() || r.y > r1.getY()) {
            return false;
        }
        return true;
    }

    public boolean contains(Rectangle r) {
        return this.contains(r.getTopLeft()) && this.contains(r.getBtmRight());
    }

    public boolean contains(Point p) {
        return p.getX() >= x && p.getX() <= x + width && p.getY() >= y && p.getY() <= y + height;
    }

    public Point getCentre() {
        return new Point(x + width / 2, y + height / 2);
    }

    public Point getTopRight() {
        return new Point(x + width, y);
    }

    public Point getTopLeft() {
        return new Point(x, y);
    }

    public Point getBtmRight() {
        return new Point(x + width, y + height);
    }

    public Point getBtmLeft() {
        return new Point(x, y + height);
    }

    public java.awt.Rectangle toAWT() {
        return new java.awt.Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public String toString() {
        String dimension = String.format("%.1f", width) + " x " + String.format("%.1f", height);
        return "Rectangle at " + new Point(x, y) + " with dimensions " + dimension;
    }
}
