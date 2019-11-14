public class TierZeroDefBuff extends BuffAbility {
    
    private static final String PATH = "tier_zero_def_icon_";

    public TierZeroDefBuff() {
        super("Protection");
        this.setIcon(PATH + "0.png");
        this.setDescription("Hardened from the loneliness, our Beginner develops unnatural defenses.\nMP cost: 10, Buff: 10 def");
        this.setMPCost(10);
        this.setDurationS(15);
        this.setBuff(new Statistics().setDefense(10));
        for (int i = 0; i < 10; i++) {
            this.addCascadeIcon(PATH + i + ".png");
        }
        this.setKey("tier_zero_def_buff");
    }
}
