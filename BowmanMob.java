import javax.swing.ImageIcon;

public class BowmanMob extends AbstractMob {
    
    private static final int DIMENSION = 20;

    public BowmanMob() {
        super("bowman", 0, 0, DIMENSION, DIMENSION);
        this.setSpeed(0.7);
        this.setAttackRadius(300);
        this.setDetectionRadius(500);
        this.setAttack(new BowmanAttack());
        this.setAttackDelayMS(1500);
    }

    @Override
    public AbstractMob clone() {
        return new BowmanMob();
    }
}

class BowmanAttack extends RangeAttack {

    private static final int DIMENSION = 20;
    private static final String TEMPLATE = "ranged_attack/test_attack_2";
    private static final ImageIcon[] ICONS = new ImageIcon[360];

    public static void initImages() {
        for (int i = 0; i < 360; i++) {
            ICONS[i] = UIUtil.readImageIcon(TEMPLATE + "_" + i + ".png", DIMENSION, DIMENSION);
        }
    }

    public BowmanAttack() {
        super("bowman_attack", 0, 0, DIMENSION, DIMENSION);
        this.setDamage(4);
        this.setKnockBack(30);
        for (int i = 0; i < 360; i++) {
            this.setImages(i + "", ICONS[i]);
        }
    }

    @Override
    public AbstractAttack clone() {
        return new BowmanAttack();
    }
}
