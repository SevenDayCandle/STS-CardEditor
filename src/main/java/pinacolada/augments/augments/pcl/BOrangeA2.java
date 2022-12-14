package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class BOrangeA2 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(BOrangeA2.class, 3, PCLAffinity.Blue)
            .setSkill(PTrait.hasAffinity(2, PCLAffinity.Orange), PTrait.hasAffinityNot(PCLAffinity.Red, PCLAffinity.Green, PCLAffinity.Blue))
            .setReqs(setAffinitiesNot(PCLAffinity.Orange));

    public BOrangeA2()
    {
        super(DATA);
    }

    public BOrangeA2(PSkill skill)
    {
        super(DATA, skill);
    }
}
