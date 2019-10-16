package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.powers.SnappedPower;


// This is like ApplyPowerAction, but with the additional effect of removing other non-clarified memories
public class RememberSpecificMemoryAction extends AbstractGameAction  {
    private AbstractMemory memoryToRemember;

    public RememberSpecificMemoryAction(AbstractMemory memoryToRemember) {
        this.setValues(memoryToRemember.owner, memoryToRemember.owner);
        this.memoryToRemember = memoryToRemember;
    }

    private void removeOtherMemories() {
        for (AbstractPower oldPower : this.source.powers) {
            if (oldPower instanceof AbstractMemory) {
                AbstractMemory oldMemory = (AbstractMemory) oldPower;
                if (!oldMemory.isClarified) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, oldMemory));
                }
            }
        }
    }

    public void update() {
        if (target.hasPower(SnappedPower.POWER_ID)) {
            target.getPower(SnappedPower.POWER_ID).flash();
            isDone = true;
            return;
        }

        // Regardless of whether the old memory is clarified or not, re-remembering it is a no-op
        if (target.hasPower(memoryToRemember.ID)) {
            target.getPower(memoryToRemember.ID).flashWithoutSound();
            isDone = true;
            return;
        }

        // addToTop instead of addToBottom is for some card interactions that do "remember X. Do Y related to X.", eg
        // * Unseen Servant
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, memoryToRemember));

        if (!memoryToRemember.isClarified) {
            removeOtherMemories();
        }

        isDone = true;
    }
}
