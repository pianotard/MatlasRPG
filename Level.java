import java.util.function.Function;

public class Level {
    
    private static final Function<Integer, Integer> TNL = level -> {
        if (level < 24) {
            return (int) Math.pow(1.0682 * level + 1.5294, 4);
        }
        if (level == 100) {
            return 0;
        }
        return (int) Math.exp(0.1607 * level + 10.6886);
    };

    private int level = 0;

    public Level(int level) {
        this.level = level;
    }

    public void levelUp() {
        this.level++;
    }

    public int getTnl() {
        return TNL.apply(this.level);
    }

    public String description() {
        return "Level: " + this.level;
    }
}
