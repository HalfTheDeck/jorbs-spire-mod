package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

public class TemperanceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(TemperanceMemory.class);

    private static final int ENEMY_STRENGTH_REDUCTION = 3;

    private ArrayList<AbstractGameAction> restoreStrengthActions;

    public TemperanceMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        setDescriptionPlaceholder("!M!", ENEMY_STRENGTH_REDUCTION);
    }

    @Override
    public void onRemember() {
        this.restoreStrengthActions = new ArrayList<>();

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(mo, owner, new StrengthPower(mo, -ENEMY_STRENGTH_REDUCTION), -ENEMY_STRENGTH_REDUCTION, true, AttackEffect.NONE));

            if (!mo.hasPower(ArtifactPower.POWER_ID)) {
                this.restoreStrengthActions.add(
                        new ApplyPowerAction(mo, owner, new StrengthPower(mo, +ENEMY_STRENGTH_REDUCTION), +ENEMY_STRENGTH_REDUCTION, true, AttackEffect.NONE));
            }
        }
    }

    @Override
    public void onForget() {
        for (AbstractGameAction restoreAction : this.restoreStrengthActions) {
            AbstractDungeon.actionManager.addToBottom(restoreAction);
        }
        restoreStrengthActions.clear();
    }
}
