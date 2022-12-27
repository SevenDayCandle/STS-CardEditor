package pinacolada.skills.skills.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.PCLActionWithCallback;
import pinacolada.actions.pileSelection.FetchFromPile;
import pinacolada.actions.pileSelection.ScoutCards;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.resources.PGR;
import pinacolada.skills.PSkillData;
import pinacolada.skills.PSkillSaveData;

import java.util.ArrayList;

import static pinacolada.skills.PSkill.PCLEffectType.CardGroupFull;

public class PMod_ScoutPerCard extends PMod_Do
{

    public static final PSkillData DATA = register(PMod_ScoutPerCard.class, CardGroupFull)
            .selfTarget()
            .setGroups(PCLCardGroupHelper.DrawPile);

    public PMod_ScoutPerCard(PSkillSaveData content)
    {
        super(content);
    }

    public PMod_ScoutPerCard()
    {
        super(DATA);
    }

    public PMod_ScoutPerCard(int amount)
    {
        super(DATA, PCLCardTarget.None, amount);
    }

    @Override
    protected PCLActionWithCallback<ArrayList<AbstractCard>> createPileAction(PCLUseInfo info)
    {
        return new ScoutCards(getName(), amount);
    }

    @Override
    public EUITooltip getActionTooltip()
    {
        return PGR.core.tooltips.scout;
    }

    @Override
    public FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> getAction()
    {
        return FetchFromPile::new;
    }
}
