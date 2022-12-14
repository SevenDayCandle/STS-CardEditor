package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class OExhaustA1 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(OExhaustA1.class, 3, PCLAffinity.Orange)
            .setSkill(PTrait.hasTags(PCLCardTag.Exhaust), PTrait.hasCost(-1))
            .setReqs(setTagsNot(PCLCardTag.Exhaust, PCLCardTag.Purge, PCLCardTag.Fleeting));

    public OExhaustA1()
    {
        super(DATA);
    }

    public OExhaustA1(PSkill skill)
    {
        super(DATA, skill);
    }
}
