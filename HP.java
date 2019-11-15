import java.util.List;
import java.util.ArrayList;

public class HP extends MapElement {
    
    private static final int WIDTH = 390;
    private static final int HEIGHT = 6;

    private int current, max;
    private List<Integer> buffs = new ArrayList<>();
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

    public void healPercent(double percent) {
        this.current += (int) (percent * this.max);
        this.current = Math.min(this.current, this.max);
        this.refresh();
    }

    public boolean isDead() {
        return this.current == 0;
    }

    public void buff(HP hp) {
        this.buffs.add(hp.max);
        this.refresh();
    }

    public void mutate(HP hp) {
        this.max += hp.max;
        this.refresh();
    }

    public void inflictDamage(int damage) {
        this.current -= damage;
        this.current = Math.max(this.current, 0);
        this.refresh();
    }

    public void refresh() {
        double percentage = (current + 0.0) / (max + buffs.stream().mapToInt(x -> x).sum());
        this.hpPanel.setSize((int) (percentage * WIDTH), HEIGHT); 
    }

    public String description() {
        return current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }

    @Override
    public String toString() {
        return "HP: " + current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }
}
