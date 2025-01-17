import java.util.List;
import java.util.ArrayList;

public class MP extends MapElement {
    
    private static final int WIDTH = 390;
    private static final int HEIGHT = 6;

    private int current, max;
    private List<Integer> buffs = new ArrayList<>();
    private javax.swing.JPanel mpPanel;

    public MP(int max) {
        super(5, 14, WIDTH, HEIGHT);
        this.current = max;
        this.max = max;
        this.mpPanel = new javax.swing.JPanel();
        this.mpPanel.setLayout(null);
        this.mpPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.mpPanel.setBackground(java.awt.Color.BLUE);
        this.panel.add(mpPanel);
    }

    public void healPercent(double percent) {
        this.current += (int) (percent * this.max);
        this.current = Math.min(this.current, this.max);
        this.refresh();
    }

    public void deductMP(int foo) {
        this.current -= foo;
        this.current = Math.max(this.current, 0);
        this.refresh();
    }

    public void buff(MP mp) {
        this.buffs.add(mp.max);
        this.refresh();
    }

    public void mutate(MP mp) {
        this.max += mp.max;
        this.refresh();
    }

    public int getCurrent() {
        return this.current;
    }

    private void refresh() {
        double percentage = (current + 0.0) / (max + buffs.stream().mapToInt(x -> x).sum());
        this.mpPanel.setSize((int) (percentage * WIDTH), HEIGHT); 
    }

    public String description() {
        return current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }

    @Override
    public String toString() {
        return "MP: " + current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }
}
