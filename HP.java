public class HP extends MapElement {
    
    private static final int WIDTH = 390;
    private static final int HEIGHT = 6;

    private int current, max;
    private javax.swing.JPanel hpPanel;

    public HP(int max) {
        super(5, 5, WIDTH, HEIGHT);
        this.current = max;
        this.max = max;
        this.hpPanel = new javax.swing.JPanel();
        this.hpPanel.setLayout(null);
        this.hpPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.hpPanel.setBackground(java.awt.Color.RED);
        this.panel.add(hpPanel);
    }

    public void inflictDamage(int damage) {
        this.current -= damage;
        this.current = Math.max(this.current, 0);
        double percentage = (this.current + 0.0) / this.max;
        this.hpPanel.setSize((int) (percentage * WIDTH), HEIGHT);
    }

    @Override
    public String toString() {
        return "HP: " + current + " / " + max;
    }
}
