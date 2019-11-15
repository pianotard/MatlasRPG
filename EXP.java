public class EXP extends MapElement {
    
    private static final int WIDTH = 390;
    private static final int HEIGHT = 6;

    private int current, max;
    private javax.swing.JPanel expPanel;

    public EXP(int current, int max) {
        super(5, 23, WIDTH, HEIGHT);
        this.current = current;
        this.max = max;
        this.expPanel = new javax.swing.JPanel();
        this.expPanel.setLayout(null);
        double percentage = (this.current + 0.0) / this.max;
        this.expPanel.setBounds(0, 0, (int) (percentage * WIDTH), HEIGHT);
        this.expPanel.setBackground(java.awt.Color.YELLOW);
        this.panel.add(expPanel);
    }

    public void levelUp(int max) {
        this.current = 0;
        this.max = max;
        this.expPanel.setSize(0, HEIGHT);
    }

    public int rewardEXP(int reward) {
        this.current += reward;
        double percentage = (this.current + 0.0) / this.max;
        this.expPanel.setSize((int) (percentage * WIDTH), HEIGHT);
        return Math.max(0, this.current - this.max);
    }

    public String description() {
        double percentage = 100 * (this.current + 0.0) / this.max;
        return current + " / " + max + " (" + String.format("%.2f", percentage) + "%)";
    }
}
