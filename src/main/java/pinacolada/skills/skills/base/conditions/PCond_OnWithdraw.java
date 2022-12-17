package pinacolada.skills.skills.base.conditions;

import extendedui.ui.tooltips.EUITooltip;
import pinacolada.cards.base.PCLCard;
import pinacolada.monsters.PCLCardAlly;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;

public class PCond_OnWithdraw extends PCond_Delegate
{
    public static final PSkillData DATA = register(PCond_OnWithdraw.class, PCLEffectType.Delegate, 1, 1)
            .pclOnly()
            .selfTarget();

    public PCond_OnWithdraw()
    {
        super(DATA);
    }

    public PCond_OnWithdraw(PSkillSaveData content)
    {
        super(content);
    }

    @Override
    public boolean triggerOnAllyWithdraw(PCLCard c, PCLCardAlly ally)
    {
        return triggerOnCard(c, ally);
    }

    @Override
    public EUITooltip getDelegateTooltip()
    {
        return PGR.core.tooltips.withdraw;
    }
}