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

    public void deductMP(int foo) {
        this.current -= foo;
        this.current = Math.max(this.current, 0);
        double percentage = (this.current + 0.0) / this.max;
        this.mpPanel.setSize((int) (percentage * WIDTH), HEIGHT);
    }

    public void buff(MP mp) {
        this.buffs.add(mp.max);
    }

    public void mutate(MP mp) {
        this.max += mp.max;
    }

    public int getCurrent() {
        return this.current;
    }

    public String description() {
        return current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }

    @Override
    public String toString() {
        return "MP: " + current + " / " + (max + buffs.stream().mapToInt(x -> x).sum());
    }
}
