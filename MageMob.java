import javax.swing.ImageIcon;

public class MageMob extends AbstractMob {
   
    private static final int DIMENSION = 20;

    public MageMob() {
        super("mage", 0, 0, DIMENSION, DIMENSION);
        this.setSpeed(0.4);
        this.setAttackRadius(400);
        this.setDetectionRadius(600);
        this.setAttack(new MageAttack());
        this.setAttackDelayMS(1600);
        this.setEXPReward(2000);
    }

    @Override
    public AbstractMob clone() {
        return new MageMob();
    }
}

class MageAttack extends HomingRangeAttack {
    
    private static final int DIMENSION = 20;
    private static final String TEMPLATE = "ranged_attack/test_attack_2";
    private static final ImageIcon[] ICONS = new ImageIcon[360];

    public static void initImages() {
        for (int i = 0; i < 360; i++) {
            ICONS[i] = UIUtil.readImageIcon(TEMPLATE + "_" + i + ".png", DIMENSION, DIMENSION);
        }
    }

    public MageAttack() {
        super("mage_attack", 0, 0, DIMENSION, DIMENSION);
        this.setDamage(7);
        this.setKnockBack(20);
        this.setSpeed(3);
        this.setImages(ICONS);
    }

    @Override
    public AbstractAttack clone() {
        return new MageAttack();
    }
}
