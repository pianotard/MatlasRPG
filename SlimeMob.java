public class SlimeMob extends AbstractMob {
    
    private static final int DIMENSION = 20;

    public SlimeMob() {
        super("slime", 0, 0, DIMENSION, DIMENSION);
        this.setDetectionRadius(200);
    }

    @Override
    public AbstractMob clone() {
        return new SlimeMob();
    }
}
