package pinacolada.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import extendedui.interfaces.delegates.FuncT2;
import pinacolada.actions.PCLActionWithCallback;
import pinacolada.actions.PCLActions;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLEffects;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.VFX;
import pinacolada.misc.CombatManager;
import pinacolada.powers.common.StolenGoldPower;
import pinacolada.utilities.GameUtilities;

public class DealDamage extends PCLActionWithCallback<AbstractCreature>
{
    protected final DamageInfo info;

    protected FuncT2<Float, AbstractCreature, AbstractCreature> onDamageEffect;
    protected AbstractOrb orb;
    protected boolean applyPowerRemovalMultiplier;
    protected boolean applyPowers;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean canKill = true;
    protected boolean hasPlayedEffect;
    protected boolean skipWait;
    protected int goldAmount;

    protected Color vfxColor = null;
    protected Color enemyTint = null;
    protected float pitchMin = 0.95f;
    protected float pitchMax = 1.05f;

    protected DealDamage(AbstractCreature target, DealDamage other)
    {
        this(other.target, other.info, other.attackEffect);

        copySettings(other);

        this.card = other.card;
        this.vfxColor = other.vfxColor;
        this.enemyTint = other.enemyTint;
        this.pitchMin = other.pitchMin;
        this.pitchMax = other.pitchMax;
        this.skipWait = other.skipWait;
        this.applyPowers = other.applyPowers;
        this.bypassBlock = other.bypassBlock;
        this.bypassThorns = other.bypassThorns;
        this.onDamageEffect = other.onDamageEffect;
        this.hasPlayedEffect = other.hasPlayedEffect;
    }

    public DealDamage(AbstractCreature target, DamageInfo info)
    {
        this(target, info, AttackEffect.NONE);
    }

    public DealDamage(AbstractCard card, AbstractCreature source, AbstractCreature target, AttackEffect effect)
    {
        this(card, target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect);
    }

    public DealDamage(AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        this(null, target, info, effect);
    }

    public DealDamage(AbstractCard card, AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        this.card = card;
        this.goldAmount = 0;
        this.skipWait = false;
        this.info = info;
        this.attackEffect = effect;

        boolean isInvalid = target == null || GameUtilities.isDeadOrEscaped(target);
        initialize(info.owner,
                isInvalid ? GameUtilities.getRandomEnemy(true) : target,
                isInvalid ? info.base : info.output);
        this.applyPowers = card != null && isInvalid;
    }

    public DealDamage applyPowers(boolean applyPowers)
    {
        this.applyPowers = applyPowers;
        return this;
    }

    public DealDamage canKill(boolean value)
    {
        this.canKill = value;

        return this;
    }

    @Override
    protected void firstUpdate()
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && this.shouldCancelAction())
        {
            complete();
            return;
        }

        if (GameUtilities.isDeadOrEscaped(target))
        {
            if (GameUtilities.getEnemies(true).size() > 0)
            {
                PCLActions.top.add(new DealDamage(GameUtilities.getRandomEnemy(true), this));
            }

            complete();
            return;
        }

        if (onDamageEffect != null)
        {
            addDuration(onDamageEffect.invoke(source, target));
        }

        if (this.goldAmount > 0)
        {
            PCLActions.instant.applyPower(source, new StolenGoldPower(target, goldAmount));
        }
    }

    @Override
    protected void updateInternal(float deltaTime)
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && shouldCancelAction())
        {
            complete();
            return;
        }

        if (!hasPlayedEffect && duration <= 0.1f)
        {
            addDuration(AttackEffects.getDamageDelay(attackEffect));
            PCLEffects.List.attack(source, target, attackEffect, pitchMin, pitchMax, vfxColor);
            hasPlayedEffect = true;
        }

        if (tickDuration(deltaTime))
        {
            if (applyPowers)
            {
                if (card != null)
                {
                    card.calculateCardDamage((AbstractMonster) target);
                    this.info.output = card.damage;
                }
                else
                {
                    this.info.applyPowers(this.info.owner, target);
                }
            }

            if (orb != null)
            {
                this.info.output = CombatManager.playerSystem.modifyOrbOutput(this.info.output, target, orb);
            }

            if (!canKill)
            {
                info.output = Math.max(0, Math.min(GameUtilities.getHP(target, true, true) - 1, info.output));
            }
            DamageHelper.applyTint(target, enemyTint, attackEffect);
            DamageHelper.dealDamage(target, info, bypassBlock, bypassThorns);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameUtilities.clearPostCombatActions();
            }

            if (!this.skipWait && !Settings.FAST_MODE)
            {
                PCLActions.top.wait(0.1f);
            }

            complete(target);
        }
    }

    public DealDamage setDamageEffect(PCLEffekseerEFX effekseerKey)
    {
        this.onDamageEffect = (s, m) -> VFX.eFX(effekseerKey, m.hb).duration;
        return this;
    }

    public DealDamage setDamageEffect(FuncT2<Float, AbstractCreature, AbstractCreature> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamage setOptions(boolean superFast, boolean canKill)
    {
        this.skipWait = superFast;
        this.canKill = canKill;
        return this;
    }

    public DealDamage setOrb(AbstractOrb orb)
    {
        this.orb = orb;
        return this;
    }

    public DealDamage setPiercing(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamage setSoundPitch(float pitchMin, float pitchMax)
    {
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;

        return this;
    }

    public DealDamage setVFX(boolean superFast, boolean muteSfx)
    {
        this.skipWait = superFast;

        if (muteSfx)
        {
            this.pitchMin = this.pitchMax = 0;
        }

        return this;
    }

    public DealDamage setVFXColor(Color color)
    {
        this.vfxColor = color.cpy();

        return this;
    }

    public DealDamage setVFXColor(Color color, Color enemyTint)
    {
        this.vfxColor = color.cpy();
        this.enemyTint = enemyTint.cpy();

        return this;
    }

    public DealDamage stealGold(int goldAmount)
    {
        this.goldAmount = goldAmount;

        return this;
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || (this.source != null && this.source.isDying) || (this.info.owner != null && (this.info.owner.isDying || this.info.owner.halfDead));
    }
}
