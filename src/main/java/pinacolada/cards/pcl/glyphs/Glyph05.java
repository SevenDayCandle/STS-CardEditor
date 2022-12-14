package pinacolada.cards.pcl.glyphs;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.misc.CombatManager;
import pinacolada.skills.skills.PSpecialSkill;
import pinacolada.ui.combat.PCLPlayerMeter;

public class Glyph05 extends Glyph
{
    public static final PCLCardData DATA = registerInternal(Glyph05.class);

    public Glyph05()
    {
        super(DATA);
    }

    public void action(PSpecialSkill move, PCLUseInfo info)
    {
        for (PCLAffinity af : move.fields.affinities)
        {
            for (PCLPlayerMeter meter : CombatManager.playerSystem.getMeters())
            {
                meter.disableAffinity(af);
            }
        }
    }

    public void setup(Object input)
    {
        addSpecialMove(0, this::action, 1).edit(f -> f.setAffinity(randomAffinity())).setCustomUpgrade((s, f, u) -> {
            if (u >= 50 && s.fields.affinities.size() <= 1)
            {
                PCLAffinity newAf = randomAffinity();
                if (newAf != null && !s.fields.affinities.contains(newAf))
                {
                    s.fields.addAffinity(newAf);
                }
            }
        });
    }
}