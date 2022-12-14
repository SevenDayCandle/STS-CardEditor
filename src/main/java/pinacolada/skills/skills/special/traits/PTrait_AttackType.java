package pinacolada.skills.skills.special.traits;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.interfaces.markers.Hidden;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.PTrait;
import pinacolada.skills.PTrigger;
import pinacolada.skills.fields.PField_AttackType;

// Only used for augments
public class PTrait_AttackType extends PTrait<PField_AttackType> implements Hidden
{

    public static final PSkillData<PField_AttackType> DATA = register(PTrait_AttackType.class, PField_AttackType.class);

    public PTrait_AttackType()
    {
        this(PCLAttackType.Normal);
    }

    public PTrait_AttackType(PSkillSaveData content)
    {
        super(content);
    }

    public PTrait_AttackType(PCLAttackType... type)
    {
        super(DATA);
        fields.setAttackType(type);
    }

    @Override
    public String getSubText()
    {
        return hasParentType(PTrigger.class) ? getSubDescText() :
                fields.random ? TEXT.actions.remove(getSubDescText()) : TEXT.actions.has(getSubDescText());
    }

    @Override
    public void applyToCard(AbstractCard c, boolean conditionMet)
    {
        if (c instanceof PCLCard && fields.attackTypes.size() > 0)
        {
            ((PCLCard) c).setAttackType(conditionMet ? fields.attackTypes.get(0) : ((PCLCard) c).cardData.attackType);
        }
    }

    @Override
    public String getSubDescText()
    {
        return fields.attackTypes.size() > 0 ? fields.attackTypes.get(0).getTooltip().getTitleOrIcon() : "";
    }

    @Override
    public String getSubSampleText()
    {
        return TEXT.cardEditor.attackType;
    }
}
