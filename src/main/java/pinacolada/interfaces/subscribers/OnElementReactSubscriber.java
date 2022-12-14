package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.AffinityReactions;

public interface OnElementReactSubscriber
{
    void onElementReact(AffinityReactions reactions, AbstractCreature m);
}