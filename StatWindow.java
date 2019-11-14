import java.util.function.Supplier;
import javax.swing.JLabel;

public class StatWindow extends UtilWindow {
    
    private static final int PADDING = 4;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 450;
    private static final int NAME_HEIGHT = 20;
    private static final int CONTENT_WIDTH = WIDTH - PADDING * 2;
    private static final int CONTENT_HEIGHT = HEIGHT - PADDING * 2 - 5;

    private static final java.awt.Font FONT = new java.awt.Font("Serif", java.awt.Font.PLAIN, 18);

    private JLabel nameLabel = new JLabel();
    private Supplier<String> nameSupp;
    private JLabel statLabel = new JLabel();
    private Supplier<String> statSupp;

    public StatWindow(Player player) {
        super(player, "Player Statistics", 30, 30, WIDTH, HEIGHT);
        this.nameSupp = () -> this.player.getName();
        this.statSupp = () -> this.player.getStatString();
        this.initLabel(nameLabel, PADDING, PADDING, CONTENT_WIDTH, NAME_HEIGHT, nameSupp);
        this.initLabel(statLabel, PADDING, NAME_HEIGHT + PADDING, CONTENT_WIDTH, CONTENT_HEIGHT - NAME_HEIGHT, statSupp);
        this.statLabel.setVerticalAlignment(JLabel.TOP);
    }

    @Override
    public void refresh() {
        this.nameLabel.setText(nameSupp.get());
        this.statLabel.setText(statSupp.get());
    }

    private void initLabel(JLabel label, int x, int y, int width, int height, Supplier<String> s) {
        label.setLayout(null);
        label.setBounds(x, y, width, height);
        label.setText(s.get());
        label.setFont(FONT);
        this.content.add(label);
    }
}
