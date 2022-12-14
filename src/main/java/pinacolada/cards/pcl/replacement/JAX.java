package pinacolada.cards.pcl.replacement;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.skills.PMove;

public class JAX extends PCLCard
{
    public static final PCLCardData DATA = register(JAX.class)
            .setSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .setAffinities(PCLAffinity.Purple)
            .setTags(PCLCardTag.Exhaust)
            .setColorless();

    public JAX()
    {
        super(DATA);
    }

    public void setup(Object input)
    {
        addUseMove(PMove.gain(6, PCLPowerHelper.DelayedDamage));
        addUseMove(PMove.gain(4, PCLPowerHelper.Vigor, PCLPowerHelper.Sorcery).setUpgrade(1));
    }
}