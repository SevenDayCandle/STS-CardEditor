package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnModifyDamageFirstSubscriber
{
    int onModifyDamageFirst(AbstractCreature target, DamageInfo info, int damage);
} 