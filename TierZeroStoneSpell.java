import javax.swing.ImageIcon;

public class TierZeroStoneSpell extends SpellAbility {
   
    private static final String PATH = "tier_zero_stone_icon.png";

    public TierZeroStoneSpell() {
        super("Stone Smash");
        this.setIcon(PATH);
        this.setDescription("A stone was all our Beginner could find in his vicinity to defend himself with.\nMP cost: 2, Active: 120% damage");
        this.setMPCost(2);
        this.setAttack(new TierZeroStoneAttack());
        this.setTargetLimit(1);
        this.setCastRange(50);
        this.setKey("tier_zero_stone_spell");
    }
}

class TierZeroStoneAttack extends MeleeAttack {
    
    private static final int DIMENSION = 50;
    private static final int DURATION = 400;

    private static final ImageIcon ICON_1 = UIUtil.readImageIcon("stone_attack_0.png", DIMENSION, DIMENSION);
    private static final ImageIcon ICON_2 = UIUtil.readImageIcon("stone_attack_1.png", DIMENSION, DIMENSION);

    public TierZeroStoneAttack() {
        super("stone_smash", 0, 0, DIMENSION, DIMENSION);
        this.setAttackDuration(DURATION);
        this.setDamage(10);
        this.setKnockBack(30);
        this.setImages("left", ICON_1, ICON_2);
        this.setImages("right", ICON_1, ICON_2);
        this.setImages("up", ICON_1, ICON_2);
        this.setImages("down", ICON_1, ICON_2);
    }

    @Override
    public AbstractAttack clone() {
        return new TierZeroStoneAttack();
    }
}
