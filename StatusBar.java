public class StatusBar extends MapElement {
   
    private static final int WIDTH = 400;
    private static final int HEIGHT = 80;

    private HP hp;

    public StatusBar(HP hp) {
        super(200, 520, WIDTH, HEIGHT);
        this.hp = hp;
        this.panel.setBackground(java.awt.Color.GRAY);
        this.panel.add(this.hp.getJPanel());
    }

    @Override
    public String toString() {
        return this.hp + "";
    }
}
