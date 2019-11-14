import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public class SpellManager {

    private Interactable manager;
    private Map<String, SpellAbility> spellAbilities = new HashMap<>();

    public SpellManager(Interactable manager) {
        this.manager = manager;
        this.initSpell(new TierZeroStoneSpell());
    }

    public void castSpellIfPresent(String key) {
        if (!this.spellAbilities.containsKey(key)) {
            return;
        }
        SpellAbility spell = this.spellAbilities.get(key);
        if (!this.manager.getPlayer().sufficientMP(spell.getMPCost())) {
            System.out.println("not enough MP!");
            return;
        }
        this.manager.getPlayer().deductMP(spell.getMPCost());
        this.manager.refreshOpenWindows();
        Point playerPos = manager.getPlayer().getCentre();
        List<AbstractMob> targets = this.manager.findTargetsForPlayer(spell.getCastRange());
        if (targets.size() == 0) {
            System.out.println("no targets found, run blind attack");
            Point blindTarget = playerPos.translate(spell.getCastRange(), 0);
            Line projectilePath = new Line(playerPos, blindTarget);
            AbstractAttack pAttack = spell.getAttack().setPath(projectilePath);
            AttackHandler handler = new AttackHandler(manager, pAttack, blindTarget);
            handler.start();
            return;
        }
        int targeted = 0;
        for (AbstractMob target : targets) {
            Line projectilePath = new Line(playerPos, target.getCentre());
            AbstractAttack pAttack = spell.getAttack().setPath(projectilePath);
            AttackHandler handler = new AttackHandler(manager, pAttack, target);
            handler.start();
            if (++targeted >= spell.getTargetLimit()) {
                break;
            }
        }
    }

    private void initSpell(SpellAbility ability) {
        this.spellAbilities.put(ability.getKey(), ability); 
    }
}
