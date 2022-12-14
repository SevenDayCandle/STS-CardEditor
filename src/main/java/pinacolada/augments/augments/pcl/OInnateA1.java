package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class OInnateA1 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(OInnateA1.class, 1, PCLAffinity.Orange)
            .setSkill(PTrait.hasTags(PCLCardTag.Innate))
            .setReqs(setTagsNot(PCLCardTag.Delayed, PCLCardTag.Innate));

    public OInnateA1()
    {
        super(DATA);
    }

    public OInnateA1(PSkill skill)
    {
        super(DATA, skill);
    }
}
