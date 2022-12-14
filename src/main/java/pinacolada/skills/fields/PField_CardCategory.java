package pinacolada.skills.fields;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import extendedui.EUIGameUtils;
import extendedui.EUIRM;
import extendedui.EUIUtils;
import extendedui.interfaces.delegates.FuncT1;
import extendedui.interfaces.delegates.FuncT4;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.actions.PCLActionWithCallback;
import pinacolada.actions.PCLActions;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardSelection;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLUseInfo;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLCoreStrings;
import pinacolada.skills.PSkill;
import pinacolada.ui.cardEditor.PCLCustomCardEffectEditor;
import pinacolada.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PField_CardCategory extends PField_CardID
{
    public ArrayList<AbstractCard.CardRarity> rarities = new ArrayList<>();
    public ArrayList<AbstractCard.CardType> types = new ArrayList<>();
    public ArrayList<PCLAffinity> affinities = new ArrayList<>();
    public ArrayList<PCLCardTag> tags = new ArrayList<>();

    public PField_CardCategory()
    {
        super();
    }

    public PField_CardCategory(PField_CardCategory other)
    {
        super(other);
        setAffinity(other.affinities);
        setRarity(other.rarities);
        setType(other.types);
        setTag(other.tags);
    }

    @Override
    public boolean equals(PField other)
    {
        return super.equals(other)
                && affinities.equals(((PField_CardCategory) other).affinities)
                && rarities.equals(((PField_CardCategory) other).rarities)
                && types.equals(((PField_CardCategory) other).types)
                && tags.equals(((PField_CardCategory) other).tags);
    }

    @Override
    public PField_CardCategory makeCopy()
    {
        return new PField_CardCategory(this);
    }

    public void setupEditor(PCLCustomCardEffectEditor editor)
    {
        editor.registerPile(groupTypes);
        editor.registerRarity(rarities);
        editor.registerType(types);
        editor.registerAffinity(affinities);
        editor.registerTag(tags);
        editor.registerBoolean(PGR.core.strings.cardEditor.random, v -> random = v, random);
    }

    public PField_CardCategory addAffinity(PCLAffinity... affinities)
    {
        this.affinities.addAll(Arrays.asList(affinities));
        return this;
    }

    public PField_CardCategory addTag(PCLCardTag... tags)
    {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public PField_CardCategory setAffinity(PCLAffinity... affinities)
    {
        return setAffinity(Arrays.asList(affinities));
    }

    public PField_CardCategory setAffinity(List<PCLAffinity> affinities)
    {
        this.affinities.clear();
        this.affinities.addAll(affinities);
        return this;
    }

    public PField_CardCategory setRarity(AbstractCard.CardRarity... types)
    {
        return setRarity(Arrays.asList(types));
    }

    public PField_CardCategory setRarity(List<AbstractCard.CardRarity> types)
    {
        this.rarities.clear();
        this.rarities.addAll(types);
        return this;
    }

    public PField_CardCategory setType(AbstractCard.CardType... types)
    {
        return setType(Arrays.asList(types));
    }

    public PField_CardCategory setType(List<AbstractCard.CardType> types)
    {
        this.types.clear();
        this.types.addAll(types);
        return this;
    }

    public PField_CardCategory setTag(PCLCardTag... nt)
    {
        return setTag(Arrays.asList(nt));
    }

    public PField_CardCategory setTag(List<PCLCardTag> nt)
    {
        this.tags.clear();
        this.tags.addAll(nt);
        return this;
    }

    public FuncT1<Boolean, AbstractCard> getFullCardFilter()
    {
        return !cardIDs.isEmpty() ? c -> EUIUtils.any(cardIDs, id -> id.equals(c.cardID)) :
                (c -> (affinities.isEmpty() || GameUtilities.hasAnyAffinity(c, affinities))
                && (rarities.isEmpty() || rarities.contains(c.rarity))
                && (tags.isEmpty() || EUIUtils.any(tags, t -> t.has(c)))
                && (types.isEmpty() || types.contains(c.type)));
    }

    public String getFullCardAndString(Object value)
    {
        return getFullCardXString(PField::getAffinityAndString, PCLCoreStrings::joinWithAnd, value);
    }

    public String getFullCardAndString()
    {
        return getFullCardAndString(skill.getAmountRawString());
    }

    public String getFullCardOrString(Object value)
    {
        return getFullCardXString(PField::getAffinityOrString, PCLCoreStrings::joinWithOr, value);
    }

    public String getFullCardOrString()
    {
        return getFullCardOrString(skill.getAmountRawString());
    }

    public String getFullCardString()
    {
        return getFullCardString(skill.getAmountRawString());
    }

    public String getFullCardString(Object value)
    {
        return !cardIDs.isEmpty() ? getCardIDOrString() : random ? PSkill.TEXT.subjects.randomX(getFullCardOrString(value)) : getFullCardOrString(value);
    }

    public final String getFullCardXString(FuncT1<String, ArrayList<PCLAffinity>> affinityFunc, FuncT1<String, ArrayList<String>> joinFunc, Object value)
    {
        ArrayList<String> stringsToJoin = new ArrayList<>();
        if (!affinities.isEmpty())
        {
            stringsToJoin.add(affinityFunc.invoke(affinities));
        }
        if (!tags.isEmpty())
        {
            stringsToJoin.add(joinFunc.invoke(EUIUtils.map(tags, tag -> tag.getTip().getTitleOrIcon())));
        }
        if (!rarities.isEmpty())
        {
            stringsToJoin.add(joinFunc.invoke(EUIUtils.map(rarities, type -> EUIGameUtils.textForRarity(type))));
        }
        if (!types.isEmpty())
        {
            stringsToJoin.add(joinFunc.invoke(EUIUtils.map(types, type -> PCLCoreStrings.plural(GameUtilities.tooltipForType(type), value))));
        }
        else
        {
            stringsToJoin.add(EUIUtils.format(PSkill.TEXT.subjects.cardN, value));
        }

        return EUIUtils.joinStrings(" ", stringsToJoin);
    }

    public String makeFullString(EUITooltip tooltip)
    {
        String tooltipTitle = tooltip.title;
        return skill.useParent ? EUIRM.strings.verbNoun(tooltipTitle, skill.getInheritedString()) :
                !groupTypes.isEmpty() ? TEXT.actions.genericFrom(tooltipTitle, skill.amount <= 0 ? TEXT.subjects.all : skill.getAmountRawString(), !cardIDs.isEmpty() ? getCardIDOrString(cardIDs) : getFullCardString(), getGroupString())
                        : EUIRM.strings.verbNoun(tooltipTitle, TEXT.subjects.thisObj);
    }

    protected SelectFromPile createAction(FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> action, PCLUseInfo info)
    {
        CardGroup[] g = getCardGroup(info);
        return action.invoke(skill.getName(), skill.target.getTarget(info.source, info.target), skill.useParent && g.length > 0 ? g[0].size() : skill.amount <= 0 ? Integer.MAX_VALUE : skill.amount, g);
    }

    public PCLActionWithCallback<ArrayList<AbstractCard>> getGenericPileAction(FuncT4<SelectFromPile, String, AbstractCreature, Integer, CardGroup[]> action, PCLUseInfo info)
    {
        if (!skill.useParent && groupTypes.isEmpty())
        {
            return PCLActions.last.add(createAction(action, info))
                    .setOptions(true, true);
        }
        else
        {
            return skill.getActions().add(createAction(action, info))
                    .setFilter(getFullCardFilter())
                    .setOptions(random || skill.amount <= 0 ? CardSelection.Random : origin, !forced);
        }
    }
}
