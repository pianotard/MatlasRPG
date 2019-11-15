import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.awt.event.MouseEvent;

public class AbilityWindow extends UtilWindow {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 520;
    private static final int PADDING = 4;
    private static final int CONTENT_WIDTH = WIDTH - PADDING * 2;
    private static final int CONTENT_HEIGHT = HEIGHT - TOP_BAR_H - PADDING * 2 - 5;

    private static final int ROW_HEIGHT = 80;

    private Interactable manager;

    public AbilityWindow(Interactable manager) {
        super(manager.getPlayer(), "Abilities", 60, 60, WIDTH, HEIGHT);
        this.manager = manager;
        this.initLabels();
    }

    @Override
    public void refresh() {}

    public void attemptKeyBind(DragLabel dl) {
        if (this.manager.getWindowManager().isOpen("keyboard_config")) {
            KeyboardWindow kw = (KeyboardWindow) this.manager.getWindowManager().getWindow("keyboard_config");
            kw.attemptBind(dl.clone());
        }
    }

    public void initLabels() {
        Map<String, List<AbstractAbility>> abilities = this.player.getAbilities();
        for (String tier : abilities.keySet()) {
            List<AbstractAbility> tieredAbilities = abilities.get(tier);
            int y = 0;
            for (AbstractAbility a : tieredAbilities) {
                a.translate(0, y);
                y += ROW_HEIGHT;
                this.initListener(a);
                this.content.add(a.getJPanel());
            }
        }
    }

    private void initListener(AbstractAbility a) {
        a.addDragListener(new java.awt.event.MouseListener() {
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {
                Point click = new Point(e.getX(), e.getY())
                    .translate(AbilityWindow.this.location)
                    .translate(a.getLocation())
                    .translate(10, 25);
                AbilityWindow.this.showDragIconOnMap(click, a.getDragLabel());            
            }
            public void mouseReleased(MouseEvent e) {
                AbilityWindow.this.removeDragIconFromMap(a.getDragLabel());
                AbilityWindow.this.attemptKeyBind(a.getDragLabel());
            }
            public void mouseClicked(MouseEvent e) {}
        });
        a.addDragListener(new java.awt.event.MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {}
            public void mouseDragged(MouseEvent e) {
                Point drag = new Point(e.getX(), e.getY())
                    .translate(AbilityWindow.this.location)
                    .translate(a.getLocation())
                    .translate(10, 25);
                 a.getDragLabel().setCentre(drag);
            }
        });
    }
}
