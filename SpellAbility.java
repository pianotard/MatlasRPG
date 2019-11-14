public abstract class SpellAbility extends AbstractAbility {

    private AbstractAttack attack;
    private int targetLimit = 1;
    private double castRange = 300;

    public SpellAbility(String name) {
        super(name);
    }

    public AbstractAttack getAttack() {
        return this.attack;
    }

    protected void setAttack(AbstractAttack attack) {
        this.attack = attack;
    }

    public int getTargetLimit() {
        return this.targetLimit;
    }

    protected void setTargetLimit(int set) {
        this.targetLimit = set;
    }

    public double getCastRange() {
        return this.castRange;
    }

    protected void setCastRange(double set) {
        this.castRange = set;
    }
}
