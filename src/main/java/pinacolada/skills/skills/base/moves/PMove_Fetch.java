package pinacolada.skills.skills.base.moves;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.pileSelection.FetchFromPile;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public class PMove_Fetch extends PMove_Select
{
    public static final PSkillData<PField_CardCategory> DATA = register(PMove_Fetch.class, PField_CardCategory.class)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.DrawPile, PCLCardGroupHelper.DiscardPile, PCLCardGroupHelper.ExhaustPile);

    public PMove_Fetch()
    {
        this(1);
    }

    public PMove_Fetch(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_Fetch(int amount, PCLCardGroupHelper... h)
    {
        super(DATA, amount, h);
    }

    @Override
    public EUITooltip getActionTooltip()
    {
        return PGR.core.tooltips.fetch;
    }

    @Override
    public FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction()
    {
        return FetchFromPile::new;
    }
}
