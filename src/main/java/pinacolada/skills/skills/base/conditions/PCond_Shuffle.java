package pinacolada.skills.skills.base.conditions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.skills.PCond;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_Empty;

// TODO make into delegate
public class PCond_Shuffle extends PCond<PField_Empty>
{
    public static final PSkillData<PField_Empty> DATA = register(PCond_Shuffle.class, PField_Empty.class, 1, 1)
            .selfTarget();

    public PCond_Shuffle()
    {
        super(DATA, PCLCardTarget.None, 0);
    }

    public PCond_Shuffle(PSkillSaveData content)
    {
        super(content);
    }

    // This should not activate the child effect when played normally

    @Override
    public String getSampleText()
    {
        return TEXT.subjects.shuffleYourDeck;
    }

    @Override
    public String getSubText()
    {
        return TEXT.conditions.wheneverYou(TEXT.subjects.shuffleYourDeck);
    }

    @Override
    public void use(PCLUseInfo info)
    {
    }

    @Override
    public void use(PCLUseInfo info, int index)
    {
    }

    @Override
    public boolean canPlay(AbstractCard card, AbstractMonster m)
    {
        return true;
    }

    @Override
    public boolean triggerOnShuffle(boolean isUsing)
    {
        if (this.childEffect != null && isUsing)
        {
            this.childEffect.use(makeInfo(null));
        }
        return true;
    }

    @Override
    public boolean checkCondition(PCLUseInfo info, boolean isUsing, boolean fromTrigger)
    {
        return fromTrigger;
    }
}
