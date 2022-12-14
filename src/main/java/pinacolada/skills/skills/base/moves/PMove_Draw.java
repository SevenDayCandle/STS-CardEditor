package pinacolada.skills.skills.base.moves;

import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.skills.PMove;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public class PMove_Draw extends PMove<PField_CardCategory>
{
    public static final PSkillData<PField_CardCategory> DATA = register(PMove_Draw.class, PField_CardCategory.class)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.DrawPile);

    public PMove_Draw()
    {
        this(1);
    }

    public PMove_Draw(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_Draw(int amount)
    {
        super(DATA, PCLCardTarget.None, amount);
    }

    @Override
    public String getSampleText()
    {
        return TEXT.actions.draw("X");
    }

    @Override
    public void use(PCLUseInfo info)
    {
        getActions().draw(amount)
                .setFilter(fields.getFullCardFilter(), true)
                .addCallback(ca -> {
                    if (this.childEffect != null)
                    {
                        info.setData(ca);
                        this.childEffect.use(info);
                    }
                });
    }

    @Override
    public String getSubText()
    {
        return TEXT.actions.drawType(getAmountRawString(), fields.getFullCardString());
    }
}
