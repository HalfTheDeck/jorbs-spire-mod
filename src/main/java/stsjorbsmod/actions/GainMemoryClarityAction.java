package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import stsjorbsmod.powers.memories.AbstractMemoryPower;
import stsjorbsmod.util.MemoryPowerUtils;


// "Gain clarity of your current memory"
public class GainMemoryClarityAction extends AbstractGameAction {
    private String specificMemoryID;

    public GainMemoryClarityAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source);
    }
    public GainMemoryClarityAction(AbstractCreature target, AbstractCreature source, String specificMemoryID) {
        this.setValues(target, source);
        this.specificMemoryID = specificMemoryID;
    }

    public void update() {
        AbstractMemoryPower oldMemory = MemoryPowerUtils.getCurrentMemory(this.target);
        if (oldMemory != null && !oldMemory.isClarified && (specificMemoryID == null || specificMemoryID == oldMemory.ID)) {
            oldMemory.flash();
            oldMemory.isClarified = true;
            oldMemory.updateDescription(); // for memories, also updates name to "Clarity of X"
            AbstractDungeon.effectList.add(new TextAboveCreatureEffect(target.hb.cX, target.hb.cY, oldMemory.name, Color.WHITE));
            AbstractDungeon.onModifyPower();
        }

        isDone = true;
    }
}
