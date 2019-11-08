public class Portal extends MapElement {

    private String exitMapID;
    private Point spawn = new Point(150, 150);
    private Point exitMapTranslation = new Point(0, 0);

    public Portal(double x, double y, double width, double height, String exitMapID, Point spawn) {
        super(x, y, width, height);
        this.panel.setBackground(java.awt.Color.PINK);
        this.exitMapID = exitMapID;
        this.spawn = spawn;
    }

    public Point getExitMapTranslation() {
        return this.exitMapTranslation;
    }

    public Portal setExitMapTranslation(Point translation) {
        this.exitMapTranslation = translation;
        return this;
    }

    public String getExitMapID() {
        return this.exitMapID;
    }

    public Point getSpawn() {
        return this.spawn;
    }
}
