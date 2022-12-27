package pinacolada.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.resources.PGR;

import java.util.ArrayList;

public class FetchFromPile extends SelectFromPile
{

    public FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DRAW, sourceName, null, amount, groups);
    }

    public FetchFromPile(String sourceName, AbstractCreature target, int amount, CardGroup... groups)
    {
        super(ActionType.DRAW, sourceName, target, amount, groups);
    }

    @Override
    protected void complete(ArrayList<AbstractCard> result)
    {
        moveToPile(result, player.hand);
        super.complete(result);
    }

    @Override
    public String getActionMessage()
    {
        return PGR.core.tooltips.fetch.title;
    }
}
