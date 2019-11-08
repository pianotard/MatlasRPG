import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.Timer;

public class MatlasManager implements java.awt.event.ActionListener {
    
    private static final int BORDER = 50;

    private Player player;
 
    private AbstractMap currentMap;
    private Map<String, Rectangle> borders = new HashMap<>();
    private Map<String, AbstractMap> gameMaps = new HashMap<>();

    private Timer globalTimer;
    private BigInteger globalTick = BigInteger.ZERO;
    private Map<String, Double[]> pressedKeys = new HashMap<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private GameFrame frame = new GameFrame();

    public MatlasManager() {
        this.gameMaps.put("test_map", new TestMap());
        this.gameMaps.put("test_map_2", new TestMapTwo());
        AbstractMap entryMap = this.gameMaps.get("test_map");
        this.player = new Player();
        this.player.setLocation(entryMap.getPlayerDefaultSpawnPoint());
        this.initBorders();
        this.setMap(entryMap);
        this.globalTimer = new Timer(4, this);
        this.globalTimer.start();
        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        this.globalTick = this.globalTick.add(BigInteger.ONE);
        this.movePlayer();
        this.checkPortals();
        this.spawnMobs();
        this.moveMobs();
    }

    private void setMap(AbstractMap map) {
        this.currentMap = map;
        map.spawnPlayer(this.player.getJPanel());
        map.showPlayerStatus(this.player.getStatusBar().getJPanel());
        this.initPlayerMover(map);
        this.frame.add(map.getPanel());
    }

    private void displayPlayerDamage(int damage) {
         DamageDisplayRunner runner = new DamageDisplayRunner(this.currentMap, this.player, damage);
         this.executor.submit(runner);
    }

    private void checkPlayerCollide(AbstractMob mob) {
        if (this.player.invulnerable()) {
            return;
        }
        if (mob.getBounds().intersects(this.player.getBounds())) {
            Line direction = new Line(mob.getBounds().getCentre(), player.getBounds().getCentre());
            Point knockBack = direction.getUnitVector().scale(mob.getKnockBack());
            Rectangle nextPos = this.player.getBounds().translate(knockBack);
            for (Obstacle o : this.currentMap.getObstacles()) {
                if (o.getBounds().intersects(nextPos)) {
                    knockBack = knockBack.scale(0);
                }
            }
            for (Rectangle border : this.borders.values()) {
                if (border.intersects(nextPos)) {
                    this.currentMap.translate(knockBack.scale(-1));
                    break;
                }
            }
            this.player.inflictDamage(mob.getBodyDamage());
            this.player.setInvulnerable(true);
            this.displayPlayerDamage(mob.getBodyDamage());
            this.player.translate(knockBack);
        }
    }

    private void moveMobs() {
        for (AbstractMob mob : this.currentMap.getSpawnedMobs()) {
            Line direction = new Line(mob.getBounds().getCentre(), this.player.getLocation());
            if (direction.length() > mob.getDetectionRadius()) {
                this.attemptRandomMove(mob);
                continue;
            }
            boolean blocked = false;
            for (Obstacle o : this.currentMap.getObstacles()) {
                if (o.intersects(direction)) {
                    blocked = true;
                    break;
                }
            }
            if (blocked) {
                this.attemptRandomMove(mob);
                continue;
            }
            mob.setPlayerDirection(this.player.getLocation());
            this.attemptMove(mob);
        } 
    }

    private void attemptMove(AbstractMob mob) {
        Point direction = mob.getPlayerDirection();
        Rectangle nextPos = mob.getBounds().translate(direction);
        for (Obstacle o : this.currentMap.getObstacles()) {
            if (o.getBounds().intersects(nextPos)) {
                this.attemptRandomMove(mob);
                return;
            }
        }
        mob.translate(direction);
        this.checkPlayerCollide(mob);
    }

    private void attemptRandomMove(AbstractMob mob) {
        mob.clearPlayerDirectionIfPresent();
        mob.initRandomDirectionIfAbsent();
        Point direction = mob.getRandomDirection();
        Rectangle nextPos = mob.getBounds().translate(direction);
        for (Obstacle o : this.currentMap.getObstacles()) {
            if (o.getBounds().intersects(nextPos)) {
                mob.changeRandomDirection();
                return;
            }
        }
        mob.translate(direction);
        this.checkPlayerCollide(mob);
    }

    private void spawnMobs() {
        this.currentMap.spawnMobs(this.globalTick);
        this.frame.repaint();
    }

    private void checkPortals() {
        for (Portal p : this.currentMap.getPortals()) {
           if (p.getBounds().intersects(this.player.getBounds())) {
                this.frame.remove(this.currentMap.getPanel());
                this.currentMap.resetElements();
                this.currentMap.despawnPlayer(this.player.getJPanel());
                this.currentMap.removePlayerStatus(this.player.getStatusBar().getJPanel());
                this.player.setLocation(p.getSpawn());
                AbstractMap entryMap = this.gameMaps.get(p.getExitMapID());
                Point entryTranslation = p.getExitMapTranslation();
                entryMap.translate(entryTranslation);
                this.setMap(entryMap);
                this.frame.repaint();
                System.out.println(this.currentMap);
                this.globalTick = BigInteger.ZERO;
                return;
            }
        }   
    }

    private void movePlayer() {
        if (this.pressedKeys.values().size() == 0) {
            return;
        }
        double x = 0, y = 0;
        for (Double[] move : this.pressedKeys.values()) {
            x += move[0];
            y += move[1];
        }
        Rectangle nextPos = this.player.getBounds().translate(x, y);
        for (Obstacle o : this.currentMap.getObstacles()) {
            if (o.getBounds().intersects(nextPos)) {
                return;
            }
        }
        for (Rectangle border : this.borders.values()) {
            if (border.intersects(nextPos)) {
                this.currentMap.translate(0 - x, 0 - y);
                return;
            }
        }
        this.player.translate(x, y); 
    }

    private void initPlayerMover(AbstractMap map) {
        InputMap inputMap = map.getPanel().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = map.getPanel().getActionMap();

        this.addMove("UP", 0, 0 - this.player.getSpeed(), inputMap, actionMap);
        this.addMove("LEFT", 0 - this.player.getSpeed(), 0, inputMap, actionMap);
        this.addMove("DOWN", 0, this.player.getSpeed(), inputMap, actionMap);
        this.addMove("RIGHT", this.player.getSpeed(), 0, inputMap, actionMap);
    }

    private void addMove(String name, double x, double y, InputMap inputMap, ActionMap actionMap) {
        Action pressedAction = new MoveAction(name, x, y, this);
        String pressedKey = "pressed " + name;
        KeyStroke pressedKS = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKS, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new MoveAction(name, 0, 0, this);
        String releasedKey = "released " + name;
        KeyStroke releasedKS = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKS, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    public void handlePressedKey(String key, double x, double y) {
        if (x == 0 && y == 0) {
            this.pressedKeys.remove(key);
        } else {
            this.pressedKeys.putIfAbsent(key, new Double[] {x, y});
        }
    }

    private void initBorders() {
        this.borders.put("north", new Rectangle(0, 0, 800, BORDER));
        this.borders.put("south", new Rectangle(0, 600 - BORDER, 800, BORDER));
        this.borders.put("east", new Rectangle(800 - BORDER, 0, BORDER, 600));
        this.borders.put("west", new Rectangle(0, 0, BORDER, 600)); 
    }

    @Override
    public String toString() {
        return this.currentMap + "\n" + this.player;
    }
}

class MoveAction extends javax.swing.AbstractAction implements java.awt.event.ActionListener {
    
    double x, y;
    MatlasManager manager;

    public MoveAction(String name, double x, double y, MatlasManager manager) {
        super(name);
        this.x = x;
        this.y = y;
        this.manager = manager;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        this.manager.handlePressedKey((String) getValue(NAME), this.x, this.y);
    }
}

class DamageDisplayRunner implements Runnable {
    
    private static final java.awt.Font FONT = new java.awt.Font("Serif", java.awt.Font.PLAIN, 17);

    private AbstractMap map;
    private Player player;
    private int damage;

    public DamageDisplayRunner(AbstractMap map, Player player, int damage) {
        this.map = map;
        this.player = player;
        this.damage = damage;
    }

    @Override
    public void run() {
        javax.swing.JLabel label = new javax.swing.JLabel();        
        label.setLayout(null);
        label.setText("-" + this.damage);
        label.setForeground(java.awt.Color.RED);
        label.setBounds(0, 0, 40, 15);
        label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        label.setFont(FONT);
        label.setLocation(this.player.getLocation().translate(-5, -25).toAWT());
        this.map.getJPanel().add(label);
        this.pause(1000);
        this.map.getJPanel().remove(label);
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
