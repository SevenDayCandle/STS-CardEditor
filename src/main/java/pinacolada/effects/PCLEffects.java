package pinacolada.effects;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import extendedui.interfaces.delegates.ActionT0;
import extendedui.interfaces.delegates.ActionT1;
import extendedui.interfaces.delegates.ActionT2;
import pinacolada.actions.utility.WaitRealtimeAction;
import pinacolada.effects.combat.TalkEffect;
import pinacolada.effects.player.ObtainRelicEffect;
import pinacolada.effects.player.RemoveRelicEffect;
import pinacolada.effects.player.SpawnRelicEffect;
import pinacolada.effects.utility.CallbackEffect;
import pinacolada.effects.utility.CallbackEffect2;
import pinacolada.effects.vfx.EffekseerEffect;

import java.util.ArrayList;


public final class PCLEffects
{
    public final static ArrayList<AbstractGameEffect> UnlistedEffects = new ArrayList<>();
    public final static PCLEffects List = new PCLEffects(EffectType.List);
    public final static PCLEffects Queue = new PCLEffects(EffectType.Queue);
    public final static PCLEffects TopLevelList = new PCLEffects(EffectType.TopLevelList);
    public final static PCLEffects TopLevelQueue = new PCLEffects(EffectType.TopLevelQueue);
    public final static PCLEffects Manual = new PCLEffects(EffectType.Manual);

    protected final EffectType effectType;

    protected PCLEffects(EffectType effectType)
    {
        this.effectType = effectType;
    }

    public static boolean isEmpty()
    {
        for (AbstractGameEffect effect : AbstractDungeon.topLevelEffects)
        {
            if (effect instanceof PCLEffect)
            {
                return false;
            }
        }

        return UnlistedEffects.isEmpty();
    }

    public <T extends AbstractGameEffect> T add(T effect)
    {
        getList().add(effect);

        return effect;
    }

    public ArrayList<AbstractGameEffect> getList()
    {
        switch (effectType)
        {
            case List:
                return AbstractDungeon.effectList;

            case Queue:
                return AbstractDungeon.effectsQueue;

            case TopLevelList:
                return AbstractDungeon.topLevelEffects;

            case TopLevelQueue:
                return AbstractDungeon.topLevelEffectsQueue;

            case Manual:
                return UnlistedEffects;
        }

        throw new RuntimeException("Enum value does not exist.");
    }

    public PCLEffect attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax)
    {
        return attack(source, target, attackEffect, pitchMin, pitchMax, null, source == target ? 0 : 0.15f);
    }

    public PCLEffect attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax, Color vfxColor)
    {
        return attack(source, target, attackEffect, pitchMin, pitchMax, vfxColor, source == target ? 0 : 0.15f);
    }

    public PCLEffect attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax, Color vfxColor, float spread)
    {
        AttackEffects.playSound(attackEffect, pitchMin, pitchMax);
        return attackWithoutSound(source, target, attackEffect, vfxColor, spread);
    }

    public PCLEffect attackWithoutSound(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, Color vfxColor, float spread)
    {
        final PCLEffect effect = add(AttackEffects.getVFX(attackEffect, source, VFX.randomX(target.hb, spread), VFX.randomY(target.hb, spread)));
        if (vfxColor != null)
        {
            effect.setColor(vfxColor);
        }

        return effect;
    }

    public BorderFlashEffect borderFlash(Color color)
    {
        return add(new BorderFlashEffect(color, true));
    }

    public BorderLongFlashEffect borderLongFlash(Color color)
    {
        return add(new BorderLongFlashEffect(color, true));
    }

    public CallbackEffect2 callback(AbstractGameEffect effect)
    {
        return add(new CallbackEffect2(effect));
    }

    public CallbackEffect2 callback(AbstractGameEffect effect, ActionT0 onCompletion)
    {
        return add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 callback(AbstractGameEffect effect, ActionT1<AbstractGameEffect> onCompletion)
    {
        return add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 callback(AbstractGameEffect effect, Object state, ActionT2<Object, AbstractGameEffect> onCompletion)
    {
        return add(new CallbackEffect2(effect, state, onCompletion));
    }

    public CallbackEffect callback(ActionT0 onCompletion)
    {
        return add(new CallbackEffect(new WaitAction(0.01f), onCompletion));
    }

    public CallbackEffect callback(AbstractGameAction action)
    {
        return add(new CallbackEffect(action));
    }

    public CallbackEffect callback(AbstractGameAction effect, ActionT0 onCompletion)
    {
        return add(new CallbackEffect(effect, onCompletion));
    }

    public CallbackEffect callback(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        return add(new CallbackEffect(action, onCompletion));
    }

    public CallbackEffect callback(AbstractGameAction action, Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        return add(new CallbackEffect(action, state, onCompletion));
    }

    public int count()
    {
        return getList().size();
    }

    public EffekseerEffect playEFX(PCLEffekseerEFX key, float x, float y)
    {
        return add(VFX.eFX(key, x, y));
    }

    public EffekseerEffect playEFX(PCLEffekseerEFX key)
    {
        return add(VFX.eFX(key));
    }

    public ObtainRelicEffect obtainRelic(AbstractRelic relic)
    {
        return add(new ObtainRelicEffect(relic));
    }

    public RemoveRelicEffect removeRelic(AbstractRelic relic)
    {
        return add(new RemoveRelicEffect(relic));
    }

    public RoomTintEffect roomTint(Color color, float transparency)
    {
        return add(new RoomTintEffect(color.cpy(), transparency));
    }

    public RoomTintEffect roomTint(Color color, float transparency, float setDuration, boolean renderBehind)
    {
        return add(new RoomTintEffect(color.cpy(), transparency, setDuration, renderBehind));
    }

    public ShowCardAndObtainEffect showAndObtain(AbstractCard card)
    {
        return showAndObtain(card, Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, true);
    }

    public ShowCardAndObtainEffect showAndObtain(AbstractCard card, float x, float y, boolean converge)
    {
        return add(new ShowCardAndObtainEffect(card, x, y, converge));
    }

    public ShowCardBrieflyEffect showCardBriefly(AbstractCard card)
    {
        return add(new ShowCardBrieflyEffect(card));
    }

    public ShowCardBrieflyEffect showCardBriefly(AbstractCard card, float x, float y)
    {
        return add(new ShowCardBrieflyEffect(card, x, y));
    }

    public ShowCardBrieflyEffect showCopy(AbstractCard card)
    {
        return showCardBriefly(card.makeStatEquivalentCopy());
    }

    public SpawnRelicEffect spawnRelic(AbstractRelic relic, float x, float y)
    {
        return add(new SpawnRelicEffect(relic, x, y));
    }

    public TalkEffect talk(AbstractCreature source, String message)
    {
        return add(new TalkEffect(source, message));
    }

    public TalkEffect talk(AbstractCreature source, String message, float duration)
    {
        return add(new TalkEffect(source, message, duration));
    }

    public CallbackEffect waitRealtime(float duration)
    {
        return add(new CallbackEffect(new WaitRealtimeAction(duration)));
    }

    public enum EffectType
    {
        List,
        Queue,
        TopLevelList,
        TopLevelQueue,
        Manual
    }
}