package pinacolada.skills.skills.base.moves;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.EUIRM;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.skills.PMove;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;
import pinacolada.skills.fields.PField_CardCategory;

public abstract class PMove_Select extends PMove<PField_CardCategory>
{
    public PMove_Select(PSkillSaveData content)
    {
        super(content);
    }

    public PMove_Select(PSkillData<PField_CardCategory> data, int amount, PCLCardGroupHelper... h)
    {
        super(data, PCLCardTarget.None, amount);
        fields.setCardGroup(h);
    }

    public PMove_Select(PSkillData<PField_CardCategory> data, PCLCardTarget target, int amount, PCLCardGroupHelper... h)
    {
        super(data, target, amount);
        fields.setCardGroup(h);
    }

    @Override
    public String getSampleText()
    {
        return EUIRM.strings.verbNoun(getActionTitle(), "X");
    }

    @Override
    public void use(PCLUseInfo info)
    {
        fields.getGenericPileAction(getAction(), info)
                .addCallback(cards -> {
                    if (this.childEffect != null)
                    {
                        info.setData(cards);
                        this.childEffect.use(info);
                    }
                });
    }

    @Override
    public String getSubText()
    {
        return useParent ? EUIRM.strings.verbNoun(getActionTitle(), getInheritedString()) :
                !fields.groupTypes.isEmpty() ? TEXT.actions.genericFrom(getActionTitle(), amount <= 0 ? TEXT.subjects.all : getAmountRawString(), fields.getFullCardString(), fields.getGroupString())
                        : EUIRM.strings.verbNoun(getActionTitle(), TEXT.subjects.thisObj);
    }

    protected String getActionTitle()
    {
        return getActionTooltip().title;
    }

    public abstract EUITooltip getActionTooltip();
    public abstract FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction();
}
