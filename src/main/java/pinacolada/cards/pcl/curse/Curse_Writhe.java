package pinacolada.cards.pcl.curse;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.skills.PCond;
import pinacolada.skills.skills.base.moves.PMove_Cycle;

public class Curse_Writhe extends PCLCard
{
    public static final PCLCardData DATA = register(Curse_Writhe.class)
            .setCurse(-2, PCLCardTarget.None, false, true)
            .setTags(PCLCardTag.Unplayable.make(), PCLCardTag.Innate.make(-1))
            .setAffinities(PCLAffinity.Purple);

    public Curse_Writhe()
    {
        super(DATA);
    }

    @Override
    public void setup(Object input)
    {
        addUseMove(PCond.onExhaust(), new PMove_Cycle(1));
    }
}