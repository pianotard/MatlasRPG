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
        Point contentLoc = new Point(this.content.getX(), this.content.getY());
        Optional<Rectangle> found = this.locationMap.keySet().stream()
            .filter(r -> r.translate(this.location).translate(contentLoc).intersects(dl.getBounds()))
            .max((r1, r2) -> {
                int ri1 = (int) (r1
                    .translate(this.location)
                    .translate(contentLoc).intersectArea(dl.getBounds()));
                int ri2 = (int) (r2.
                    translate(this.location)
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
            oldKey.ifPresent(k -> {
                this.keyboard.unbindKey(k);
                this.content.remove(this.placedLabels.get(k).getJPanel());
                DragLabel replacement = new DragLabel(k);
                Rectangle bounds = this.locationMap.entrySet().stream()
                    .filter(es -> es.getValue().equals(k))
                    .findAny()
                    .get().getKey();
                replacement.setBounds(bounds);
                this.content.add(replacement.getJPanel());
                this.placedLabels.replace(k, replacement);
            });
            this.placedLabels.replace(key, dl);
            Rectangle bounds = this.locationMap.entrySet().stream()
                .filter(es -> es.getValue().equals(key))
                .findAny()
                .get().getKey();
            dl.setBounds(bounds);
            this.content.add(dl.getJPanel());
            this.content.repaint();
        }, () -> System.out.println("no key found"));
    }

    @Override
    public void refresh() {}

    private void initKeys() {
        double x = 30, y = 20;
        Map<String, PlayerAction> bindings = this.keyboard.getFreeBindings();
        String[] order = new String[] {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
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
            }
            x += 40 + 5;
        }
        x = 50;
        y += 40 + 5;
        order = new String[] {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
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
            }
            x += 40 + 5;
        }
        x = 70;
        y += 40 + 5;
        order = new String[] {"Z", "X", "C", "V", "B", "N", "M"};
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
            }
            x += 40 + 5;
        }
    }
}
