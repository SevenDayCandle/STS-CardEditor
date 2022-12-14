package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.actions.PCLActions;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.ui.combat.CombatHelper;
import pinacolada.utilities.GameUtilities;

public class DelayedDamagePower extends PCLPower implements HealthBarRenderPower
{
    public static final String POWER_ID = createFullID(DelayedDamagePower.class);
    private static final Color healthBarColor = Color.PURPLE.cpy();
    private final AbstractGameAction.AttackEffect attackEffect;

    public DelayedDamagePower(AbstractCreature owner, int amount)
    {
        this(owner, amount, AttackEffects.SLASH_VERTICAL);
    }

    public DelayedDamagePower(AbstractCreature owner, int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(owner, POWER_ID);

        this.priority = 97;
        this.attackEffect = attackEffect;

        initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public String getUpdatedDescription()
    {
        return formatDescription(0, amount, owner.isPlayer ? powerStrings.DESCRIPTIONS[1] : "");
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.getHealthBarAmount(owner, amount, true, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.play(SFX.HEART_BEAT, 1.25f, 1.35f, 0.9f);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        int damageAmount = owner.isPlayer ? Math.max(0, Math.min(GameUtilities.getHP(owner, true, true) - 1, amount)) : amount;
        PCLActions.bottom.takeDamage(owner, damageAmount, attackEffect);
        removePower();

        playApplyPowerSfx();
        flashWithoutSound();

        super.atEndOfTurn(isPlayer);
    }
}