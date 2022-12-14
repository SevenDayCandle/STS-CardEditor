package pinacolada.ui.debug;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import extendedui.debug.*;
import imgui.ImGuiTextFilter;
import org.apache.commons.lang3.StringUtils;
import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.resources.PGR;

import java.util.ArrayList;

public class PCLDebugAugmentPanel
{
    protected static final String ALL = "Any";
    protected static final String BASE_GAME = "Base";
    protected ArrayList<PCLAugmentData> originalSortedAugments = new ArrayList<>();
    protected ArrayList<PCLAugmentData> sortedAugments = originalSortedAugments;
    protected ImGuiTextFilter augmentFilter = new ImGuiTextFilter();
    protected DEUITabItem augments = new DEUITabItem("Augments");
    protected DEUIIntInput augmentCount = new DEUIIntInput("Count", 1, 1, Integer.MAX_VALUE);
    protected DEUIButton obtain = new DEUIButton("Obtain");
    protected DEUIFilteredSuffixListBox<PCLAugmentData> augmentList = new DEUIFilteredSuffixListBox<PCLAugmentData>("##all augments",
            sortedAugments, p -> p.ID, p -> p.strings.NAME, this::passes);


    private boolean passes(PCLAugmentData augment)
    {
        return (augmentFilter.passFilter(augment.ID) || augmentFilter.passFilter(augment.strings.NAME));
    }

    private void obtain()
    {
        PCLAugmentData chosen = augmentList.get();
        if (chosen != null)
        {
            PGR.core.dungeon.addAugment(chosen.ID, augmentCount.get());
        }
    }

    public PCLDebugAugmentPanel()
    {
        originalSortedAugments.addAll(PCLAugment.getValues());
        originalSortedAugments.sort((a, b) -> StringUtils.compare(a.ID, b.ID));
    }

    public void render()
    {
        augments.render(() -> {
            DEUIUtils.withFullWidth(() ->
            {
                augmentFilter.draw("##");
                augmentList.render();
            });
            DEUIUtils.withWidth(90, () ->
            {
                augmentCount.renderInline();
            });
            DEUIUtils.disabledIf(AbstractDungeon.player == null || augmentList.get() == null, () ->
            {
                obtain.render(this::obtain);
            });
        });
    }
}
