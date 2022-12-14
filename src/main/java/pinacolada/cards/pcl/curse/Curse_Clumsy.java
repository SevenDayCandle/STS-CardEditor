package pinacolada.cards.pcl.curse;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.fields.PCLCardTag;

public class Curse_Clumsy extends PCLCard
{
    public static final PCLCardData DATA = register(Curse_Clumsy.class)
            .setCurse(-2, PCLCardTarget.None, false)
            .setTags(PCLCardTag.Unplayable, PCLCardTag.Ethereal)
            .setAffinities(PCLAffinity.Purple);

    public Curse_Clumsy()
    {
        super(DATA);
    }
}