import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;

public abstract class AbstractAbility extends MapElement {

    private static final int PADDING = 4;
    private static final int WIDTH = 300 - PADDING * 2;
    private static final int HEIGHT = 75;
    private static final int ICON_WIDTH = HEIGHT - PADDING * 4;
    private static final int ICON_HEIGHT = HEIGHT - PADDING * 4;
    private static final int ICON_PADDING = PADDING * 2;
    private static final int DRAG_WIDTH = 40;
    private static final int DRAG_HEIGHT = 40;
    private static final int TITLE_HEIGHT = 15;
    private static final Font NAME_FONT = new Font("Serif", Font.BOLD, 15);
    private static final Font DESC_FONT = new Font("Serif", Font.PLAIN, 12);

    private static final HTMLParser PARSER = new HTMLParser();

    private String name;
    private String key;
    private int mpCost = 0;

    private JLabel nameLabel = new JLabel();
    private JLabel iconLabel = new JLabel();
    private DragLabel dragLabel = new DragLabel(0, 0, DRAG_WIDTH, DRAG_HEIGHT);

    public AbstractAbility(String name) {
        super(0, 0, WIDTH, HEIGHT);
        this.label.setBounds(HEIGHT + PADDING * 2, TITLE_HEIGHT, WIDTH - HEIGHT - PADDING * 3, HEIGHT - PADDING * 2);
        this.label.setVerticalAlignment(JLabel.TOP);
        this.label.setText("ability description");
        this.label.setFont(DESC_FONT);
        this.name = name;
        this.nameLabel.setLayout(null);
        this.nameLabel.setBounds(HEIGHT + PADDING * 2, 0, WIDTH - HEIGHT - PADDING * 3, TITLE_HEIGHT);
        this.nameLabel.setText(name);
        this.panel.add(this.nameLabel);
        this.iconLabel.setLayout(null);
        this.iconLabel.setBounds(ICON_PADDING, ICON_PADDING, ICON_WIDTH, ICON_HEIGHT);
        this.panel.add(this.iconLabel);
    }

    public int getMPCost() {
        return this.mpCost;
    }

    protected void setMPCost(int set) {
        this.mpCost = set;
    }

    public DragLabel getDragLabel() {
        return this.dragLabel;
    }

    public void addDragListener(java.awt.event.MouseMotionListener mml) {
        this.panel.addMouseMotionListener(mml);
    }

    public void addDragListener(java.awt.event.MouseListener ml) {
        this.panel.addMouseListener(ml);
    }

    protected void setIcon(String path) {
        this.iconLabel.setIcon(UIUtil.readImageIcon(path, ICON_WIDTH, ICON_HEIGHT));
        this.dragLabel.getLabel().setIcon(UIUtil.readImageIcon(path, DRAG_WIDTH, DRAG_HEIGHT));
        this.dragLabel.setImgPath(path);
    }

    public String getKey() {
        return this.key;
    }

    protected void setKey(String key) {
        this.key = key;
        this.dragLabel.setKey(key);
    }

    protected void setDescription(String text) {
        this.label.setText(PARSER.parse(text));
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AbstractAbility) {
            AbstractAbility a = (AbstractAbility) o;
            return a.key.equals(this.key);
        }
        return false;
    }
}
