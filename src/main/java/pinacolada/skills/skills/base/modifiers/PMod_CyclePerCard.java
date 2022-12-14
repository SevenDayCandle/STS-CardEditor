package pinacolada.skills.skills.base.modifiers;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.EUIRM;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.pileSelection.CycleCards;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public class PMod_CyclePerCard extends PMod_Do
{
    public static final PSkillData<PField_CardCategory> DATA = register(PMod_CyclePerCard.class, PField_CardCategory.class)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.Hand);

    public PMod_CyclePerCard(PSkillSaveData content)
    {
        super(content);
    }

    public PMod_CyclePerCard()
    {
        super(DATA);
    }

    public PMod_CyclePerCard(int amount)
    {
        super(DATA, PCLCardTarget.None, amount);
    }

    @Override
    public String getMoveString(boolean addPeriod)
    {
        return fields.forced ? super.getMoveString(addPeriod) : EUIRM.strings.verbNoun(getActionTitle(), getAmountRawString());
    }

    @Override
    public EUITooltip getActionTooltip()
    {
        return PGR.core.tooltips.cycle;
    }

    @Override
    public FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction()
    {
        return (s, c, i, g) -> new CycleCards(s, i, fields.random);
    }
}
