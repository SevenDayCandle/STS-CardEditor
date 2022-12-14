package pinacolada.skills.skills.base.moves;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import extendedui.EUIRM;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.skills.PMove;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Orb;
import pinacolada.utilities.GameUtilities;

import java.util.List;

public class PMove_ChannelOrb extends PMove<PField_Orb>
{
    public static final PSkillData<PField_Orb> DATA = register(PMove_ChannelOrb.class, PField_Orb.class)
            .setExtra(-1, DEFAULT_MAX)
            .selfTarget();

    public PMove_ChannelOrb()
    {
        this(1);
    }

    public PMove_ChannelOrb(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_ChannelOrb(int amount, PCLOrbHelper... orb)
    {
        super(DATA, PCLCardTarget.None, amount);
        fields.setOrb(orb);
    }

    public PMove_ChannelOrb(int amount, int extra, PCLOrbHelper... orb)
    {
        super(DATA, PCLCardTarget.None, amount, extra);
        fields.setOrb(orb);
    }

    @Override
    public String getSampleText()
    {
        return TEXT.actions.channelX("X", TEXT.cardEditor.orbs);
    }

    @Override
    public void use(PCLUseInfo info)
    {
        if (!fields.orbs.isEmpty())
        {
            if (fields.random)
            {
                PCLOrbHelper orb = GameUtilities.getRandomElement(fields.orbs);
                if (orb != null)
                {
                    getActions().channelOrbs(orb, amount).addCallback(this::modifyFocus);
                }
            }
            else
            {
                for (PCLOrbHelper orb : fields.orbs)
                {
                    getActions().channelOrbs(orb, amount).addCallback(this::modifyFocus);
                }
            }
        }
        else
        {
            getActions().channelRandomOrbs(amount).addCallback(this::modifyFocus);
        }
        super.use(info);
    }

    @Override
    public String getSubText()
    {
        String base = fields.getOrbAmountString();
        if (extra > 0)
        {
            base = TEXT.subjects.withX(base, EUIRM.strings.numNoun("+" + getExtraRawString(), PGR.core.tooltips.focus.title));
        }
        return fields.random ? TEXT.subjects.randomly(TEXT.actions.channelX(getAmountRawString(), base))
                : TEXT.actions.channelX(getAmountRawString(), base);
    }

    protected void modifyFocus(List<AbstractOrb> orbs)
    {
        if (extra > 0)
        {
            for (AbstractOrb o : orbs)
            {
                GameUtilities.modifyOrbBaseFocus(o, extra, true, false);
            }
        }
    }
}
