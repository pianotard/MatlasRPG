import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;

public abstract class AbstractMap {
    
    private String name;
    private JLayeredPane layeredPane = new JLayeredPane();
    private JPanel panel = new JPanel();

    private Point defaultSpawnPoint;
    private Map<String, Obstacle> obstacles = new HashMap<>();
    private Map<String, Portal> portals = new HashMap<>();

    private MobSpawner mobSpawner = new MobSpawner();
    private List<AbstractMob> spawnedMobs = new ArrayList<>();
    private int mobCap = 0;

    public AbstractMap(String name) {
        this.layeredPane.setLayout(null);
        this.layeredPane.setBounds(0, 0, 800, 600);
        this.panel.setLayout(null);
        this.panel.setBounds(0, 0, 800, 600);
        this.layeredPane.add(this.panel, JLayeredPane.DEFAULT_LAYER);
        this.name = name;
        this.panel.setBackground(java.awt.Color.BLACK);
    }

    public void spawnMobs(java.math.BigInteger tick) {
        if (this.mobSpawner.empty() || this.spawnedMobs.size() == this.mobCap) {
            return;
        }
        List<AbstractMob> toSpawn = this.mobSpawner.spawnMobs(tick);
        for (AbstractMob mob : toSpawn) {
            System.out.println("Spawned: " + mob);
            this.panel.add(mob.getJPanel());
            this.spawnedMobs.add(mob);
            if (this.spawnedMobs.size() == this.mobCap) {
                return;
            }
        }
    }

    private void despawnMob(AbstractMob mob) {
        this.panel.remove(mob.getJPanel());
    }

    protected void setMobCap(int cap) {
        this.mobCap = cap;
    }

    protected void setMobSpawnDelay(int tick) {
        this.mobSpawner.setSpawnDelay(tick);
    }

    public void showPlayerStatus(JPanel status) {
        this.layeredPane.add(status, JLayeredPane.POPUP_LAYER);
    }

    public void removePlayerStatus(JPanel status) {
        this.layeredPane.remove(status);
    }

    public void spawnPlayer(JPanel player) {
        this.panel.add(player);
    }

    public void despawnPlayer(JPanel player) {
        this.panel.remove(player);
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

    public JLayeredPane getPanel() {
        return this.layeredPane;
    }

    public JPanel getJPanel() {
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
        this.panel.add(o.getJPanel());
    }

    protected void addPortal(String name, Portal p) {
        this.portals.put(name, p);
        this.panel.add(p.getJPanel());
    }

    protected void addMob(String name, AbstractMob mob, Point... spawnLocs) {
        this.mobSpawner.addMob(name, mob.clone(), spawnLocs);
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
