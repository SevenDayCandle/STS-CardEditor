package pinacolada.skills.skills.base.conditions;

import extendedui.EUIRM;
import extendedui.EUIUtils;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.skills.PCond;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Orb;
import pinacolada.utilities.GameUtilities;

public class PCond_CheckOrb extends PCond<PField_Orb>
{
    public static final PSkillData<PField_Orb> DATA = register(PCond_CheckOrb.class, PField_Orb.class)
            .selfTarget();

    public PCond_CheckOrb(PSkillSaveData content)
    {
        super(content);
    }

    public PCond_CheckOrb(int amount, PCLOrbHelper... orb)
    {
        super(DATA, PCLCardTarget.None, amount);
        fields.setOrb(orb);
    }

    @Override
    public String getSampleText()
    {
        return TEXT.actions.evoke("X");
    }

    @Override
    public String getSubText()
    {
        String tt = fields.getOrbAndOrString();
        return TEXT.conditions.ifYouHave(amount == 1 ? tt : EUIRM.strings.numNoun(amount <= 0 ? amount : amount + "+", tt));
    }

    @Override
    public boolean checkCondition(PCLUseInfo info, boolean isUsing, boolean fromTrigger)
    {
        if (fields.orbs.isEmpty())
        {
            return amount <= 0 ? GameUtilities.getOrbCount() == 0 : GameUtilities.getOrbCount() >= amount;
        }
        return fields.random ? EUIUtils.any(fields.orbs, o -> GameUtilities.getOrbCount(o.ID) >= amount) : EUIUtils.all(fields.orbs, o -> GameUtilities.getOrbCount(o.ID) >= amount);
    }
}
