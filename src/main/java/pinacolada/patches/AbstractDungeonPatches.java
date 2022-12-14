package pinacolada.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import pinacolada.cards.pcl.curse.Curse_AscendersBane;
import pinacolada.misc.CombatManager;
import pinacolada.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Map;

public class AbstractDungeonPatches
{
/*    @SpirePatch(clz = AbstractDungeon.class, method = "getEvent", paramtypez = Random.class)
    public static class AbstractDungeonPatches_GetEvent
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractEvent> prefix(Random rng)
        {

            AbstractEvent event = PCLEvent.GenerateSpecialEvent(CardCrawlGame.dungeon, rng, GameUtilities.IsPCLPlayerClass() || PGR.core.Config.EnableEventsForOtherCharacters.Get());
            if (event != null)
            {
                return SpireReturn.Return(event);
            }


            return SpireReturn.Continue();
        }
    }*/

    @SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class AbstractDungeonPatches_DungeonTransitionSetup
    {
        @SpirePostfixPatch
        public static void postfix()
        {
            if (!GameUtilities.isPCLPlayerClass())
            {
                return;
            }

            final ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
            for (int i = 0; i < cards.size(); i++)
            {
                if (cards.get(i).cardID.equals(AscendersBane.ID))
                {
                    cards.set(i, Curse_AscendersBane.DATA.makeCopy(false));
                    UnlockTracker.markCardAsSeen(Curse_AscendersBane.DATA.ID);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "addCurseCards", optional = true)
    public static class AbstractDungeonPatches_AddCurseCards
    {
        @SpirePrefixPatch
        public static SpireReturn prefix()
        {
            for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet())
            {
                AbstractCard c = entry.getValue();
                if (c.type == AbstractCard.CardType.CURSE && c.rarity != AbstractCard.CardRarity.SPECIAL)
                {
                    AbstractDungeon.curseCardPool.addToTop(c);
                }
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower", optional = true)
    public static class AbstractDungeonPatches_OnModifyPower
    {
        @SpireInstrumentPatch
        public static ExprEditor instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("hasPower"))
                    {
                        //onModifyPower checks if the player has focus to update orbs, it doesn't update them if focus is reduced to 0...
                        m.replace("$_ = true;");
                    }
                }
            };
        }

        @SpirePostfixPatch
        public static void postfix()
        {
            if (GameUtilities.inBattle())
            {
                CombatManager.summons.applyPowers();
            }
        }
    }

    // The vanilla GetRandomCard from AbstractDungeon does an infinite loop (e.g. in the shop) if there are no uncommon and rare power cards in the pool...
    @SpirePatch(clz = AbstractDungeon.class, method = "getCardFromPool", optional = true)
    public static class AbstractDungeonPatches_GetCardFromPool
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> prefix(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean useRng)
        {
            return SpireReturn.Return(GameUtilities.getRandomCard(rarity, type, useRng, true));
        }
    }
}