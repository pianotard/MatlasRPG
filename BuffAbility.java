import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;

public abstract class BuffAbility extends AbstractAbility {

    private static final int ACTIVE_WIDTH = 40;
    private static final int ACTIVE_HEIGHT = 40;

    private int durationMS = 5000;
    private Statistics buff;
    private int activeNowShowing = 0;
    private List<ImageIcon> activeLabelIcons = new ArrayList<>();
    private JLabel activeLabel = new JLabel();

    public BuffAbility(String name) {
        super(name);
        this.activeLabel.setLayout(null);
        this.activeLabel.setBounds(0, 0, ACTIVE_WIDTH, ACTIVE_HEIGHT);
    }

    public Statistics getBuff() {
        return this.buff;
    }

    public void cascade() {
        if (this.activeNowShowing > this.activeLabelIcons.size()) {
            System.out.println("less than 10 active icons!");
            return;
        }
        this.activeLabel.setIcon(this.activeLabelIcons.get(this.activeNowShowing++));
    }

    public void reset() {
        this.activeNowShowing = 0;
        this.activeLabel.setIcon(this.activeLabelIcons.get(0));
    }

    public JLabel getActiveLabel() {
        return this.activeLabel;
    }

    public int getDurationMS() {
        return this.durationMS;
    }

    protected void setDurationS(int set) {
        this.durationMS = set * 1000;
    }

    protected void setDurationMS(int set) {
        this.durationMS = set;
    }

    protected void setBuff(Statistics set) {
        this.buff = set;
    }

    protected void addCascadeIcon(String path) {
        this.activeLabelIcons.add(UIUtil.readImageIcon(path, ACTIVE_WIDTH, ACTIVE_HEIGHT));
    }

    @Override
    protected void setIcon(String path) {
        super.setIcon(path);
        this.activeLabel.setIcon(UIUtil.readImageIcon(path, ACTIVE_WIDTH, ACTIVE_HEIGHT));
    }
}
