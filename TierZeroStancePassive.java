public class TierZeroStancePassive extends PassiveAbility {

    private static final String PATH = "tier_zero_stance_icon.png";

    public TierZeroStancePassive() {
        super("Beginner's Grit");
        this.setIcon(PATH);
        this.setDescription("All alone in this world, a Beginner must learn to stand his ground firmly.\nPassive: 20% stance");
        this.setPassive(new Statistics().setStance(20));
        this.setKey("tier_zero_stance_passive");
    }
}
