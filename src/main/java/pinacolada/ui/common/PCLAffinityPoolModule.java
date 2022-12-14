package pinacolada.ui.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import extendedui.EUIUtils;
import extendedui.ui.cardFilter.CardKeywordFilters;
import extendedui.ui.cardFilter.CustomCardFilterModule;
import extendedui.ui.controls.EUILabel;
import extendedui.ui.controls.EUISearchableDropdown;
import extendedui.ui.hitboxes.EUIHitbox;
import extendedui.ui.hitboxes.RelativeHitbox;
import extendedui.utilities.EUIFontHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLLoadout;
import pinacolada.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static extendedui.ui.cardFilter.CardKeywordFilters.DRAW_START_Y;
import static extendedui.ui.cardFilter.CardKeywordFilters.SPACING;
import static pinacolada.ui.common.AffinityKeywordButton.ICON_SIZE;

public class PCLAffinityPoolModule extends CustomCardFilterModule
{
    public static HashSet<PCLLoadout> currentSeries = new HashSet<>();
    public static ArrayList<PCLCardAffinity> currentAffinities = EUIUtils.map(PCLAffinity.values(), PCLCardAffinity::new);
    public final ArrayList<AffinityKeywordButton> affinityButtons = new ArrayList<>();
    public final EUILabel affinitiesSectionLabel;
    public final EUISearchableDropdown<PCLLoadout> seriesDropdown;
    protected CardKeywordFilters filters;

    public PCLAffinityPoolModule(CardKeywordFilters filters)
    {
        this.filters = filters;
        seriesDropdown = (EUISearchableDropdown<PCLLoadout>) new EUISearchableDropdown<PCLLoadout>(new EUIHitbox(0, 0, scale(240), scale(48)), PCLLoadout::getName)
                .setOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .setOnChange(selectedSeries -> {
                    currentSeries.clear();
                    currentSeries.addAll(selectedSeries);
                    filters.invoke(null);
                })
                .setHeader(EUIFontHelper.cardtitlefontSmall, 0.8f, Settings.GOLD_COLOR, PGR.core.strings.seriesUI.seriesUI)
                .setIsMultiSelect(true)
                .setCanAutosizeButton(true);

        affinitiesSectionLabel = new EUILabel(EUIFontHelper.cardtitlefontSmall,
                new EUIHitbox(0, 0, scale(48), scale(48)))
                .setFont(EUIFontHelper.cardtitlefontSmall, 0.8f)
                .setLabel(PGR.core.strings.seriesUI.affinities)
                .setColor(Settings.GOLD_COLOR)
                .setAlignment(0.5f, 0.0f, false);

        initializeAffinities();
    }

    protected void initializeAffinities()
    {
        affinityButtons.clear();
        for (int i = 0; i < currentAffinities.size(); i++)
        {
            PCLCardAffinity a = currentAffinities.get(i);
            affinityButtons.add(new AffinityKeywordButton(
                    new RelativeHitbox(affinitiesSectionLabel.hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * (0.5f + i * 1.05f), -1.5f * (ICON_SIZE / affinitiesSectionLabel.hb.width)).setIsPopupCompatible(true), a.type)
                    .setLevel(a.level)
                    .setOptions(false, true)
                    .setOnClick((button) -> {
                        button.setLevel((button.currentLevel + 1) % 3);
                        a.level = button.currentLevel;
                        filters.invoke(null);
                    })
                    .setOnRightClick((button) -> {
                        button.setLevel(0);
                        a.level = button.currentLevel;
                        filters.invoke(null);
                    }));
        }
    }

    @Override
    public boolean isCardValid(AbstractCard c)
    {
        if (!currentSeries.isEmpty() && !currentSeries.contains(GameUtilities.getPCLSeries(c)))
        {
            return false;
        }
        for (PCLCardAffinity cAffinity : currentAffinities)
        {
            if (GameUtilities.getPCLCardAffinityLevel(c, cAffinity.type, true) < cAffinity.level)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return currentSeries.isEmpty() && currentAffinities.isEmpty();
    }

    @Override
    public boolean isHovered()
    {
        return seriesDropdown.hb.hovered || EUIUtils.any(affinityButtons, b -> b.backgroundButton.hb.hovered);
    }

    @Override
    public void initializeSelection(Collection<AbstractCard> cards)
    {
        HashSet<PCLLoadout> availableSeries = new HashSet<>();
        for (AbstractCard card : cards)
        {
            availableSeries.add(GameUtilities.getPCLSeries(card));
            if (card instanceof PCLCard)
            {
                ((PCLCard) card).affinities.updateSortedList();
            }
        }
        ArrayList<PCLLoadout> seriesItems = EUIUtils.filter(availableSeries, Objects::nonNull);
        seriesItems.sort((a, b) -> StringUtils.compare(a.getName(), b.getName()));
        seriesDropdown.setItems(seriesItems).setActive(seriesItems.size() > 0);

        currentAffinities = EUIUtils.map(PCLAffinity.getAvailableAffinities(), PCLCardAffinity::new);
        currentAffinities.add(new PCLCardAffinity(PCLAffinity.Star));
        currentAffinities.add(new PCLCardAffinity(PCLAffinity.General));
        initializeAffinities();
    }

    @Override
    public void reset()
    {
        currentSeries.clear();
        seriesDropdown.setSelectionIndices((int[]) null, false);
        for (PCLCardAffinity c : currentAffinities)
        {
            c.level = 0;
        }
        for (AffinityKeywordButton c : affinityButtons)
        {
            c.setLevel(0);
        }
    }

    @Override
    public void updateImpl()
    {
        this.seriesDropdown.setPosition(filters.typesDropdown.hb.x + filters.typesDropdown.hb.width + SPACING * 2, DRAW_START_Y + filters.getScrollDelta()).tryUpdate();
        this.affinitiesSectionLabel.setPosition(filters.descriptionInput.hb.x + filters.descriptionInput.hb.width + SPACING * 6, DRAW_START_Y + filters.getScrollDelta() - SPACING * 2f).tryUpdate();
        for (AffinityKeywordButton c : affinityButtons)
        {
            c.tryUpdate();
        }
    }

    @Override
    public void renderImpl(SpriteBatch sb)
    {
        this.seriesDropdown.tryRender(sb);
        this.affinitiesSectionLabel.tryRender(sb);
        for (AffinityKeywordButton c : affinityButtons)
        {
            c.tryRender(sb);
        }
    }
}
