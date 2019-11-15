import java.awt.Font;

public class DragLabel extends MapElement {

    private static final Font FONT = new Font("Serif", Font.BOLD, 12);

    private String key = "";
    private String actionKey = "";
    private String imgPath = "";

    public DragLabel(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public DragLabel(String label) {
        super(0, 0, 40, 40);
        this.label.setText(label);
        this.label.setFont(FONT);
        this.label.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
    }

    public DragLabel clone() {
        DragLabel clone = new DragLabel(this.key);
        clone.setBounds(this.bounds);
        clone.setKey(this.key);
        clone.setImgPath(this.imgPath);
        clone.setImageIcon(UIUtil.readImageIcon(this.imgPath, 40, 40));
        return clone;
    }

    public void setActionKey(String key) {
        this.actionKey = key;
    }

    public String getActionKey() {
        return this.actionKey;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImgPath(String path) {
        this.imgPath = path;
    }

    public String getKey() {
        return this.key;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImageIcon(javax.swing.ImageIcon icon) {
        this.label.setIcon(icon);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DragLabel) {
            DragLabel d = (DragLabel) o;
            return d.key.equals(this.key);
        }
        return false;
    }

    @Override
    public String toString() {
        return "DragLabel with key: " + key;
    }
}
