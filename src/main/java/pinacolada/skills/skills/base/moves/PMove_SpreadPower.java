package pinacolada.skills.skills.base.moves;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.skills.PMove;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Power;
import pinacolada.utilities.GameUtilities;

import java.util.List;

public class PMove_SpreadPower extends PMove<PField_Power>
{
    public static final PSkillData<PField_Power> DATA = register(PMove_SpreadPower.class, PField_Power.class);

    public PMove_SpreadPower()
    {
        this(PCLCardTarget.Self, 1);
    }

    public PMove_SpreadPower(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_SpreadPower(PCLCardTarget target, PCLPowerHelper... powers)
    {
        this(target, 0, powers);
    }

    public PMove_SpreadPower(PCLCardTarget target, int amount, PCLPowerHelper... powers)
    {
        super(DATA, target, amount);
        fields.setPower(powers);
    }

    @Override
    public String getSampleText()
    {
        return PGR.core.tooltips.spread.title;
    }

    @Override
    public void use(PCLUseInfo info)
    {
        List<AbstractCreature> targets = getTargetList(info);
        if (fields.powers.isEmpty())
        {
            for (PCLPowerHelper power : PCLPowerHelper.commonDebuffs())
            {
                spreadPower(info.source, targets, power);
            }
        }
        else if (fields.random)
        {
            PCLPowerHelper power = GameUtilities.getRandomElement(fields.powers);
            if (power != null)
            {
                spreadPower(info.source, targets, power);
            }
        }
        else
        {
            for (PCLPowerHelper power : fields.powers)
            {
                spreadPower(info.source, targets, power);
            }
        }
        super.use(info);
    }

    @Override
    public String getSubText()
    {
        String powerString = fields.getPowerSubjectString();
        String mainString = amount <= 0 ? TEXT.actions.spread(powerString, getTargetString()) : TEXT.actions.spreadAmount(getAmountRawString(), powerString, getTargetString());
        return fields.random ? TEXT.subjects.randomly(mainString) : mainString;
    }

    protected void spreadPower(AbstractCreature p, List<AbstractCreature> targets, PCLPowerHelper power)
    {
        for (AbstractCreature t : targets)
        {
            getActions().spreadPower(p, t, power.ID, amount);
        }
        // Handle powers that are equivalent in terms of what the player sees but that have different IDs
        if (power == PCLPowerHelper.Intangible)
        {
            for (AbstractCreature t : targets)
            {
                getActions().spreadPower(p, t, IntangiblePower.POWER_ID, amount);
            }
        }
    }
}
