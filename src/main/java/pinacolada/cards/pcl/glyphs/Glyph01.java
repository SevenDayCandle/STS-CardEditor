package pinacolada.cards.pcl.glyphs;

import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.skills.PMove;

public class Glyph01 extends Glyph
{
    public static final PCLCardData DATA = registerInternal(Glyph01.class);

    public Glyph01()
    {
        super(DATA);
    }

    public void setup(Object input)
    {
        addUseMove(PMove.applyToEnemies(1, PCLPowerHelper.Ritual).setUpgrade(1));
    }
}