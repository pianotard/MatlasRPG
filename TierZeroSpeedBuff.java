public class TierZeroSpeedBuff extends BuffAbility {

    private static final String PATH = "tier_zero_agility_icon_";

    public TierZeroSpeedBuff() {
        super("Beginner's Agility");
        this.setIcon(PATH + "0.png");
        this.setDescription("Left to fend for himself, a Beginner must be swift to escape his enemies.\nMP cost: 5, Buff: 10% speed");
        this.setMPCost(5);
        this.setDurationS(10);
        this.setBuff(new Statistics().setSpeed(5));
        for (int i = 0; i < 10; i++) {
            this.addCascadeIcon(PATH + i + ".png");
        }
        this.setKey("tier_zero_speed_buff");
    }
}
