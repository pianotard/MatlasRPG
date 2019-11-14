import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Player extends Entity {
    
    private static final int DIMENSION = 30;
    private static final java.awt.Color DEFAULT_COLOR = java.awt.Color.BLUE;
    private static final java.awt.Color INVULNERABLE_COLOR = new java.awt.Color(41, 60, 185);

    private String name;

    private Statistics statistics = new Statistics()
        .setHP(200)
        .setMP(100)
        .setSpeed(2)
        .setDefense(4);
    private StatusBar statusBar = new StatusBar(statistics.getHP(), statistics.getMP());

    private Map<String, List<AbstractAbility>> abilities = new HashMap<>();

    private boolean invulnerable = false;
    private int invulnerableTimeMS = 2000;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PausableRunner invulnerableTimer = new InvulnerableTimerRunner(this);

    public Player() {
        super(0, 0, DIMENSION, DIMENSION);
        this.name = "Matlas";
        this.panel.setBackground(DEFAULT_COLOR);
        this.bestowAbility("Tier 0", new TierZeroStancePassive());
        this.bestowAbility("Tier 0", new TierZeroSpeedBuff());
        this.bestowAbility("Tier 0", new TierZeroDefBuff());
        this.bestowAbility("Tier 0", new TierZeroStoneSpell());
    }

    public void deductMP(int foo) {
        this.statistics.deductMP(foo);
    }

    public boolean sufficientMP(int foo) {
        return this.statistics.sufficientMP(foo);
    }

    public StatusBar getStatusBar() {
        return this.statusBar;
    }

    public void debuff(BuffAbility ability) {
        this.statistics.debuff(ability.getBuff());
    }

    public void buff(BuffAbility ability) {
        this.statistics.buff(ability.getBuff());
    }

    public void mutate(Statistics mutate) {
        this.statistics.mutate(mutate);
    }

    public void bestowAbility(String tier, SpellAbility ability) {
        this.abilities.putIfAbsent(tier, new ArrayList<>());
        this.abilities.get(tier).add(ability);
    }

    public void bestowAbility(String tier, BuffAbility ability) {
        this.abilities.putIfAbsent(tier, new ArrayList<>());
        this.abilities.get(tier).add(ability);
    }

    public void bestowAbility(String tier, PassiveAbility ability) {
        this.abilities.putIfAbsent(tier, new ArrayList<>());
        this.abilities.get(tier).add(ability);
        Statistics passive = ability.getPassive();
        this.statistics.mutate(passive);
    }

    @Override
    public boolean resistKnockBack() {
        return this.statistics.resistKnockBack();
    }

    @Override
    public void inflictDamage(int damage) {
        this.statistics.inflictDamage(damage);
    }

    @Override
    public boolean invulnerable() {
        return this.invulnerable;
    }

    public int getInvulnerableMS() {
        return this.invulnerableTimeMS;
    }

    @Override
    public void setInvulnerable(boolean set) {
        this.invulnerable = set;
        if (set) {
            this.panel.setBackground(INVULNERABLE_COLOR);
            this.executor.submit(this.invulnerableTimer);
        } else {
            this.panel.setBackground(DEFAULT_COLOR);
        }
    }

    public Map<String, List<AbstractAbility>> getAbilities() {
        return this.abilities;
    }

    public double getSpeed() {
        return this.statistics.getSpeed();
    }

    @Override
    public void setLocation(Point p) {
        super.setLocation(p);
        this.bounds = new Rectangle(p.getX(), p.getY(), DIMENSION, DIMENSION);
    }

    public String getStatString() {
        return this.statistics.description();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Player: " + this.name + " @ " + this.location + "\n");
        builder.append(this.statistics + "");
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
