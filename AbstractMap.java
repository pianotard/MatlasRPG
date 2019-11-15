import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Container;
import javax.swing.JLayeredPane;

public abstract class AbstractMap {
    
    private String name;
    private javax.swing.JPanel panel = new javax.swing.JPanel();
    private JLayeredPane layeredPane = new JLayeredPane();

    private Point defaultSpawnPoint;
    private Map<String, Obstacle> obstacles = new HashMap<>();
    private Map<String, Portal> portals = new HashMap<>();

    private MobSpawner mobSpawner = new MobSpawner();
    private List<AbstractMob> spawnedMobs = new ArrayList<>();
    private int mobCap = 0;

    public AbstractMap(String name) {
        this.panel.setLayout(null);
        this.panel.setBounds(0, 0, 800, 600);
        this.panel.setBackground(java.awt.Color.BLACK);
        this.panel.add(this.layeredPane);
        this.layeredPane.setLayout(null);
        this.layeredPane.setBounds(0, 0, 800, 600);
        this.name = name;
    }

    public void spawnMobs(java.math.BigInteger tick) {
        if (this.mobSpawner.empty() || this.spawnedMobs.size() == this.mobCap) {
            return;
        }
        List<AbstractMob> toSpawn = this.mobSpawner.spawnMobs(tick);
        for (AbstractMob mob : toSpawn) {
            System.out.println("Spawned: " + mob);
            this.layeredPane.add(mob.getJPanel(), JLayeredPane.DEFAULT_LAYER);
            this.spawnedMobs.add(mob);
            if (this.spawnedMobs.size() == this.mobCap) {
                return;
            }
        }
    }

    public void refresh() {
        this.layeredPane.repaint();
    }

    public void showActiveBuffs(Container panel) {
        this.layeredPane.add(panel, JLayeredPane.MODAL_LAYER);
    }

    public void showDragIcon(Container icon) {
        this.layeredPane.add(icon, JLayeredPane.DRAG_LAYER);
        this.layeredPane.moveToFront(icon);
    }

    public void showWindow(Container window) {
        this.layeredPane.add(window, JLayeredPane.POPUP_LAYER);
        this.layeredPane.moveToFront(window);
    }

    public void killMob(AbstractMob mob) {
        this.despawnMob(mob);
        this.spawnedMobs.remove(mob);
    }

    private void despawnMob(AbstractMob mob) {
        this.layeredPane.remove(mob.getJPanel());
    }

    protected void setMobCap(int cap) {
        this.mobCap = cap;
    }

    protected void setMobSpawnDelay(int tick) {
        this.mobSpawner.setSpawnDelay(tick);
    }

    public void showDamage(Container damage) {
        this.layeredPane.add(damage, JLayeredPane.PALETTE_LAYER);
    }

    public void showPlayerStatus(Container status) {
        this.layeredPane.add(status, JLayeredPane.MODAL_LAYER);
    }

    public void showAttack(Container attack) {
        this.layeredPane.add(attack, JLayeredPane.PALETTE_LAYER);
    }

    public void spawnPlayer(Container player) {
        this.panel.add(player);
    }

    public void remove(Container thing) {
        this.layeredPane.remove(thing);
        this.layeredPane.repaint();
    }

    public void resetElements() {
        for (Obstacle o : this.obstacles.values()) {
            o.resetLocation();
        }
        for (Portal p : this.portals.values()) {
            p.resetLocation();
        }
        for (AbstractMob mob : this.spawnedMobs) {
            System.out.println("despawned: " + mob);
            this.despawnMob(mob);
        }
        this.spawnedMobs.clear();
        this.mobSpawner.reset();
    }

    public void translate(double x, double y) {
        for (Obstacle o : this.obstacles.values()) {
            o.translate(x, y);
        }
        for (Portal p : this.portals.values()) {
            p.translate(x, y);
        }
    }

    public void translate(Point p) {
        this.translate(p.getX(), p.getY());
    }

    public javax.swing.JPanel getJPanel() {
        return this.panel;
    }

    public Collection<Obstacle> getObstacles() {
        return this.obstacles.values();
    }

    public Collection<Portal> getPortals() {
        return this.portals.values();
    }

    public Collection<AbstractMob> getSpawnedMobs() {
        return this.spawnedMobs;
    }

    public Point getPlayerDefaultSpawnPoint() {
        return this.defaultSpawnPoint;
    }

    protected void setDefaultSpawnPoint(double x, double y) {
        this.defaultSpawnPoint = new Point(x, y);
    }

    protected void addObstacle(String name, Obstacle o) {
        this.obstacles.put(name, o);
        this.layeredPane.add(o.getJPanel(), JLayeredPane.DEFAULT_LAYER);
    }

    protected void addPortal(String name, Portal p) {
        this.portals.put(name, p);
        this.layeredPane.add(p.getJPanel(), JLayeredPane.DEFAULT_LAYER);
    }

    protected void addMob(String name, AbstractMob mob, Point... spawnLocs) {
        this.mobSpawner.addMob(name, mob.clone(), spawnLocs);
    }

    public boolean hasEmptyMobSpawner() {
        return this.mobSpawner.empty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nMap: " + this.name + "\nMob cap: " + this.mobCap + "\n\nOBSTACLES\n\n");
        for (Obstacle o : this.obstacles.values()) {
            builder.append(o + "\n");
        }
        builder.append("\nMOBS\n\n" + this.mobSpawner);
        builder.append("\n-------------------------------");
        return builder + "";
    }
}
