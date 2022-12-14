package pinacolada.augments.augments.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class RBlockB2 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(RBlockB2.class, 2, PCLAffinity.Red)
            .setSkill(PTrait.hasBlockMultiplier(70), PTrait.hasCost(1))
            .setReqs(setTypes(AbstractCard.CardType.SKILL));

    public RBlockB2()
    {
        super(DATA);
    }

    public RBlockB2(PSkill skill)
    {
        super(DATA, skill);
    }
}
