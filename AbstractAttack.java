import java.util.Map;
import java.util.HashMap;
import javax.swing.ImageIcon;

public abstract class AbstractAttack extends MapElement {
    
    private String name;

    protected String nowShowingName;
    protected int nowShowingIndex = 0;
    protected Map<String, ImageIcon[]> icons = new HashMap<>();

    protected Point source, target;

    private int damage = 5;
    private double knockBack = 20;
    protected int attackDuration;

    public AbstractAttack(String name, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.panel.setBackground(null);
        this.panel.setOpaque(false);
        this.name = name;    
    }

    public abstract AbstractAttack clone();
    public abstract void next();
    public abstract boolean finished();
    public abstract int getAttackInterval();

    public void resetIcon() {
        this.nowShowingIndex = 0;
        this.label.setIcon(this.icons.get(nowShowingName)[0]);
    }

    public int getNumberIcons() {
        return this.icons.get(nowShowingName).length;
    }

    public Point getUnitVector() {
        return source.getUnitVectorTo(target);
    }

    public Point getTarget() {
        return this.target;
    }

    public AbstractAttack setPath(Line path) {
        this.source = path.getStart();
        this.target = path.getEnd();
        return this;
    }

    protected void setAttackDuration(int time) {
        this.attackDuration = time;
    }

    protected void setImages(String name) {
        this.nowShowingName = name;
        this.label.setIcon(this.icons.get(name)[0]);
    }

    protected void setImages(String name, ImageIcon... icons) {
        this.nowShowingName = name;
        this.icons.put(name, icons);
    }

    public double getKnockBack() {
        return this.knockBack;
    }

    protected void setKnockBack(double kb) {
        this.knockBack = kb;
    }

    public int getDamage() {
        return this.damage;
    }

    protected void setDamage(int damage) {
        this.damage = damage;
    }
}
