package pinacolada.actions.creature;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import pinacolada.actions.PCLActionWithCallback;
import pinacolada.misc.CombatManager;
import pinacolada.monsters.PCLCardAlly;

public class TriggerAllyAction extends PCLActionWithCallback<PCLCardAlly>
{
    public final PCLCardAlly ally;

    public TriggerAllyAction(PCLCardAlly slot)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);
        initialize(AbstractDungeon.player, slot, 1);
        this.ally = slot;
    }

    @Override
    protected void firstUpdate()
    {
        if (this.ally == null || !this.ally.hasCard())
        {
            complete();
            return;
        }

        this.ally.takeTurn();
        CombatManager.onAllyTrigger(ally);
        complete(this.ally);
    }
}