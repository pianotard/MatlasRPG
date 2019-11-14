import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.function.Function;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.Timer;

public class MatlasManager implements java.awt.event.ActionListener, Interactable {
    
    private static final int BORDER = 50;

    private Player player;
 
    private AbstractMap currentMap;
    private Map<String, Rectangle> borders = new HashMap<>();
    private GameMapManager gameMaps = new GameMapManager();

    private Timer globalTimer;
    private BigInteger globalTick = BigInteger.ZERO;
    private Map<String, Double[]> pressedKeys = new HashMap<>();
    private WindowManager windowManager;
    private BuffManager buffManager;
    private SpellManager spellManager;
    private Keyboard keyboard;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private GameFrame frame = new GameFrame();

    public MatlasManager() {
        java.util.concurrent.Future imagesDone = this.executor.submit(new ImageReader());
        AbstractMap entryMap = this.gameMaps.get("test_map");
        this.player = new Player();
        this.player.setLocation(entryMap.getPlayerDefaultSpawnPoint());

        this.keyboard = new Keyboard(this);
        this.windowManager = new WindowManager(this);
        this.buffManager = new BuffManager(this);
        this.spellManager = new SpellManager(this);
        this.initBorders();
        this.setMap(entryMap);
        this.globalTimer = new Timer(4, this);

        try {
            imagesDone.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.err.println(e);
        }
        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (this.mapIsQuiet()) {
            this.globalTimer.stop();
            return;
        }
        this.globalTick = this.globalTick.add(BigInteger.ONE);
        this.movePlayer();
        this.checkPortals();
        this.spawnMobs();
        this.moveMobs();
    }

    @Override
    public void runAbility(String key) {
        System.out.println("manager received command: run " + key);
        this.spellManager.castSpellIfPresent(key);
        this.buffManager.runBuffIfPresent(key);
    }

    @Override
    public void toggleUtil(String key) {
        this.windowManager.toggle(key);
    }

    @Override
    public List<AbstractMob> findTargetsForPlayer(double radius) {
        Function<AbstractMob, Double> distanceFromPlayer = m -> m.getCentre()
            .distance(player.getCentre());
        List<AbstractMob> targets = this.currentMap.getSpawnedMobs().stream()
            .filter(m -> distanceFromPlayer.apply(m) < radius)
            .collect(java.util.stream.Collectors.toList());
        targets.sort((m1, m2) -> (int) (distanceFromPlayer.apply(m1) - distanceFromPlayer.apply(m2)));
        return targets;
    }

    private void setMap(AbstractMap map) {
        this.currentMap = map;
        map.spawnPlayer(this.player.getJPanel());
        map.showPlayerStatus(this.player.getStatusBar().getJPanel());
        this.keyboard.initBindings(map.getJPanel());
        this.windowManager.setMap(map);
        this.buffManager.setMap(map);
        this.frame.add(map.getJPanel());
    }

    private boolean mapIsQuiet() {
        boolean playerNotMoving = this.pressedKeys.isEmpty();
        boolean noMobs = this.currentMap.hasEmptyMobSpawner();
        return playerNotMoving && noMobs;
    }

    private void attemptAttack(AbstractMob mob) {
        if (this.player.invulnerable() || mob.isAttacking()) {
            return;
        }
        if (mob.targetSighted(this.player.getBounds().getCentre())) {
            mob.setAttacking(true);
            Line projectilePath = new Line(mob.getCentre(), player.getCentre());
            AbstractAttack mobAttack = mob.getAttack().setPath(projectilePath);
            AttackHandler handler = new AttackHandler(this, mobAttack, player);
            handler.start();
        }
    }

    private void moveMobs() {
        for (AbstractMob mob : this.currentMap.getSpawnedMobs()) {
            if (mob.isAttacking()) {
                continue;
            }
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
            mob.setPlayerDirection(this.player.getCentre());
            this.attemptMove(mob);
            this.attemptAttack(mob);
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
    }

    private void spawnMobs() {
        this.currentMap.spawnMobs(this.globalTick);
        this.frame.repaint();
    }

    private void checkPortals() {
        for (Portal p : this.currentMap.getPortals()) {
           if (p.getBounds().intersects(this.player.getBounds())) {
                AbstractMap oldMap = this.currentMap;
                this.frame.remove(oldMap.getJPanel());
                oldMap.resetElements();
                oldMap.remove(this.player.getJPanel());
                oldMap.remove(this.player.getStatusBar().getJPanel());
                this.player.setLocation(p.getSpawn());
                AbstractMap entryMap = this.gameMaps.get(p.getExitMapID());
                Point entryTranslation = p.getExitMapTranslation();
                entryMap.translate(entryTranslation);
                this.windowManager.closeWindowsForAnotherMap();
                this.buffManager.hideBuffsForAnotherMap();
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

    @Override
    public void handleMovement(String key, double x, double y) {
        if (this.pressedKeys.size() != 1) {
            this.globalTimer.start();
        }
        if (x == 0 && y == 0) {
            this.pressedKeys.remove(key);
        } else {
            this.pressedKeys.putIfAbsent(key, new Double[] {x, y});
        }
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Keyboard getKeyboard() {
        return this.keyboard;
    }

    @Override
    public AbstractMap getMap() {
        return this.currentMap;
    }

    @Override
    public WindowManager getWindowManager() {
        return this.windowManager;
    }

    @Override
    public java.util.Collection<Rectangle> getBorders() {
        return this.borders.values();
    }

    @Override
    public void refreshPlayer() {
        this.keyboard.refreshPlayer();
    }

    @Override
    public void refreshOpenWindows() {
        this.windowManager.refreshOpenWindows();
    }

    private void initBorders() {
        this.borders.put("north", new Rectangle(0, 0, 800, BORDER));
        this.borders.put("south", new Rectangle(0, 600 - BORDER * 2, 800, BORDER * 2));
        this.borders.put("east", new Rectangle(800 - BORDER, 0, BORDER, 600));
        this.borders.put("west", new Rectangle(0, 0, BORDER, 600)); 
    }

    @Override
    public String toString() {
        return this.currentMap + "\n" + this.player;
    }
}
