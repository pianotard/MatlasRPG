import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Player extends Entity {
    
    private static final int DIMENSION = 30;
    
    private String name;

    private HP hp = new HP(200);
    private StatusBar statusBar = new StatusBar(this.hp);
    private double speed = 2;
    
    private boolean invulnerable = false;
    private int invulnerableTimeMS = 2000;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PausableRunner invulnerableTimer = new InvulnerableTimerRunner(this);

    public Player() {
        super(0, 0, DIMENSION, DIMENSION);
        this.name = "Matlas";
        this.panel.setBackground(java.awt.Color.BLUE);
    }

    public StatusBar getStatusBar() {
        return this.statusBar;
    }

    public void inflictDamage(int damage) {
        this.hp.inflictDamage(damage);
    }

    public boolean invulnerable() {
        return this.invulnerable;
    }

    public int getInvulnerableMS() {
        return this.invulnerableTimeMS;
    }

    public void setInvulnerable(boolean set) {
        this.invulnerable = set;
        if (set) {
            this.executor.submit(this.invulnerableTimer);
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void setLocation(Point p) {
        super.setLocation(p);
        this.bounds = new Rectangle(p.getX(), p.getY(), DIMENSION, DIMENSION);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Player: " + this.name + " @ " + this.location + "\n");
        builder.append(this.hp + "");
        return builder + "";
    }
}

class InvulnerableTimerRunner extends PausableRunner {
    
    private Player player;

    public InvulnerableTimerRunner(Player p) {
        this.player = p;
    }

    @Override
    public void run() {
        this.pause(this.player.getInvulnerableMS());
        this.player.setInvulnerable(false);
    }
}
