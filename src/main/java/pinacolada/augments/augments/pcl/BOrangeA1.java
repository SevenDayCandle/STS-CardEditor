package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class BOrangeA1 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(BOrangeA1.class, 1, PCLAffinity.Blue)
            .setSkill(PTrait.hasAffinity(PCLAffinity.Orange))
            .setReqs(setAffinitiesNot(PCLAffinity.Orange));

    public BOrangeA1()
    {
        super(DATA);
    }

    public BOrangeA1(PSkill skill)
    {
        super(DATA, skill);
    }
}
