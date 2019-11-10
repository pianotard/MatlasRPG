public abstract class Entity extends MapElement {
    
    public Entity(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public abstract void inflictDamage(int damage);
    public abstract void setInvulnerable(boolean set);
}
