package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class ODelayA1 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(ODelayA1.class, 1, PCLAffinity.Orange)
            .setSkill(PTrait.hasTags(PCLCardTag.Delayed))
            .setReqs(setTagsNot(PCLCardTag.Delayed, PCLCardTag.Innate));

    public ODelayA1()
    {
        super(DATA);
    }

    public ODelayA1(PSkill skill)
    {
        super(DATA, skill);
    }
}
