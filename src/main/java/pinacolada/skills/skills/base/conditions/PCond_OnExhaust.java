package pinacolada.skills.skills.base.conditions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public class PCond_OnExhaust extends PCond_Delegate
{
    public static final PSkillData<PField_CardCategory> DATA = register(PCond_OnExhaust.class, PField_CardCategory.class, 1, 1)
            .selfTarget();

    public PCond_OnExhaust()
    {
        super(DATA);
    }

    public PCond_OnExhaust(PSkillSaveData content)
    {
        super(content);
    }

    @Override
    public boolean triggerOnExhaust(AbstractCard c)
    {
        return triggerOnCard(c);
    }

    @Override
    public EUITooltip getDelegateTooltip()
    {
        return PGR.core.tooltips.exhaust;
    }
}
