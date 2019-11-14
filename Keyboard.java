import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.ActionMap;

public class Keyboard {

    private static final int KEY_DIM = 40;

    private Interactable manager;
    private Map<String, PlayerAction> fixedBindings = new HashMap<>();
    private Map<String, PlayerAction> freeBindings = new HashMap<>();

    public Keyboard(Interactable manager) {
        this.manager = manager;
        this.initMovement();
        this.initUtility();
        this.initAbility();
    }

    public void unbindKey(String key) {
        System.out.println("unbound: " + key);
        this.freeBindings.remove(key);
        javax.swing.JPanel panel = this.manager.getMap().getJPanel();
        InputMap inputMap = panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        KeyStroke ks = KeyStroke.getKeyStroke(key);
        inputMap.remove(ks);
        actionMap.remove(key);
    }

    public void bindAbility(String key, String abilityKey, String imgPath) {
        System.out.println("bound: " + abilityKey + " to " + key + ", img path: " + imgPath);
        this.freeBindings.put(key, new AbilityAction(abilityKey, manager)
                .setImageIcon(UIUtil.readImageIcon(imgPath, KEY_DIM, KEY_DIM)));
    }

    public void initBindings(javax.swing.JPanel panel) {
        InputMap inputMap = panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        for (String s : this.fixedBindings.keySet()) {
            KeyStroke ks = KeyStroke.getKeyStroke(s);
            inputMap.put(ks, s);
            actionMap.put(s, this.fixedBindings.get(s));
        }
        for (String s : this.freeBindings.keySet()) {
            KeyStroke ks = KeyStroke.getKeyStroke(s);
            inputMap.put(ks, s);
            actionMap.put(s, this.freeBindings.get(s));
        }
    }

    public Map<String, PlayerAction> getFreeBindings() {
        return this.freeBindings;
    }

    public void refreshMapBindings() {
        this.initBindings(this.manager.getMap().getJPanel());
    }

    private void initAbility() {
        this.bindAbility("X", "tier_zero_speed_buff", "tier_zero_agility_icon_0.png");
    }

    private void initUtility() {
        this.freeBindings.put("S", new UtilAction("player_statistics", "STAT", manager));
        this.freeBindings.put("A", new UtilAction("player_abilities", "ABIL", manager));
        this.freeBindings.put("K", new UtilAction("keyboard_config", "KEYS", manager));
    }

    public void refreshPlayer() {
        Supplier<Double> zero = () -> 0.0;
        Supplier<Double> positive = () -> this.manager.getPlayer().getSpeed();
        Supplier<Double> negative = () -> 0 - this.manager.getPlayer().getSpeed();
        this.fixedBindings.replace("pressed UP", new MoveAction("UP", zero, negative, manager));
        this.fixedBindings.replace("pressed DOWN", new MoveAction("DOWN", zero, positive, manager));
        this.fixedBindings.replace("pressed LEFT", new MoveAction("LEFT", negative, zero, manager));
        this.fixedBindings.replace("pressed RIGHT", new MoveAction("RIGHT", positive, zero, manager));
        this.fixedBindings.replace("released UP", new MoveAction("UP", zero, zero, manager));
        this.fixedBindings.replace("released DOWN", new MoveAction("DOWN", zero, zero, manager));
        this.fixedBindings.replace("released LEFT", new MoveAction("LEFT", zero, zero, manager));
        this.fixedBindings.replace("released RIGHT", new MoveAction("RIGHT", zero, zero, manager)); 
   }

    private void initMovement() {
        Supplier<Double> zero = () -> 0.0;
        Supplier<Double> positive = () -> this.manager.getPlayer().getSpeed();
        Supplier<Double> negative = () -> 0 - this.manager.getPlayer().getSpeed();
        this.fixedBindings.put("pressed UP", new MoveAction("UP", zero, negative, manager));
        this.fixedBindings.put("pressed DOWN", new MoveAction("DOWN", zero, positive, manager));
        this.fixedBindings.put("pressed LEFT", new MoveAction("LEFT", negative, zero, manager));
        this.fixedBindings.put("pressed RIGHT", new MoveAction("RIGHT", positive, zero, manager));
        this.fixedBindings.put("released UP", new MoveAction("UP", zero, zero, manager));
        this.fixedBindings.put("released DOWN", new MoveAction("DOWN", zero, zero, manager));
        this.fixedBindings.put("released LEFT", new MoveAction("LEFT", zero, zero, manager));
        this.fixedBindings.put("released RIGHT", new MoveAction("RIGHT", zero, zero, manager)); 
    }
}

class AbilityAction extends PlayerAction {
    
    Interactable manager;

    public AbilityAction(String key, Interactable manager) {
        super(key, key);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.manager.runAbility((String) getValue(NAME));
    }
}

class UtilAction extends PlayerAction {
    
    Interactable manager;

    public UtilAction(String key, String name, Interactable manager) {
        super(key, name);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.manager.toggleUtil((String) getValue(NAME));
    }
}

class MoveAction extends PlayerAction {
    
    Supplier<Double> x, y;
    Interactable manager;

    public MoveAction(String name, Supplier<Double> x, Supplier<Double> y, Interactable manager) {
        super(name, name);
        this.x = x;
        this.y = y;
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.manager.handleMovement((String) getValue(NAME), this.x.get(), this.y.get());
    }
}
