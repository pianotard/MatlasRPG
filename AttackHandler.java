import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class AttackHandler {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PausableRunner runner;

    public AttackHandler(AbstractMap map, Collection<Rectangle> borders, AbstractAttack attack, Entity target) {
        this.runner = new AttackRunner(map, borders, attack, target);
    }

    public void start() {
        this.executor.submit(runner);   
    }
}

class AttackRunner extends PausableRunner {

    private AbstractMap map;
    private Collection<Rectangle> borders;
    private AbstractAttack attack;
    private Entity target;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
 
    public AttackRunner(AbstractMap map, Collection<Rectangle> borders, AbstractAttack attack, Entity target) {
        this.map = map;
        this.borders = borders;
        this.attack = attack;
        this.target = target;
    }

    @Override
    public void run() {
        int interval = this.attack.getAttackInterval();
        this.map.showAttack(attack.getJPanel());
        while(!this.attack.finished()) {
            this.pause(interval);
            this.attack.next();
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
        if (this.target.getBounds().contains(this.attack.getTarget())) {
            this.target.inflictDamage(this.attack.getDamage());
            this.target.setInvulnerable(true);
            this.target.translate(knockBack);
            this.displayDamage(this.attack.getDamage());
        }
        this.map.remove(attack.getJPanel());
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
