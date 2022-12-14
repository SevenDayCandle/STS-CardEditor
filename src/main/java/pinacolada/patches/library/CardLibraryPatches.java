package pinacolada.patches.library;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import extendedui.EUIUtils;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCustomCardSlot;
import pinacolada.cards.base.ReplacementCardBuilder;
import pinacolada.cards.pcl.curse.*;
import pinacolada.cards.pcl.special.QuestionMark;
import pinacolada.cards.pcl.status.*;
import pinacolada.resources.PGR;
import pinacolada.utilities.GameUtilities;
import pinacolada.utilities.RandomizedList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CardLibraryPatches
{
    public static String[] splitCardID(String cardID)
    {
        return cardID.split(Pattern.quote(":"), 2);
    }

    // TODO allow sub-mods to add their own replacements
    public static PCLCardData getStandardReplacement(String cardID)
    {
        // Base game card replacements
        switch (cardID)
        {
            case Apparition.ID:
                return pinacolada.cards.pcl.replacement.Apparition.DATA;
            case AscendersBane.ID:
                return Curse_AscendersBane.DATA;
            case Bite.ID:
                return pinacolada.cards.pcl.replacement.Bite.DATA;
            case Burn.ID:
                return Status_Burn.DATA;
            case Clumsy.ID:
                return Curse_Clumsy.DATA;
            case Dazed.ID:
                return Status_Dazed.DATA;
            case Decay.ID:
                return Curse_Decay.DATA;
            case Doubt.ID:
                return Curse_Doubt.DATA;
            case Injury.ID:
                return Curse_Injury.DATA;
            case Insight.ID:
                return pinacolada.cards.pcl.replacement.Insight.DATA;
            case JAX.ID:
                return pinacolada.cards.pcl.replacement.JAX.DATA;
            case Madness.ID:
                return pinacolada.cards.pcl.colorless.Madness.DATA;
            case Miracle.ID:
                return pinacolada.cards.pcl.replacement.Miracle.DATA;
            case Normality.ID:
                return Curse_Normality.DATA;
            case Pain.ID:
                return Curse_Pain.DATA;
            case Parasite.ID:
                return Curse_Parasite.DATA;
            case Regret.ID:
                return Curse_Regret.DATA;
            case RitualDagger.ID:
                return pinacolada.cards.pcl.replacement.RitualDagger.DATA;
            case Shame.ID:
                return Curse_Shame.DATA;
            case Slimed.ID:
                return Status_Slimed.DATA;
            case VoidCard.ID:
                return Status_Void.DATA;
            case Wound.ID:
                return Status_Wound.DATA;
            case Writhe.ID:
                return Curse_Writhe.DATA;

            default:
                return null;
        }
    }

    protected static AbstractCard tryCreateReplacementForPCL(AbstractCard card)
    {
        if (card instanceof PCLCard)
        {
            return card;
        }
        PCLCardData data = getStandardReplacement(card.cardID);
        if (data != null)
        {
            return data.makeCopy(card.upgraded);
        }
        else if (PGR.core.config.replaceCardsPCL.get())
        {
            AbstractCard c = new ReplacementCardBuilder(card, true).build();
            if (card.upgraded)
            {
                c.upgrade();
            }
            return c;
        }
        return card;
    }

    public static void tryReplace(AbstractCard[] card)
    {
        AbstractPlayer.PlayerClass playerClass = GameUtilities.getPlayerClass();
        if (GameUtilities.isPCLPlayerClass(playerClass))
        {
            card[0] = tryCreateReplacementForPCL(card[0]);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
    public static class CardLibraryPatches_GetCard
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix(String key)
        {
            if (PGR.isLoaded())
            {
                if (GameUtilities.isPCLPlayerClass())
                {
                    final PCLCardData data = getStandardReplacement(key);
                    if (data != null)
                    {
                        return SpireReturn.Return(data.makeCopy(false));
                    }
                }

                PCLCustomCardSlot slot = PCLCustomCardSlot.get(key);
                if (slot != null)
                {
                    return SpireReturn.Return(slot.getBuilder(0).build(true));
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
    public static class CardLibraryPatches_GetCopy
    {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, String key, int upgradeTime, int misc)
        {
            // If a card is not found, the base game will put a Madness in its place. This change makes it easier for players to see what card is missing
            if (__result instanceof Madness && !Madness.ID.equals(key))
            {
                __result = new QuestionMark();
                __result.name = __result.originalName = key;
                EUIUtils.logError(CardLibrary.class, "Card not found: " + key);
            }
            if (PGR.core.config.replaceCardsPCL.get() && GameUtilities.isPCLPlayerClass())
            {
                return tryCreateReplacementForPCL(__result);
            }
            return __result;
        }

        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix(String key, int upgradeTime, int misc)
        {
            if (key.equals(AscendersBane.ID) && GameUtilities.isPCLPlayerClass())
            {
                return SpireReturn.Return(Curse_AscendersBane.DATA.makeCopy(false));
            }
            else if (GameUtilities.isPCLPlayerClass())
            {
                PCLCardData data = getStandardReplacement(key);
                if (data != null)
                {
                    return SpireReturn.Return(data.makeCopy(upgradeTime > 0));
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {})
    public static class CardLibraryPatches_GetCurse
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix()
        {
            return CardLibraryPatches_GetCurse2.postfix(null, AbstractDungeon.cardRng);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {AbstractCard.class, Random.class})
    public static class CardLibraryPatches_GetCurse2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> postfix(AbstractCard ignore, Random rng)
        {
            final RandomizedList<String> cards = new RandomizedList<>();
            final HashMap<String, AbstractCard> curses = ReflectionHacks.getPrivateStatic(CardLibrary.class, "curses");
            for (Map.Entry<String, AbstractCard> entry : curses.entrySet())
            {
                final AbstractCard c = entry.getValue();
                final PCLCardData replacement = (PGR.isLoaded() && GameUtilities.isPCLPlayerClass() && PGR.core.config.replaceCardsPCL.get()) ? getStandardReplacement(c.cardID) : null;
                if (c.rarity != AbstractCard.CardRarity.SPECIAL && (ignore == null || !c.cardID.equals(ignore.cardID)) && replacement == null)
                {
                    cards.add(entry.getKey());
                }
            }

            return SpireReturn.Return(CardLibrary.cards.get(cards.retrieve(rng)));
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard", paramtypez = {AbstractCard.CardType.class, AbstractCard.CardRarity.class})
    public static class CardLibraryPatches_GetAnyColorCard
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix(AbstractCard.CardType type, AbstractCard.CardRarity rarity)
        {
            if (GameUtilities.isPCLPlayerClass())
            {
                return SpireReturn.Return(GameUtilities.getAnyColorCardFiltered(rarity, type, false));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard", paramtypez = {AbstractCard.CardRarity.class})
    public static class CardLibraryPatches_GetAnyColorCard2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix(AbstractCard.CardRarity rarity)
        {
            if (GameUtilities.isPCLPlayerClass())
            {
                return SpireReturn.Return(GameUtilities.getAnyColorCardFiltered(rarity, null, true));
            }
            return SpireReturn.Continue();
        }
    }
}
