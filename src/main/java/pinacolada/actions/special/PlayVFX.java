package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import pinacolada.actions.PCLActionWithCallback;
import pinacolada.effects.PCLEffects;

public class PlayVFX extends PCLActionWithCallback<AbstractGameEffect>
{
    private AbstractGameEffect effect;
    private boolean isTopLevelEffect;
    private boolean wait;

    public PlayVFX(AbstractGameEffect effect, float duration)
    {
        super(AbstractGameAction.ActionType.WAIT, duration);

        this.wait = false;
        this.effect = effect;
    }

    @Override
    protected void firstUpdate()
    {
        if (this.isTopLevelEffect)
        {
            PCLEffects.TopLevelList.add(effect);
        }
        else
        {
            PCLEffects.List.add(effect);
        }
    }

    @Override
    protected void updateInternal(float deltaTime)
    {
        if (wait)
        {
            isDone = effect.isDone;
        }
        else
        {
            tickDuration(deltaTime);
        }

        if (isDone)
        {
            complete(effect);
        }
    }
}
