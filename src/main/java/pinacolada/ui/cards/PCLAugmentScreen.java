package pinacolada.ui.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import extendedui.EUIUtils;
import extendedui.interfaces.delegates.ActionT2;
import extendedui.interfaces.delegates.FuncT0;
import extendedui.ui.AbstractScreen;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.cards.base.PCLCard;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.utility.CallbackEffect;
import pinacolada.resources.PGR;
import pinacolada.ui.cardView.PCLAugmentList;

import java.util.HashMap;
import java.util.Map;

public class PCLAugmentScreen extends AbstractScreen
{
    protected PCLAugmentList panel;
    protected PCLEffect curEffect;
    protected FuncT0<HashMap<PCLAugmentData, Integer>> getEntries;
    protected ActionT2<PCLAugment, Integer> addItem;
    protected boolean canSelect;

    public PCLAugmentScreen()
    {
        panel = new PCLAugmentList(this::doAction);
    }

    public void doAction(PCLAugment augment)
    {
        if (canSelect && augment != null)
        {
            curEffect = new CallbackEffect(new SelectFromPile(augment.getName(), 1, AbstractDungeon.player.masterDeck)
                    .cancellableFromPlayer(true)
                    .setOptions(false, true)
                    .setFilter(augment::canApply)
                    .addCallback(selection -> {
                        for (AbstractCard c : selection)
                        {
                            PGR.core.dungeon.addAugment(augment.ID, -1);
                            augment.addToCard((PCLCard) c);
                            refreshAugments();
                        }
                    }));
        }
        else
        {
            AbstractDungeon.closeCurrentScreen();
        }
    }

    public void open(FuncT0<HashMap<PCLAugmentData, Integer>> getEntries, int rows, boolean canSelect)
    {
        super.open(false, false);
        this.getEntries = getEntries;
        this.canSelect = canSelect;
        panel = new PCLAugmentList(this::doAction, rows);
        addItem = canSelect ? (a, b) -> panel.addPanelItem(a, b, EUIUtils.any(AbstractDungeon.player.masterDeck.group, a::canApply)) : panel::addListItem;
        refreshAugments();
    }

    public void refreshAugments()
    {
        panel.clear();
        HashMap<PCLAugmentData, Integer> entries = getEntries != null ? getEntries.invoke() : new HashMap<>();
        for (Map.Entry<PCLAugmentData, Integer> params : entries.entrySet())
        {
            PCLAugmentData data = params.getKey();
            int amount = params.getValue();
            if (data != null && amount > 0)
            {
                PCLAugment augment = data.create();
                addItem.invoke(augment, amount);
            }
        }
        PGR.core.cardAffinities.open(entries, null, false);
    }

    @Override
    public void updateImpl()
    {
        super.updateImpl();
        if (curEffect != null)
        {
            curEffect.update();
            if (curEffect.isDone)
            {
                curEffect = null;
            }
        }
        else
        {
            panel.updateImpl();
        }
        PGR.core.cardAffinities.tryUpdate(true);

    }

    @Override
    public void preRender(SpriteBatch sb)
    {
        if (curEffect != null)
        {
            curEffect.render(sb);
        }
        else
        {
            panel.renderImpl(sb);
        }
    }

    @Override
    public void renderImpl(SpriteBatch sb)
    {
        super.renderImpl(sb);
        PGR.core.cardAffinities.tryRender(sb);
    }


}
