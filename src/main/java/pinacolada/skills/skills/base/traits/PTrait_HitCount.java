package pinacolada.skills.skills.base.traits;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.PTrait;
import pinacolada.skills.fields.PField_Empty;

public class PTrait_HitCount extends PTrait<PField_Empty>
{

    public static final PSkillData<PField_Empty> DATA = register(PTrait_HitCount.class, PField_Empty.class);

    public PTrait_HitCount()
    {
        this(1);
    }

    public PTrait_HitCount(PSkillSaveData content)
    {
        super(content);
    }

    public PTrait_HitCount(int amount)
    {
        super(DATA, amount);
    }

    @Override
    public String getSubDescText()
    {
        return TEXT.subjects.hits;
    }

    @Override
    public String getSubSampleText()
    {
        return TEXT.subjects.hits;
    }

    @Override
    public float modifyHitCount(PCLCard card, AbstractMonster m, float amount)
    {
        return amount + this.amount;
    }

    @Override
    public boolean isDetrimental()
    {
        return amount < 0;
    }
}
