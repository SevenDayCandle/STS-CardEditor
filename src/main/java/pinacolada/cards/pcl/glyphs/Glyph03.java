package pinacolada.cards.pcl.glyphs;

import pinacolada.cards.base.PCLCardData;
import pinacolada.skills.PTrait;
import pinacolada.skills.PTrigger;
import pinacolada.skills.skills.PMultiTrait;
import pinacolada.skills.skills.base.conditions.PCond_IfHasAffinity;
import pinacolada.skills.skills.base.traits.PTrait_BlockMultiplier;
import pinacolada.skills.skills.base.traits.PTrait_DamageMultiplier;

public class Glyph03 extends Glyph
{
    public static final PCLCardData DATA = registerInternal(Glyph03.class);

    public Glyph03()
    {
        super(DATA);
    }

    public void setup(Object input)
    {
        addGainPower(PTrigger.passive(new PCond_IfHasAffinity(randomAffinity()),
                PMultiTrait.join(
                        (PTrait) new PTrait_DamageMultiplier(-33).setUpgrade(-1),
                        (PTrait) new PTrait_BlockMultiplier(-33).setUpgrade(-1)
                )));
    }
}