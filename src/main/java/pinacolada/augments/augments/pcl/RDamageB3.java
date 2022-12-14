package pinacolada.augments.augments.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.skills.PSkill;
import pinacolada.skills.PTrait;

public class RDamageB3 extends PCLAugment
{

    public static final PCLAugmentData DATA = register(RDamageB3.class, 3, PCLAffinity.Red)
            .setSkill(PTrait.hasDamageMultiplier(140), PTrait.hasCost(2))
            .setReqs(setTypes(AbstractCard.CardType.ATTACK));

    public RDamageB3()
    {
        super(DATA);
    }

    public RDamageB3(PSkill skill)
    {
        super(DATA, skill);
    }
}
