import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JLabel;

public abstract class PlayerAction extends AbstractAction implements ActionListener {
 
    private String name;
    private DragLabel label;

    public PlayerAction(String abilityKey, String name) {
        super(abilityKey);
        this.name = name;
        this.label = new DragLabel(name);
        this.label.setKey(abilityKey);
    }

    public PlayerAction(String key, DragLabel label) {
        super(key);
        this.label = label;
        this.label.setKey(key);
    }

    protected void setActionKey(String key) {
        this.label.setActionKey(key);
    }

    public DragLabel getDragLabel() {
        return this.label;
    }

    public javax.swing.JPanel getJPanel() {
        return this.label.getJPanel();
    }
/*
    public void setLabel(String key) {
        JLabel label = new JLabel();
        label.setLayout(null);
        label.setBounds(0, 0, 40, 10);
        label.setText(key);
        this.label.getJPanel().add(label);
    }
*/
    public PlayerAction setImageIcon(javax.swing.ImageIcon icon) {
        this.label.setImageIcon(icon);
        return this;
    }

    public void setLocation(double x, double y) {
        this.label.setLocation(x, y);
    }
}
