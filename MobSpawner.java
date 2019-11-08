import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class MobSpawner {
    
    private Map<String, AbstractMob> mobs = new HashMap<>();
    private Map<AbstractMob, Point[]> spawnLocs = new HashMap<>();

    private BigInteger spawnDelay = new BigInteger("10");
    private BigInteger lastSpawned = new BigInteger("-1");

    public MobSpawner() {}

    public List<AbstractMob> spawnMobs(BigInteger currentTick) {
        List<AbstractMob> spawned = new ArrayList<>();
        if (this.lastSpawned.add(this.spawnDelay).compareTo(currentTick) == -1) {
            this.lastSpawned = currentTick;
            for (AbstractMob mob : this.mobs.values()) {
                for (Point loc : this.spawnLocs.get(mob)) {
                    AbstractMob toSpawn = mob.clone();
                    toSpawn.setLocation(loc);
                    spawned.add(toSpawn);
                }
            }
        }
        return spawned;
    }

    public void addMob(String name, AbstractMob mob, Point... spawnLocs) {
        this.mobs.put(name, mob);
        this.spawnLocs.put(mob, spawnLocs);
    }

    public void setSpawnDelay(int tick) {
        this.spawnDelay = new BigInteger(tick + "");
    }

    public void reset() {
        this.lastSpawned = new BigInteger("-1");
    }

    public boolean empty() {
        return this.mobs.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String mobName : this.mobs.keySet()) {
            builder.append(mobName + "\n");
            AbstractMob mob = this.mobs.get(mobName);
            for (Point loc : this.spawnLocs.get(mob)) {
                builder.append("  " + loc + "\n");
            }
        }
        return builder + "";
    }
}
