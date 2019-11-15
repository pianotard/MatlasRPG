import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class AttackHandler {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PausableRunner runner;

    public AttackHandler(Interactable manager, AbstractAttack attack, Entity target) {
        this.runner = new AttackRunner(manager, attack, target);
    }

    public AttackHandler(Interactable manager, AbstractAttack attack, Point target) {
        this.runner = new BlindAttackRunner(manager, attack, target);
    }

    public void start() {
        this.executor.submit(runner);   
    }
}

class BlindAttackRunner extends PausableRunner {
     
    private Interactable manager;
    private AbstractMap map;
    private Collection<Rectangle> borders;
    private AbstractAttack attack;
    private Point target;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public BlindAttackRunner(Interactable manager, AbstractAttack attack, Point target) {
        this.manager = manager;
        this.map = manager.getMap();
        this.borders = manager.getBorders();
        this.attack = attack;
        this.target = target; 
    }

    @Override
    public void run() {
        int interval = this.attack.getAttackInterval();
        this.map.showAttack(attack.getJPanel());
        while(!this.attack.finished(this.target)) {
            this.pause(interval);
            this.attack.next(this.target, this.map.getObstacles());
        }
        this.pause(interval);
        this.map.remove(attack.getJPanel());
        this.manager.refreshOpenWindows();
        this.attack.resetIcon();
    }
}

class AttackRunner extends PausableRunner {

    private Interactable manager;
    private AbstractMap map;
    private Collection<Rectangle> borders;
    private AbstractAttack attack;
    private Entity target;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
 
    public AttackRunner(Interactable manager, AbstractAttack attack, Entity target) {
        this.manager = manager;
        this.map = manager.getMap();
        this.borders = manager.getBorders();
        this.attack = attack;
        this.target = target;
    }

    @Override
    public void run() {
        int interval = this.attack.getAttackInterval();
        this.map.showAttack(attack.getJPanel());
        while(!this.attack.finished(this.target.getCentre())) {
            this.pause(interval);
            this.attack.next(this.target.getCentre(), this.map.getObstacles());
        }
        this.pause(interval);
        Point knockBack = attack.getUnitVector().scale(attack.getKnockBack());
        Rectangle nextPos = this.target.getBounds().translate(knockBack);
        for (Obstacle o : this.map.getObstacles()) {
            if (o.getBounds().intersects(nextPos)) {
                do {
                    knockBack = knockBack.scale(0.9);
                    nextPos = this.target.getBounds().translate(knockBack);
                } while (o.getBounds().intersects(nextPos));
            }
        }
        for (Rectangle border : this.borders) {
            if (border.intersects(nextPos)) {
                this.map.translate(knockBack.scale(-1));
                break;
            }
        }
        if (this.target.getBounds().contains(this.attack.getCentre()) && !this.target.invulnerable()) {
            this.target.inflictDamage(this.attack.getDamage());
            this.target.setInvulnerable(true);
            if (!this.target.resistKnockBack()) {
                this.target.translate(knockBack);
            }
            this.displayDamage(this.attack.getDamage());
        }
        if (this.target.isDead()) {
            if (target instanceof AbstractMob) {
                AbstractMob mob = (AbstractMob) this.target;
                this.map.killMob(mob);
                this.manager.getPlayer().rewardEXP(mob.getEXPReward());
            }
        }
        this.map.remove(attack.getJPanel());
        this.manager.refreshOpenWindows();
        this.attack.resetIcon();
    }

    private void displayDamage(int damage) {
         DamageDisplayRunner runner = new DamageDisplayRunner(this.map, this.target, damage);
         this.executor.submit(runner);
    }
}

class DamageDisplayRunner extends PausableRunner {
    
    private static final java.awt.Font FONT = new java.awt.Font("Serif", java.awt.Font.PLAIN, 18);
    private static final int DURATION = 1000;

    private AbstractMap map;
    private Entity target;
    private int damage;

    public DamageDisplayRunner(AbstractMap map, Entity target, int damage) {
        this.map = map;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void run() {
        javax.swing.JLabel label = new javax.swing.JLabel();        
        label.setLayout(null);
        label.setText("-" + this.damage);
        label.setForeground(java.awt.Color.RED);
        label.setBounds(0, 0, 40, 18);
        label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        label.setFont(FONT);
        label.setLocation(this.target.getLocation().translate(-5, -30).toAWT());
        this.map.showDamage(label);
        this.pause(DURATION);
        this.map.remove(label);
    }
}
