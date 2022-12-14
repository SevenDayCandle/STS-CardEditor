package pinacolada.skills.skills.base.primary;

import pinacolada.cards.base.PCLUseInfo;
import pinacolada.resources.PGR;
import pinacolada.skills.PLimit;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Empty;

public class PLimit_Limited extends PLimit
{
    public static final PSkillData<PField_Empty> DATA = register(PLimit_Limited.class, PField_Empty.class, 1, 1)
            .selfTarget();

    public PLimit_Limited()
    {
        super(DATA);
    }

    public PLimit_Limited(PSkillSaveData content)
    {
        super(content);
    }

    @Override
    public boolean canActivate(PCLUseInfo info)
    {
        return info.canActivateLimited;
    }

    @Override
    public boolean tryActivate(PCLUseInfo info)
    {
        return info.tryActivateLimited();
    }

    @Override
    public String getSubText()
    {
        return PGR.core.tooltips.limited.title;
    }
}
