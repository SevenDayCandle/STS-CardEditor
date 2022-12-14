package pinacolada.skills.skills.base.modifiers;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.pileSelection.ExhaustFromPile;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

import java.util.List;



public class PMod_ExhaustBranch extends PMod_DoBranch
{
    public static final PSkillData<PField_CardCategory> DATA = register(PMod_ExhaustBranch.class, PField_CardCategory.class)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.DrawPile, PCLCardGroupHelper.DiscardPile, PCLCardGroupHelper.Hand);

    public PMod_ExhaustBranch(PSkillSaveData content)
    {
        super(content);
    }

    public PMod_ExhaustBranch()
    {
        super(DATA);
    }

    public PMod_ExhaustBranch(int amount, PCLCardGroupHelper... groups)
    {
        super(DATA, PCLCardTarget.None, amount, groups);
    }

    public PMod_ExhaustBranch(int amount, List<PCLCardGroupHelper> groups)
    {
        super(DATA, PCLCardTarget.None, amount, groups.toArray(new PCLCardGroupHelper[]{}));
    }

    @Override
    public EUITooltip getActionTooltip()
    {
        return PGR.core.tooltips.exhaust;
    }

    @Override
    public FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction()
    {
        return ExhaustFromPile::new;
    }
}
