import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class WindowManager {
    
    private java.util.Map<String, UtilWindow> windows = new java.util.HashMap<>();
    private java.util.Map<String, UtilWindow> open = new java.util.HashMap<>();
    private Interactable manager;
    private AbstractMap map;

    public WindowManager(Interactable manager) {
        Player player = manager.getPlayer();
        Keyboard keyboard = manager.getKeyboard();
        this.windows.put("player_statistics", new StatWindow(player));
        this.windows.put("player_abilities", new AbilityWindow(manager));
        this.windows.put("keyboard_config", new KeyboardWindow(player, keyboard));
        this.windows.put("player_inventory", new ItemWindow(manager));
        this.initListeners();
    }

    public UtilWindow getWindow(String key) {
        return this.windows.get(key);
    }

    public boolean isOpen(String key) {
        return this.open.containsKey(key);        
    }

    public void moveToFront(UtilWindow w) {
        this.map.remove(w.getJPanel());
        this.map.showWindow(w.getJPanel());
    }

    public void toggle(String key) {
        UtilWindow w = this.windows.get(key);
        if (this.open.values().contains(w)) {
            w.removeDragLabel();
            this.open.remove(key);
            this.map.remove(w.getJPanel());
            this.map.getJPanel().repaint();
        } else {
            w.refresh();
            this.open.put(key, w);
            this.map.showWindow(w.getJPanel());
        }
    }

    public void refreshOpenWindows() {
        for (UtilWindow w : this.open.values()) {
            w.refresh();
        }
    }

    public void setMap(AbstractMap map) {
        this.map = map;
        for (UtilWindow w : this.open.values()) {
            this.map.showWindow(w.getJPanel());
        }
        for (UtilWindow w : this.windows.values()) {
            w.setMap(map);
        }
    }

    public void closeWindowsForAnotherMap() {
        for (UtilWindow w : this.open.values()) {
            this.map.remove(w.getJPanel());
        }
    }

    private void initListeners() {
        for (UtilWindow w : this.windows.values()) {
            w.getJPanel().addMouseListener(new MouseListener() {
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {
                    WindowManager.this.moveToFront(w);
                }
                public void mouseReleased(MouseEvent e) {}
                public void mouseClicked(MouseEvent e) {}
            });
        }
    }
}
