public class StatusBar extends MapElement {
    
    private HP hp;

    public StatusBar(HP hp) {
        super(300, 520, 200, 80);
        this.hp = hp;
        this.panel.add(this.hp.getJPanel());
    }

    @Override
    public String toString() {
        return this.hp + "";
    }
}
