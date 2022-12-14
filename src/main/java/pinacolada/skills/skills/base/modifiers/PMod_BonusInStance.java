package pinacolada.skills.skills.base.modifiers;

import com.megacrit.cardcrawl.stances.NeutralStance;
import extendedui.EUIUtils;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Stance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.GameUtilities;

public class PMod_BonusInStance extends PMod_BonusOn<PField_Stance>
{

    public static final PSkillData<PField_Stance> DATA = register(PMod_BonusInStance.class, PField_Stance.class).selfTarget();

    public PMod_BonusInStance(PSkillSaveData content)
    {
        super(content);
    }

    public PMod_BonusInStance()
    {
        super(DATA, 0);
    }

    public PMod_BonusInStance(int amount, PCLStanceHelper... stance)
    {
        super(DATA, amount);
        fields.setStance(stance);
    }

    @Override
    public String getConditionSampleText()
    {
        return PGR.core.tooltips.stance.title;
    }

    @Override
    public String getSubText()
    {
        String base = fields.getAnyStanceString();
        return TEXT.conditions.numIf(getAmountRawString(), fields.random ? TEXT.conditions.not(base) : base);
    }

    @Override
    public boolean meetsCondition(PCLUseInfo info)
    {
        return fields.random ^ (fields.stances.isEmpty() ? !GameUtilities.inStance(NeutralStance.STANCE_ID) : EUIUtils.any(fields.stances, GameUtilities::inStance));
    }
}
