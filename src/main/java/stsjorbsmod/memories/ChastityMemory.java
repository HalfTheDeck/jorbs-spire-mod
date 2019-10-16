package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class ChastityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(ChastityMemory.class);

    private static final int DEXTERITY_ON_REMEMBER = 2;
    private static final int DEXTERITY_LOSS_PER_TURN = 1;
    private static final int BLOCK_PER_TURN = 6;

    public ChastityMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        this.descriptionPlaceholders.put("!D!", DEXTERITY_LOSS_PER_TURN+"");
        this.descriptionPlaceholders.put("!B!", BLOCK_PER_TURN+"");
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new DexterityPower(owner, DEXTERITY_ON_REMEMBER), DEXTERITY_ON_REMEMBER));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, DexterityPower.POWER_ID, DEXTERITY_LOSS_PER_TURN));
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(owner, source, BLOCK_PER_TURN));
    }
}
