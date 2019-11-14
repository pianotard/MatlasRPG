public abstract class PassiveAbility extends AbstractAbility {

    private Statistics passive;

    public PassiveAbility(String name) {
        super(name);
    }

    public Statistics getPassive() {
        return this.passive;
    }

    protected void setPassive(Statistics set) {
        this.passive = set;
    }

    @Override
    public void addDragListener(java.awt.event.MouseListener ml) {}
}
