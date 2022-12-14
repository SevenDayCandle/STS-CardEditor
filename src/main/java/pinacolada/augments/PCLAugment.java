package pinacolada.augments;


import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import extendedui.EUIRM;
import extendedui.EUIUtils;
import extendedui.interfaces.markers.TooltipProvider;
import extendedui.ui.tooltips.EUITooltip;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.fields.PCLCardTag;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLCoreStrings;
import pinacolada.skills.PSkill;
import pinacolada.utilities.WeightedList;

import java.lang.reflect.Constructor;
import java.util.*;

public abstract class PCLAugment implements TooltipProvider
{
    private static final HashMap<String, PCLAugmentData> AUGMENT_MAP = new HashMap<>();
    private static final String PREFIX_AUGMENTS = "pinacolada.augments.augments";
    public static final int WEIGHT_MODIFIER = 3;

    public PCLAugmentData data;
    public String ID;
    public PSkill skill;
    public PCLCard card;

    public PCLAugment(PCLAugmentData data)
    {
        this(data, data.skill);
    }

    public PCLAugment(PCLAugmentData data, PSkill skill)
    {
        this.data = data;
        this.ID = data.ID;
        this.skill = skill.makeCopy();
    }

    public static String createFullID(Class<? extends PCLAugment> type)
    {
        return PGR.core.createID(type.getSimpleName());
    }

    public static PCLAugmentData get(String id)
    {
        return AUGMENT_MAP.get(id);
    }

    public static Set<String> getIDs()
    {
        return AUGMENT_MAP.keySet();
    }

    public static Collection<PCLAugmentData> getValues()
    {
        return AUGMENT_MAP.values();
    }

    public static int getWeight(PCLAugmentWeights weights, PCLAugmentData data)
    {
        return getWeight(weights, data, false);
    }

    public static int getWeight(PCLAugmentWeights weights, PCLAugmentData data, boolean allowSpecial)
    {
        return (data.isSpecial && !allowSpecial ? 0 : weights.getWeight(data.affinity) - Math.max(0, data.tier - weights.getRareModifier())) * WEIGHT_MODIFIER;
    }

    public static PCLAugmentData getWeighted(Random rng, PCLAugmentWeights weights)
    {
        return getWeightedList(weights).retrieve(rng);
    }

    public static WeightedList<PCLAugmentData> getWeightedList(PCLAugmentWeights weights)
    {
        return getWeightedList(weights, false);
    }

    public static WeightedList<PCLAugmentData> getWeightedList(PCLAugmentWeights weights, boolean allowSpecial)
    {
        final WeightedList<PCLAugmentData> weightedList = new WeightedList<>();
        for (PCLAugmentData data : AUGMENT_MAP.values())
        {
            int weight = getWeight(weights, data, allowSpecial);
            if (weight > 0)
            {
                weightedList.add(data, weight);
            }
        }
        return weightedList;
    }

    // Each ID must be called at least once to have it selectable in the console
    public static void initialize()
    {
        ArrayList<String> effectClassNames = PGR.getClassNamesFromJarFile(PREFIX_AUGMENTS);
        for (String s : effectClassNames)
        {
            try
            {

                Class<?> name = Class.forName(s);
                PCLAugmentData data = ReflectionHacks.getPrivateStatic(name, "DATA");
                EUIUtils.logInfoIfDebug(PCLAugment.class, "Adding effect " + data.ID);
            }
            catch (Exception e)
            {
                EUIUtils.logError(PCLAugment.class, "Failed to load " + s + ": " + e.getLocalizedMessage());
            }
        }
    }

    public static PCLAugmentData register(Class<? extends PCLAugment> type, int tier, PCLAffinity affinity)
    {
        String id = PGR.core.createID(type.getSimpleName());
        PCLAugmentData d = new PCLAugmentData(id, type, tier, affinity);
        AUGMENT_MAP.put(id, d);
        return d;
    }

    public static PCLAugmentReqs setAffinities(PCLAffinity... values)
    {
        return new PCLAugmentReqs().setAffinities(values);
    }

    public static PCLAugmentReqs setAffinitiesNot(PCLAffinity... values)
    {
        return new PCLAugmentReqs().setAffinitiesNot(values);
    }

    public static PCLAugmentReqs setTargets(PCLCardTarget... values)
    {
        return new PCLAugmentReqs().setTargets(values);
    }

    public static PCLAugmentReqs setRarities(AbstractCard.CardRarity... values)
    {
        return new PCLAugmentReqs().setRarities(values);
    }

    public static PCLAugmentReqs setTags(PCLCardTag... values)
    {
        return new PCLAugmentReqs().setTags(values);
    }

    public static PCLAugmentReqs setTagsNot(PCLCardTag... values)
    {
        return new PCLAugmentReqs().setTagsNot(values);
    }

    public static PCLAugmentReqs setTypes(AbstractCard.CardType... values)
    {
        return new PCLAugmentReqs().setTypes(values);
    }

    public void addToCard(PCLCard c)
    {
        if (canApplyImpl(c))
        {
            c.addAugment(this);
        }
    }

    public boolean canApply(AbstractCard c)
    {
        return c instanceof PCLCard && canApplyImpl((PCLCard) c);
    }

    /* An augment can only be applied if
    *   1. Card has free augment slots
    *   2. Augment requirements are passed
    *   3. Card doesn't already have an augment of its color
    */
    protected boolean canApplyImpl(PCLCard c)
    {
        return c != null && (data.reqs == null || data.reqs.check(c)) && c.getFreeAugmentSlot() >= 0 && !EUIUtils.any(c.getAugments(), a -> a.data.affinity == data.affinity);
    }

    public boolean canRemove()
    {
        return !data.isSpecial;
    }

    public Color getColor()
    {
        switch (data.affinity)
        {
            case Red:
                return Color.FIREBRICK;
            case Green:
                return Color.FOREST;
            case Blue:
                return Color.SKY;
            case Orange:
                return Color.GOLDENROD;
            case Yellow:
                return Color.CHARTREUSE;
            case Purple:
                return Color.VIOLET;
            case Silver:
                return Color.GRAY;
            case Star:
                return Color.PINK;
        }
        return Color.WHITE;
    }

    public String getFullText()
    {
        return EUIUtils.joinTrueStrings(EUIUtils.SPLIT_LINE,
                EUIRM.strings.generic2(data.affinity.getTooltip(), PCLCoreStrings.headerString(PGR.core.tooltips.level.title, data.tier)),
                data.isSpecial ? "{#r:" + PGR.core.tooltips.specialAugment + "}" : null,
                PCLCoreStrings.headerString(PGR.core.strings.cardMods.requirement, getReqsString()),
                getPowerText());
    }

    public String getName()
    {
        return data.strings.NAME;
    }

    public String getPowerText()
    {
        return skill.getPowerText();
    }

    public String getReqsString()
    {
        return data.reqs == null ? PGR.core.strings.combat.na : data.reqs.getString();
    }

    public String getText()
    {
        return skill.getText();
    }

    // TODO More textures
    public Texture getTexture()
    {
        return PGR.core.images.augments.augmentBasic.texture();
    }

    public EUITooltip getTip()
    {
        return new EUITooltip(getName(), getFullText());
    }

    @Override
    public List<EUITooltip> getTips()
    {
        return Collections.singletonList(getTip());
    }

    public PCLAugment makeCopy()
    {
        PCLAugment copy = null;
        try
        {
            Constructor<? extends PCLAugment> c = EUIUtils.tryGetConstructor(this.getClass(), PSkill.class);
            if (c != null)
            {
                copy = c.newInstance(skill);
                copy.card = card;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            EUIUtils.logError(this, "Failed to copy");
        }
        return copy;
    }

    public void onAddToCard(PCLCard c)
    {
        this.skill.setSource(c).onAddToCard(c);
        card = c;
    }

    public void onRemoveFromCard(PCLCard c)
    {
        this.skill.onRemoveFromCard(c);
        card = null;
    }
}
