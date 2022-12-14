package pinacolada.resources;

import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import extendedui.EUIUtils;
import pinacolada.augments.AugmentStrings;
import pinacolada.commands.*;
import pinacolada.interfaces.markers.Hidden;
import pinacolada.resources.pcl.PCLCoreResources;
import pinacolada.utilities.GameUtilities;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PGR
{
    public static final String BASE_PREFIX = "pcl";
    public static final String PREFIX_CARDS = "pinacolada.cards.";
    public static final String PREFIX_POTIONS = "pinacolada.potions.";
    public static final String PREFIX_POWERS = "pinacolada.powers.";
    public static final String PREFIX_RELIC = "pinacolada.relics.";

    protected static final HashMap<AbstractCard.CardColor, PCLResources> colorResourceMap = new HashMap<>();
    protected static final HashMap<AbstractPlayer.PlayerClass, PCLResources> playerResourceMap = new HashMap<>();

    public static PCLCoreResources core;
    public static boolean simpleModePreview;

    public static void registerResource(PCLResources resources)
    {
        if (core == null)
        {
            throw new RuntimeException("No core present");
        }
        colorResourceMap.put(resources.cardColor, resources);
        playerResourceMap.put(resources.playerClass, resources);
        initialize(resources);
    }

    public static boolean canInstantiate(Class<?> type)
    {
        return !Hidden.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers());
    }

    public static String createID(String prefix, String suffix)
    {
        return prefix + ":" + suffix;
    }

    public static AugmentStrings getAugmentStrings(String stringID)
    {
        return AugmentStrings.STRINGS.get(stringID);
    }

    public static String getBlightImage(String id)
    {
        return getPng(id, "blights");
    }

    public static String getBlightOutlineImage(String id)
    {
        return getPng(id, "blights/outline");
    }

    public static BlightStrings getBlightStrings(String blightID)
    {
        return getLanguagePack().getBlightString(blightID);
    }

    public static String getCardImage(String id)
    {
        return getPng(id, "cards");
    }

    public static CardStrings getCardStrings(String cardID)
    {
        return getLanguagePack().getCardStrings(cardID);
    }

    public static CharacterStrings getCharacterStrings(String characterID)
    {
        return getLanguagePack().getCharacterString(characterID);
    }

    public static CharacterStrings getCharacterStrings(AbstractCard.CardColor cardColor)
    {
        PCLResources resources = colorResourceMap.getOrDefault(cardColor, null);
        return resources != null ? resources.getCharacterStrings() : null;
    }

    public static CharacterStrings getCharacterStrings(AbstractPlayer.PlayerClass player)
    {
        PCLResources resources = playerResourceMap.getOrDefault(player, null);
        return resources != null ? resources.getCharacterStrings() : null;
    }

    public static ArrayList<String> getClassNamesFromJarFile(String prefix)
    {
        return GameUtilities.getClassNamesFromJarFile(PGR.class, prefix);
    }

    public static EventStrings getEventStrings(String eventID)
    {
        return getLanguagePack().getEventString(eventID);
    }

    public static LocalizedStrings getLanguagePack()
    {
        return CardCrawlGame.languagePack;
    }

    public static String getMonsterImage(String id)
    {
        return getPng(id, "monsters");
    }

    public static MonsterStrings getMonsterStrings(String monsterID)
    {
        return getLanguagePack().getMonsterStrings(monsterID);
    }

    public static OrbStrings getOrbStrings(String orbID)
    {
        return getLanguagePack().getOrbString(orbID);
    }

    public static PCLAbstractPlayerData getPlayerData(AbstractCard.CardColor playerClass)
    {
        return getResources(playerClass).data;
    }

    public static PCLAbstractPlayerData getPlayerData(AbstractPlayer.PlayerClass playerClass)
    {
        return getResources(playerClass).data;
    }

    public static Collection<PCLResources> getAllResources()
    {
        return colorResourceMap.values();
    }

    public static PCLResources getResources(AbstractCard.CardColor cardColor)
    {
        return colorResourceMap.getOrDefault(cardColor, core);
    }

    public static PCLResources getResources(AbstractPlayer.PlayerClass playerClass)
    {
        return playerResourceMap.getOrDefault(playerClass, core);
    }

    public static String getPng(String id, String subFolder)
    {
        String[] s = id.split(Pattern.quote(":"), 2);
        return "images/" + s[0] + "/" + subFolder + "/" + s[1].replace(":", "_") + ".png";
    }

    public static String getPowerImage(String id)
    {
        return getPng(id, "powers");
    }

    public static PowerStrings getPowerStrings(String powerID)
    {
        return getLanguagePack().getPowerStrings(powerID);
    }

    public static String getRelicImage(String id)
    {
        return getPng(id, "relics");
    }

    public static RelicStrings getRelicStrings(String relicID)
    {
        return getLanguagePack().getRelicStrings(relicID);
    }

    public static String getRewardImage(String id)
    {
        return getPng(id, "ui/rewards");
    }

    public static RunModStrings getRunModStrings(String stringID)
    {
        return getLanguagePack().getRunModString(stringID);
    }

    public static StanceStrings getStanceString(String stanceID)
    {
        return getLanguagePack().getStanceString(stanceID);
    }

    public static void initialize()
    {
        if (core != null)
        {
            throw new RuntimeException("Already Initialized");
        }

        core = new PCLCoreResources();
        initialize(core);
    }

    protected static void initialize(PCLResources resources)
    {
        resources.initializeInternal();
        resources.initializeColor();

        BaseMod.subscribe(resources);
    }

    public static boolean isLoaded()
    {
        return core != null && core.isLoaded && EUIUtils.all(getAllResources(), r -> r.isLoaded);
    }

    public static boolean isTranslationSupported(Settings.GameLanguage language)
    {
        // TODO update when needed
        return false;
    }

    public static void registerCommands()
    {
        ConsoleCommand.addCommand("augment", AugmentCommand.class);
        ConsoleCommand.addCommand("effekseer", EffekseerCommand.class);
        ConsoleCommand.addCommand("exportcsv", ExportCSVCommand.class);
        ConsoleCommand.addCommand("obtain", ObtainCommand.class);
        ConsoleCommand.addCommand("obtaincustom", ObtainCustomCommand.class);
        ConsoleCommand.addCommand("reloadcustom", ReloadCustomCommand.class);
        ConsoleCommand.addCommand("unlockall", UnlockAllCommand.class);
    }
}
