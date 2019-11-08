import javax.swing.JPanel;

public abstract class MapElement {
    
    protected Point location;
    protected Rectangle bounds;
    protected JPanel panel;
    protected Point translationFromOriginal = new Point(0, 0);

    public MapElement(double x, double y, double width, double height) {
        this.panel = new JPanel();
        this.panel.setLayout(null); 
        this.location = new Point(x, y);
        this.bounds = new Rectangle(x, y, width, height);
        this.panel.setBounds(this.bounds.toAWT());
    }

    public void translate(Point p) {
        this.translate(p.getX(), p.getY());
    }

    public void translate(double x, double y) {
        this.location = this.location.translate(x, y);
        this.bounds = this.bounds.translate(x, y);
        this.panel.setLocation(this.location.toAWT());
        this.translationFromOriginal = this.translationFromOriginal.translate(x, y);
    }

    public void resetLocation() {
        double x = 0 - this.translationFromOriginal.getX();
        double y = 0 - this.translationFromOriginal.getY();
        this.location = this.location.translate(x, y);
        this.bounds = this.bounds.translate(x, y);
        this.panel.setLocation(this.location.toAWT());
        this.translationFromOriginal = new Point(0, 0);

    }

    public void setLocation(Point p) {
        this.location = p;
        this.bounds = this.bounds.setLocation(p.getX(), p.getY());
    }

    public Point getLocation() {
        return this.location;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public JPanel getJPanel() {
        this.panel.setLocation(this.location.toAWT());
        return this.panel;
    }

    public Point getTranslation() {
        return this.translationFromOriginal;
    }
}
