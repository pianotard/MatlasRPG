import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.awt.Container;

public class BuffManager {

    private Interactable manager;
    private AbstractMap map;
    private Map<String, BuffAbility> buffAbilities = new HashMap<>();
    private Map<BuffAbility, BuffRunner> activeBuffs = new HashMap<>();
    private BuffPanel buffPanel = new BuffPanel();
    private Map<ExecutorService, Future> executorMap = new HashMap<>();

    public BuffManager(Interactable manager) {
        this.manager = manager;
        this.initBuff(new TierZeroSpeedBuff());
        this.initBuff(new TierZeroDefBuff());
    }

    public void runBuffIfPresent(String key) {
        if (!this.buffAbilities.containsKey(key)) {
            return;
        }
        BuffAbility ability = this.buffAbilities.get(key);
        if (!this.manager.getPlayer().sufficientMP(ability.getMPCost())) {
            System.out.println("not enough MP!");
            return;
        }
        this.manager.getPlayer().deductMP(ability.getMPCost());
        if (!this.activeBuffs.containsKey(ability)) {
            System.out.println("fresh buff");
            ExecutorService executor = this.findFreeExecutor();
            this.manager.getPlayer().buff(ability);
            this.manager.refreshOpenWindows();
            this.manager.refreshPlayer();
            BuffRunner runner = new BuffRunner(activeBuffs, manager, buffPanel, ability);
            Future completed = executor.submit(runner);
            this.executorMap.put(executor, completed);
            this.activeBuffs.put(ability, runner);            
        } else {
            System.out.println("rebuff");
            this.activeBuffs.get(ability).restart();
        }
        this.manager.refreshOpenWindows();
    }

    private ExecutorService findFreeExecutor() {
        for (ExecutorService es : this.executorMap.keySet()) {
            Future completed = this.executorMap.get(es);
            if (completed.isDone()) {
                return es;
            }
        }
        return Executors.newSingleThreadExecutor();
    }

    public void hideBuffsForAnotherMap() {
        this.map.remove(this.buffPanel.getJPanel());
    }

    public void setMap(AbstractMap map) {
        this.map = map;
        this.map.showActiveBuffs(this.buffPanel.getJPanel());
    }

    private void initBuff(BuffAbility ability) {
        this.buffAbilities.put(ability.getKey(), ability); 
    }
}

class BuffPanel extends MapElement {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 40;
    
    private List<Container> buffsDisplaying = new ArrayList<>();

    public BuffPanel() {
        super(5, 10, WIDTH, HEIGHT);
        this.panel.setOpaque(false);
    }

    public void removeBuff(Container buffLabel) {
        this.buffsDisplaying.remove(buffLabel);
        this.panel.remove(buffLabel);
        for (int i = 0; i < this.buffsDisplaying.size(); i++) {
            Container c = this.buffsDisplaying.get(i);
            c.setLocation((HEIGHT + 5) * i, 0);
        }
    }

    public void addBuff(Container buffLabel) {
        buffLabel.setLocation((HEIGHT + 5) * buffsDisplaying.size(), 0);
        this.buffsDisplaying.add(buffLabel);
        this.panel.add(buffLabel);
    }
}

class BuffRunner extends PausableRunner {

    private static final int WIDTH = 40;
    private static final int FRAMES = 10;

    private Map<BuffAbility, BuffRunner> activeBuffs;
    private Interactable manager;
    private BuffPanel buffPanel;
    private BuffAbility ability;

    public BuffRunner(Map<BuffAbility, BuffRunner> activeBuffs, Interactable manager, BuffPanel panel, BuffAbility ability) {
        this.activeBuffs = activeBuffs;
        this.manager = manager;
        this.buffPanel = panel;
        this.ability = ability;
    }

    @Override
    public void run() {
        System.out.println("run");
        this.ability.reset();
        int cascadeDelay = this.ability.getDurationMS() / FRAMES;
        this.buffPanel.addBuff(this.ability.getActiveLabel());
        this.manager.getMap().refresh();
        for (int cascade = 0; cascade < FRAMES - 1; cascade++) {
            if (this.restart) {
                cascade = 0;
                this.ability.reset();
                this.restart = false;
            }
            this.pause(cascadeDelay);
            this.ability.cascade();
        }
        this.pause(cascadeDelay);
        this.manager.getPlayer().debuff(this.ability);
        this.manager.refreshOpenWindows();
        this.manager.refreshPlayer();
        this.buffPanel.removeBuff(ability.getActiveLabel());
        this.manager.getMap().refresh();
        this.activeBuffs.remove(this.ability);
        System.out.println("end buff");
    }
}
