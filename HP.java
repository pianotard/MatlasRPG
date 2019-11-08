public class HP extends MapElement {
    
    int current, max;

    public HP(int max) {
        super(0, 0, 80, 20);
        this.current = max;
        this.max = max;
    }

    public void inflictDamage(int damage) {
        this.current -= damage;
        this.current = Math.max(this.current, 0);
    }

    @Override
    public String toString() {
        return "HP: " + current + " / " + max;
    }
}
