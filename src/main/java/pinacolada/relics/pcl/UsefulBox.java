package pinacolada.relics.pcl;

import pinacolada.actions.PCLActions;
import pinacolada.cards.base.PCLCard;
import pinacolada.interfaces.subscribers.OnAllySummonSubscriber;
import pinacolada.misc.CombatManager;
import pinacolada.monsters.PCLCardAlly;

public class UsefulBox extends AbstractBox implements OnAllySummonSubscriber
{
    public static final String ID = createFullID(UsefulBox.class);

    public UsefulBox()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        CombatManager.onAllySummon.subscribe(this);
        setCounter(1);
    }

    @Override
    public void onAllySummon(PCLCard card, PCLCardAlly ally)
    {
        if (counter > 0)
        {
            PCLActions.delayed.gainBlock(getValue());
            PCLActions.delayed.gainBlock(ally, getValue());
            addCounter(-1);
        }
    }

    public void atTurnStart()
    {
        setCounter(1);
    }

    public int getValue()
    {
        return 3;
    }
}