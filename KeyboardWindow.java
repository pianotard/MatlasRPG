import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.function.Supplier;
import java.awt.event.MouseEvent;

public class KeyboardWindow extends UtilWindow {

    private Keyboard keyboard;
    private Map<Rectangle, String> locationMap = new HashMap<>();
    private Map<String, DragLabel> placedLabels = new HashMap<>();

    public KeyboardWindow(Player player, Keyboard keyboard) {
        super(player, "Keyboard Configuration", 45, 45, 600, 250);
        this.keyboard = keyboard;
        this.initKeys();
    }

    public void attemptBind(DragLabel dl) {
        if (dl.getJPanel().getMouseListeners().length < 1) {
            this.addListener(dl);
        }
        Point contentLoc = new Point(this.content.getX(), this.content.getY());
        Optional<Rectangle> found = this.locationMap.keySet().stream()
            .filter(r -> r.translate(this.location).translate(contentLoc).intersects(dl.getBounds()))
            .max((r1, r2) -> {
                int ri1 = (int) (r1
                    .translate(this.location)
                    .translate(contentLoc).intersectArea(dl.getBounds()));
                int ri2 = (int) (r2
                    .translate(this.location)
                    .translate(contentLoc).intersectArea(dl.getBounds()));
                return ri1 - ri2;
            });
        found.ifPresentOrElse(r -> {
            String key = this.locationMap.get(r);
            this.keyboard.bindAbility(key, dl.getKey(), dl.getImgPath());
            this.keyboard.refreshMapBindings();
            this.content.remove(this.placedLabels.get(key).getJPanel());
            Optional<String> oldKey = this.placedLabels.keySet().stream()
                .filter(k -> this.placedLabels.get(k).equals(dl))
                .findAny();
            oldKey.ifPresent(k -> this.replaceWithDefault(k));
            this.placedLabels.replace(key, dl);
            Rectangle bounds = this.locationMap.entrySet().stream()
                .filter(es -> es.getValue().equals(key))
               .findAny()
                .get().getKey();
            dl.setBounds(bounds);
            dl.setActionKey(key);
            this.content.add(dl.getJPanel());
            this.content.repaint();
            }, () -> System.out.println("no key found"));
    }

    @Override
    public void refresh() {}

    private void addListener(DragLabel dl) {
        dl.getJPanel().addMouseListener(new java.awt.event.MouseListener() {
            public void mousePressed(MouseEvent e) {
                Point click = new Point(e.getX(), e.getY())
                    .translate(KeyboardWindow.this.location)
                    .translate(dl.getLocation())
                    .translate(5, 25);
                KeyboardWindow.this.showDragIconOnMap(click, dl);
                KeyboardWindow.this.replaceWithDefault(dl.getActionKey());
            }
            public void mouseReleased(MouseEvent e) {
                KeyboardWindow.this.removeDragIconFromMap(dl);
                KeyboardWindow.this.attemptBind(dl);
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        dl.getJPanel().addMouseMotionListener(new java.awt.event.MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {}
            public void mouseDragged(MouseEvent e) {
                Point drag = new Point(e.getX(), e.getY())
                    .translate(KeyboardWindow.this.location)
                    .translate(dl.getLocation())
                    .translate(-30, -40);
                dl.setCentre(drag);
            }
        });

    }

    private void replaceWithDefault(String key) {
        this.keyboard.unbindKey(key);
        this.content.remove(this.placedLabels.get(key).getJPanel());
        DragLabel replacement = new DragLabel(key);
        Rectangle bounds = this.locationMap.entrySet().stream()
            .filter(es -> es.getValue().equals(key))
            .findAny()
            .get().getKey();
        replacement.setBounds(bounds);
        this.content.add(replacement.getJPanel());
        this.placedLabels.replace(key, replacement);
    }

    private void initRow(double x, double y, String[] order) {
        Map<String, PlayerAction> bindings = this.keyboard.getFreeBindings();
        for(String key : order) {
            PlayerAction pa = bindings.get(key);
            if (pa == null) {
                DragLabel label = new DragLabel(key);
                label.setLocation(x, y);
                this.content.add(label.getJPanel());
                this.locationMap.put(label.getBounds(), key);
                this.placedLabels.put(key, label);
            } else {
                pa.setLocation(x, y);
                this.content.add(pa.getJPanel());
                this.locationMap.put(pa.getDragLabel().getBounds(), key);
                this.placedLabels.put(key, pa.getDragLabel());
                this.addListener(pa.getDragLabel());
           }
            x += 40 + 5;
        } 
    }

    private void initKeys() {
        double x = 30, y = 20;
        String[] order = new String[] {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
        this.initRow(x, y, order);
        x = 50;
        y += 40 + 5;
        order = new String[] {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
        this.initRow(x, y, order);
        x = 70;
        y += 40 + 5;
        order = new String[] {"Z", "X", "C", "V", "B", "N", "M"};
        this.initRow(x, y, order);
    }
}
