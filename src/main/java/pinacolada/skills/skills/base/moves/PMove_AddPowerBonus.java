package pinacolada.skills.skills.base.moves;

import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.skills.PMove;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField;
import pinacolada.skills.fields.PField_Power;

public class PMove_AddPowerBonus extends PMove<PField_Power>
{
    public static final PSkillData<PField_Power> DATA = register(PMove_AddPowerBonus.class, PField_Power.class, -DEFAULT_MAX, DEFAULT_MAX);

    public PMove_AddPowerBonus()
    {
        this(1);
    }

    public PMove_AddPowerBonus(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_AddPowerBonus(int amount, PCLPowerHelper... powers)
    {
        super(DATA, PCLCardTarget.None, amount);
        fields.setPower(powers);
    }

    @Override
    public String getSampleText()
    {
        return TEXT.actions.objectGainsBonus("X", "Y", TEXT.subjects.effectBonus);
    }

    @Override
    public void use(PCLUseInfo info)
    {
        for (PCLPowerHelper power : fields.powers)
        {
            getActions().addPowerEffectEnemyBonus(power.ID, amount);
        }
        super.use(info);
    }

    @Override
    public String getSubText()
    {
        return TEXT.actions.objectGainsBonus(PField.getPowerString(fields.powers), (amount > 0 ? ("+ " + getAmountRawString()) : getAmountRawString()), TEXT.subjects.effectBonus);
    }
}
