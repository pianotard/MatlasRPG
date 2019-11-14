import java.util.List;
import java.util.ArrayList;

public class Statistics {
    
    private static final java.util.Random RNG = new java.util.Random();
    private static final double MAX_SPEED = 5;

    private HP hp;
    private MP mp;
    private double stance = 0;
    private double speed = 0;
    private int defense = 0;
    private List<Statistics> buffs = new ArrayList<>();

    public Statistics() {
        this.hp = new HP(0);
        this.mp = new MP(0);
    }

    public void deductMP(int foo) {
        this.mp.deductMP(foo);
    }

    public boolean sufficientMP(int foo) {
        return this.mp.getCurrent() > foo;
    }

    public void debuff(Statistics s) {
        this.buffs.remove(s);
    }

    public void buff(Statistics s) {
        this.buffs.add(s);
    }

    public void mutate(Statistics s) {
        hp.mutate(s.hp);
        mp.mutate(s.mp);
        this.stance += s.stance;
        this.speed += s.speed;
    }

    public boolean resistKnockBack() {
        return RNG.nextDouble() * 100 < this.stance + this.buffs.stream()
            .mapToDouble(b -> b.stance)
            .sum();
    }

    public void inflictDamage(int damage) {
        this.hp.inflictDamage(damage);
    }

    public double getSpeed() {
        return this.speed + this.buffs.stream()
            .mapToDouble(b -> b.speed)
            .sum();
    }

    public HP getHP() {
        return this.hp;
    }

    public MP getMP() {
        return this.mp;
    }

    public Statistics setDefense(int set) {
        this.defense = set;
        return this;
    }

    public Statistics setSpeed(double set) {
        if (set < 0 || set > MAX_SPEED) {
            System.out.println("Invalid speed!");
            return this;
        }
        this.speed = set;
        return this;
    }

    public Statistics setStance(double set) {
        if (set < 0 || set > 100) {
            System.out.println("Invalid percentage for stance!");
            return this;
        }
        this.stance = set;
        return this;
    }

    public Statistics setMP(int set) {
        this.mp = new MP(set);
        return this;
    }

    public Statistics setHP(int set) {
        this.hp = new HP(set);
        return this;
    }
    
    public String description() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("HP: " + this.hp.description() + "<br>");
        builder.append("MP: " + this.mp.description() + "<br>");
        builder.append("Stance: " + this.stance + "%<br>");
        builder.append("Speed: " + (this.speed / MAX_SPEED * 100));
        double spdBuff = this.buffs.stream().mapToDouble(b -> b.speed).sum();
        if (spdBuff != 0) {
            builder.append(" + " + (spdBuff / MAX_SPEED * 100));
        }
        builder.append("%<br>");
        builder.append("Def: " + this.defense);
        int defBuff = this.buffs.stream().mapToInt(b -> b.defense).sum();
        if (defBuff != 0) {
            builder.append(" + " + defBuff);
        }
        builder.append("<br>");
        builder.append("</html>");
        return builder + "";
    }

    @Override
    public String toString() {
        return this.hp + "";
    }
}
