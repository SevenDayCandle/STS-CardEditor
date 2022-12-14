package pinacolada.augments.augments.pcl;

import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class GAimA1 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(GAimA1.class, 1, PCLAffinity.Green)
            .setSkill(PTrait.hasCardTarget(PCLCardTarget.Single))
            .setReqs(setTargets(PCLCardTarget.RandomEnemy, PCLCardTarget.Self));

    public GAimA1()
    {
        super(DATA);
    }

    public GAimA1(PSkill skill)
    {
        super(DATA, skill);
    }
}
