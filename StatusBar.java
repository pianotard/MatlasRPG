public class StatusBar extends MapElement {
   
    private static final int WIDTH = 400;
    private static final int HEIGHT = 80;

    private HP hp;
    private MP mp;

    public StatusBar(HP hp, MP mp) {
        super(200, 520, WIDTH, HEIGHT);
        this.hp = hp;
        this.mp = mp;
        this.panel.setBackground(java.awt.Color.GRAY);
        this.panel.add(this.hp.getJPanel());
        this.panel.add(this.mp.getJPanel());
    }

    @Override
    public String toString() {
        return this.hp + "\n" + this.mp;
    }
}
