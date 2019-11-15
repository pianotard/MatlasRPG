import java.util.Optional;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public abstract class UtilWindow extends MapElement {
    
    private static final int PADDING = 4;
    protected static final int TOP_BAR_H = 20;

    private static final java.awt.Font HEADER_FONT = new java.awt.Font("Serif", java.awt.Font.PLAIN, 15);

    private String name;
    private javax.swing.JPanel draggable = new javax.swing.JPanel();
    private Point clicked = new Point(0, 0);

    protected Player player;
    protected javax.swing.JPanel content = new javax.swing.JPanel();
    protected AbstractMap map;
    protected Optional<DragLabel> dragLabel = Optional.empty();

    public UtilWindow(Player player, String name, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.name = name;
        this.player = player;
        this.draggable.setLayout(null);
        this.draggable.setBackground(java.awt.Color.YELLOW);
        this.draggable.setBounds(PADDING, PADDING, (int) width - PADDING * 2, TOP_BAR_H);
        this.draggable.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {
                UtilWindow.this.unclickTitle();
            }
            public void mousePressed(MouseEvent e) {
                UtilWindow.this.setClickedTitle(new Point(e.getX(), e.getY()));
            }
        });
        this.draggable.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {}
            public void mouseDragged(MouseEvent e) {
                UtilWindow.this.translate(new Point(e.getX(), e.getY()));
            }
        });
        javax.swing.JLabel title = new javax.swing.JLabel();
        title.setLayout(null);
        title.setFont(HEADER_FONT);
        title.setText(name);
        title.setBounds(PADDING, 0, (int) width - PADDING * 2, TOP_BAR_H);
        this.draggable.add(title);
        this.panel.add(this.draggable);
        this.content.setLayout(null);
        this.content.setBounds(PADDING, TOP_BAR_H + PADDING * 2, (int) width - PADDING * 2, (int) height - TOP_BAR_H - PADDING * 2 - 5);
        this.content.setBackground(java.awt.Color.YELLOW);
        this.panel.add(this.content);
    }

    public abstract void refresh();

    public void removeDragIconFromMap(DragLabel icon) {
        this.dragLabel = Optional.empty();
        this.map.remove(icon.getJPanel());
    }

    public void showDragIconOnMap(Point p, DragLabel icon) {
        this.dragLabel = Optional.of(icon);
        icon.setCentre(p);
        this.map.showDragIcon(icon.getJPanel());
    }

    public void setClickedTitle(Point p) {
        this.clicked = p;
    }

    public void unclickTitle() {
        this.clicked = new Point(0, 0);
    }

    public void removeDragLabel() {
        this.dragLabel.ifPresent(dl -> this.map.remove(dl.getJPanel()));
    }

    public void setMap(AbstractMap map) {
        this.map = map;
    }

    @Override
    public void translate(Point p) {
        Point trans = this.clicked.getVectorTo(p);
        super.translate(trans);
    }

    @Override
    public String toString() {
        return "Util window: " + this.name;
    }
}
