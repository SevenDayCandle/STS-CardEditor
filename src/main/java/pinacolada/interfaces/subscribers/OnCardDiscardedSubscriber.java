package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardDiscardedSubscriber
{
    void onCardDiscarded(AbstractCard card);
}
