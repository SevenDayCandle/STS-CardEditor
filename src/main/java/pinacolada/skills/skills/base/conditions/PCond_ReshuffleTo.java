package pinacolada.skills.skills.base.conditions;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.pileSelection.ReshuffleFromPile;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public class PCond_ReshuffleTo extends PCond_DoTo
{
    public static final PSkillData<PField_CardCategory> DATA = register(PCond_ReshuffleTo.class, PField_CardCategory.class)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.ExhaustPile, PCLCardGroupHelper.DiscardPile, PCLCardGroupHelper.Hand);

    public PCond_ReshuffleTo()
    {
        this(1, PCLCardGroupHelper.Hand);
    }

    public PCond_ReshuffleTo(PSkillSaveData content)
    {
        super(content);
    }

    public PCond_ReshuffleTo(int amount, PCLCardGroupHelper... h)
    {
        super(DATA, PCLCardTarget.None, amount, h);
    }

    @Override
    public EUITooltip getActionTooltip()
    {
        return PGR.core.tooltips.reshuffle;
    }

    @Override
    public FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction()
    {
        return ReshuffleFromPile::new;
    }
}
