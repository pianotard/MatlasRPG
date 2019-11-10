import javax.swing.ImageIcon;

public class SlimeMob extends AbstractMob {
    
    private static final int DIMENSION = 20;

    public SlimeMob() {
        super("slime", 0, 0, DIMENSION, DIMENSION);
        this.setSpeed(0.9);
        this.setAttackRadius(25);
        this.setDetectionRadius(300);
        this.setAttack(new SlimeAttack());
        this.setAttackDelayMS(1500);
    }

    @Override
    public AbstractMob clone() {
        return new SlimeMob();
    }
}

class SlimeAttack extends MeleeAttack {

    private static final int DIMENSION = 40;
    private static final int DURATION = 200;

    private static ImageIcon ICON_LR_1, ICON_LR_2, ICON_LR_3, ICON_N_1, ICON_N_2, ICON_N_3,
            ICON_S_1, ICON_S_2, ICON_S_3;

    public static void initImages() {
        ICON_LR_1 = UIUtil.readImageIcon("test_attack_1.png", DIMENSION, DIMENSION);
        ICON_LR_2 = UIUtil.readImageIcon("test_attack_2.png", DIMENSION, DIMENSION);
        ICON_LR_3 = UIUtil.readImageIcon("test_attack_3.png", DIMENSION, DIMENSION);
        ICON_N_1 = UIUtil.readImageIcon("test_attack_1_up.png", DIMENSION, DIMENSION);
        ICON_N_2 = UIUtil.readImageIcon("test_attack_2_up.png", DIMENSION, DIMENSION);
        ICON_N_3 = UIUtil.readImageIcon("test_attack_3_up.png", DIMENSION, DIMENSION);
        ICON_S_1 = UIUtil.readImageIcon("test_attack_1_down.png", DIMENSION, DIMENSION);
        ICON_S_2 = UIUtil.readImageIcon("test_attack_2_down.png", DIMENSION, DIMENSION);
        ICON_S_3 = UIUtil.readImageIcon("test_attack_3_down.png", DIMENSION, DIMENSION);
   }

    public SlimeAttack() {
        super("slime_attack", 0, 0, DIMENSION, DIMENSION);
        this.setAttackDuration(DURATION);
        this.setDamage(6);
        this.setKnockBack(30);
        this.setImages("left", ICON_LR_1, ICON_LR_2, ICON_LR_3);
        this.setImages("right", ICON_LR_1, ICON_LR_2, ICON_LR_3);
        this.setImages("up", ICON_N_1, ICON_N_2, ICON_N_3);
        this.setImages("down", ICON_S_1, ICON_S_2, ICON_S_3);
    }

    @Override
    public AbstractAttack clone() {
        return new SlimeAttack();
    }
}
