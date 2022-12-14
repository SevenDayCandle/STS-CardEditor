package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import extendedui.EUI;
import extendedui.EUIRM;
import extendedui.interfaces.delegates.ActionT0;
import extendedui.interfaces.delegates.ActionT2;
import extendedui.ui.AbstractScreen;
import extendedui.ui.controls.*;
import extendedui.ui.hitboxes.EUIHitbox;
import extendedui.ui.tooltips.EUITooltip;
import extendedui.utilities.EUIColors;
import extendedui.utilities.EUIFontHelper;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.PCLAbstractPlayerData;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLCardSlot;
import pinacolada.resources.pcl.PCLLoadout;
import pinacolada.resources.pcl.PCLLoadoutData;
import pinacolada.resources.pcl.PCLRelicSlot;
import pinacolada.utilities.GameUtilities;

import java.util.ArrayList;

public class PCLLoadoutEditor extends AbstractScreen
{
    public static final int MAX_CARD_SLOTS = 6;
    public static final int MAX_RELIC_SLOTS = 2;
    protected final static PCLLoadout.Validation val = new PCLLoadout.Validation();
    public final EUIContextMenu<ContextOption> contextMenu;
    protected final ArrayList<PCLCardSlotEditor> slotsEditors = new ArrayList<>();
    protected final ArrayList<PCLRelicSlotEditor> relicsEditors = new ArrayList<>();
    protected final ArrayList<PCLBaseStatEditor> baseStatEditors = new ArrayList<>();
    protected final PCLLoadoutData[] presets = new PCLLoadoutData[PCLLoadout.MAX_PRESETS];
    public PCLBaseStatEditor activeEditor;
    protected PCLLoadout loadout;
    protected ActionT0 onClose;
    protected int preset;
    protected CharacterOption characterOption;
    protected PCLAbstractPlayerData data;
    protected PCLCardSlotSelectionEffect cardSelectionEffect;
    protected PCLRelicSlotSelectionEffect relicSelectionEffect;
    protected EUILabel startingDeck;
    protected EUILabel deckText;
    protected EUILabel relicText;
    protected EUILabel attributesText;
    protected EUIImage backgroundImage;
    protected EUIButton seriesButton;
    protected EUIButton[] presetButtons;
    protected EUIButton cancelButton;
    protected EUIButton clearButton;
    protected EUIButton saveButton;
    protected EUIToggle upgradeToggle;
    protected EUITextBox ascensionRequirement;
    protected EUITextBox cardscountText;
    protected EUITextBox cardsvalueText;
    protected EUITextBox hindrancevalueText;
    protected int rightClickedSlot;

    public PCLLoadoutEditor()
    {
        final float buttonHeight = screenH(0.07f);
        final float labelHeight = screenH(0.04f);
        final float buttonWidth = screenW(0.18f);
        final float labelWidth = screenW(0.20f);
        final float button_cY = buttonHeight * 1.5f;

        backgroundImage = new EUIImage(EUIRM.images.fullSquare.texture(), new EUIHitbox(screenW(1), screenH(1)))
                .setPosition(screenW(0.5f), screenH(0.5f))
                .setColor(0, 0, 0, 0.9f);

        startingDeck = new EUILabel(null, new EUIHitbox(screenW(0.18f), screenH(0.05f))
                .setCenter(screenW(0.08f), screenH(0.97f)))
                .setFont(EUIFontHelper.carddescriptionfontNormal, 0.9f)
                .setColor(Settings.CREAM_COLOR);

        deckText = new EUILabel(EUIFontHelper.cardtitlefontLarge,
                new EUIHitbox(screenW(0.1f), screenH(0.8f), buttonHeight, buttonHeight))
                .setLabel(PGR.core.strings.charSelect.deckHeader)
                .setFontScale(0.8f)
                .setAlignment(0.5f, 0.5f);

        relicText = new EUILabel(EUIFontHelper.cardtitlefontLarge,
                new EUIHitbox(screenW(0.1f), screenH(0.4f), buttonHeight, buttonHeight))
                .setLabel(PGR.core.strings.charSelect.relicsHeader)
                .setFontScale(0.8f)
                .setAlignment(0.5f, 0.5f);

        attributesText = new EUILabel(EUIFontHelper.cardtitlefontLarge,
                new EUIHitbox(screenW(0.57f), screenH(0.8f), buttonHeight, buttonHeight))
                .setLabel(PGR.core.strings.charSelect.attributesHeader)
                .setFontScale(0.8f)
                .setAlignment(0.5f, 0.5f);

        seriesButton = new EUIButton(PGR.core.images.edit.texture(), new EUIHitbox(0, 0, scale(64), scale(64)))
                .setPosition(startingDeck.hb.x + scale(80), startingDeck.hb.y - scale(48)).setText("")
                .setTooltip(PGR.core.strings.charSelect.seriesEditor, PGR.core.strings.charSelect.seriesEditorInfo)
                .setOnClick(this::openSeriesSelect);

        presetButtons = new EUIButton[PCLLoadout.MAX_PRESETS];
        for (int i = 0; i < presetButtons.length; i++)
        {
            //noinspection SuspiciousNameCombination
            presetButtons[i] = new EUIButton(EUIRM.images.squaredButton.texture(), new EUIHitbox(0, 0, buttonHeight, buttonHeight))
                    .setPosition(screenW(0.45f) + ((i - 1f) * buttonHeight), screenH(1f) - (buttonHeight * 0.85f))
                    .setText(String.valueOf(i + 1))
                    .setOnClick(i, (preset, __) -> changePreset(preset))
                    .setOnRightClick(i, (preset, __) -> rightClickPreset(preset));
        }

        cancelButton = createHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .setPosition(buttonWidth * 0.75f, button_cY)
                .setColor(Color.FIREBRICK)
                .setText(GridCardSelectScreen.TEXT[1])
                .setOnClick(AbstractDungeon::closeCurrentScreen);

        saveButton = createHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .setPosition(screenW(1) - (buttonWidth * 0.75f), button_cY)
                .setColor(Color.FOREST)
                .setText(GridCardSelectScreen.TEXT[0])
                .setInteractable(false)
                .setOnClick(this::save);

        clearButton = createHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .setPosition(saveButton.hb.cX, saveButton.hb.y + saveButton.hb.height + labelHeight * 0.8f)
                .setColor(Color.WHITE)
                .setText(PGR.core.strings.charSelect.clear)
                .setOnClick(this::clear);

        upgradeToggle = new EUIToggle(new EUIHitbox(0, 0, labelWidth * 0.75f, labelHeight))
                .setPosition(screenW(0.5f), screenH(0.055f))
                .setBackground(EUIRM.images.panelRounded.texture(), new Color(0, 0, 0, 0.85f))
                .setText(SingleCardViewPopup.TEXT[6])
                .setOnToggle(this::toggleViewUpgrades);

        cardsvalueText = new EUITextBox(EUIRM.images.panelRounded.texture(), new EUIHitbox(labelWidth, labelHeight))
                .setColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
                .setAlignment(0.5f, 0.5f)
                .setPosition(saveButton.hb.cX, screenH(0.65f))
                .setFont(FontHelper.tipHeaderFont, 1);

        cardscountText = new EUITextBox(EUIRM.images.panelRounded.texture(), new EUIHitbox(labelWidth, labelHeight))
                .setColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
                .setAlignment(0.5f, 0.5f)
                .setPosition(saveButton.hb.cX, cardsvalueText.hb.y + cardsvalueText.hb.height + labelHeight * 0.5f)
                .setFont(FontHelper.tipHeaderFont, 1);

        hindrancevalueText = (EUITextBox) new EUITextBox(EUIRM.images.panelRounded.texture(), new EUIHitbox(labelWidth, labelHeight))
                .setColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
                .setAlignment(0.5f, 0.5f)
                .setPosition(saveButton.hb.cX, cardscountText.hb.y + cardscountText.hb.height + labelHeight * 0.5f)
                .setFont(FontHelper.tipHeaderFont, 1)
                .setTooltip(new EUITooltip("", PGR.core.strings.charSelect.hindranceDescription));

        for (int i = 0; i < MAX_CARD_SLOTS; i++)
        {
            slotsEditors.add(new PCLCardSlotEditor(this, screenW(0.1f), screenH(0.75f - (i * 0.05f))));
        }

        for (int i = 0; i < MAX_RELIC_SLOTS; i++)
        {
            relicsEditors.add(new PCLRelicSlotEditor(this, screenW(0.1f), screenH(0.35f - (i * 0.05f))));
        }

        final PCLBaseStatEditor.StatType[] statTypes = PCLBaseStatEditor.StatType.values();
        for (int i = 0; i < statTypes.length; i++)
        {
            baseStatEditors.add(new PCLBaseStatEditor(statTypes[i], screenW(0.6f), screenH(0.78f - i * 0.1f), this));
        }

        ascensionRequirement = new EUITextBox(EUIRM.images.panelRounded.texture(), new EUIHitbox(labelWidth, labelHeight * 4))
                .setColors(EUIColors.black(0.4f), EUIColors.cream(0.9f))
                .setLabel(PGR.core.strings.charSelect.unlocksAtAscension(PCLLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED))
                .setAlignment(0.5f, 0.5f)
                .setPosition(screenW(0.62f), screenH(0.78f))
                .setFont(FontHelper.charDescFont, 0.9f);

        contextMenu = new EUIContextMenu<ContextOption>(new EUIHitbox(0, 0, 0, 0), o -> o.name)
                .setOnChange(options -> {
                    for (ContextOption o : options)
                    {
                        o.onSelect.invoke(this, rightClickedSlot);
                    }
                })
                .setCanAutosizeButton(true);
    }

    public void changePreset(int preset)
    {
        this.preset = preset;
        setSlotsActive(true);
    }

    public void clear()
    {
        PCLLoadoutData defaultData = loadout.getDefaultData(preset);
        presets[preset] = defaultData;
        setSlotsActive(true);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        PCLCard.canCropPortraits = true;
        toggleViewUpgrades(false);

        if (onClose != null)
        {
            onClose.invoke();
        }
    }

    @Override
    public void updateImpl()
    {
        super.updateImpl();

        val.refresh(presets[preset]);
        backgroundImage.updateImpl();
        startingDeck.updateImpl();
        deckText.updateImpl();
        relicText.updateImpl();
        attributesText.updateImpl();
        upgradeToggle.setToggle(SingleCardViewPopup.isViewingUpgrade).updateImpl();

        if (cardSelectionEffect != null)
        {
            cardSelectionEffect.update();

            if (cardSelectionEffect.isDone)
            {
                cardSelectionEffect = null;
                setSlotsActive(true);
            }
        }
        else if (relicSelectionEffect != null)
        {
            relicSelectionEffect.update();

            if (relicSelectionEffect.isDone)
            {
                relicSelectionEffect = null;
                setSlotsActive(true);
            }
        }
        else
        {
            seriesButton.tryUpdate();

            if (!EUI.doesActiveElementExist())
            {
                for (int i = 0; i < presetButtons.length; i++)
                {
                    final EUIButton button = presetButtons[i];
                    button
                            .showTooltip(!button.interactable)
                            .setColor((i == preset) ? Color.SKY : button.interactable ? Color.LIGHT_GRAY : Color.DARK_GRAY)
                            .tryUpdate();
                }
            }

            ascensionRequirement.tryUpdate();

            for (PCLBaseStatEditor beditor : baseStatEditors)
            {
                if (activeEditor == null || activeEditor == beditor)
                {
                    beditor.setEstimatedValue(val.values.getOrDefault(beditor.type, 0)).tryUpdate();
                }
            }

            cancelButton.updateImpl();
            clearButton.updateImpl();
            saveButton.updateImpl();

            for (PCLCardSlotEditor editor : slotsEditors)
            {
                editor.tryUpdate();
            }
            for (PCLRelicSlotEditor editor : relicsEditors)
            {
                editor.tryUpdate();
            }
        }

        hindrancevalueText.setLabel(PGR.core.strings.charSelect.hindranceValue(val.hindranceLevel)).tryUpdate();
        hindrancevalueText.tooltip.setTitle(hindrancevalueText.label.text);
        cardscountText.setLabel(PGR.core.strings.charSelect.cardsCount(val.cardsCount.v1)).setFontColor(val.cardsCount.v2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).tryUpdate();
        cardsvalueText.setLabel(PGR.core.strings.charSelect.totalValue(val.totalValue.v1, PCLLoadout.MAX_VALUE)).setFontColor(val.totalValue.v2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).tryUpdate();
        saveButton.setInteractable(val.isValid).tryUpdate();

        contextMenu.tryUpdate();
    }

    @Override
    public void renderImpl(SpriteBatch sb)
    {
        super.renderImpl(sb);

        backgroundImage.renderImpl(sb);

        if (relicSelectionEffect != null)
        {
            relicSelectionEffect.render(sb);
        }
        else if (cardSelectionEffect != null)
        {
            cardSelectionEffect.render(sb);
        }
        else
        {
            seriesButton.tryRender(sb);

            for (EUIButton button : presetButtons)
            {
                button.tryRender(sb);
            }

            startingDeck.renderImpl(sb);
            deckText.renderImpl(sb);
            relicText.renderImpl(sb);
            attributesText.renderImpl(sb);

            // All editors must be rendered from top to bottom to prevent dropdowns from overlapping
            for (int i = baseStatEditors.size() - 1; i >= 0; i--)
            {
                baseStatEditors.get(i).tryRender(sb);
            }

            ascensionRequirement.tryRender(sb);
            cancelButton.renderImpl(sb);
            clearButton.renderImpl(sb);
            saveButton.renderImpl(sb);
            upgradeToggle.renderImpl(sb);
            hindrancevalueText.tryRender(sb);
            cardscountText.tryRender(sb);
            cardsvalueText.tryRender(sb);

            for (int i = relicsEditors.size() - 1; i >= 0; i--)
            {
                relicsEditors.get(i).tryRender(sb);
            }

            for (int i = slotsEditors.size() - 1; i >= 0; i--)
            {
                slotsEditors.get(i).tryRender(sb);
            }
        }

        contextMenu.tryRender(sb);
    }

    public void open(PCLLoadout loadout, PCLAbstractPlayerData data, CharacterOption option, ActionT0 onClose)
    {
        super.open();

        boolean enableHPAndGoldEditor = GameUtilities.getMaxAscensionLevel(option.c) >= PCLLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED;
        ascensionRequirement.setActive(!enableHPAndGoldEditor);
        for (PCLBaseStatEditor beditor : baseStatEditors)
        {
            beditor.setActive(enableHPAndGoldEditor);
            beditor.setInteractable(enableHPAndGoldEditor);
        }

        for (int i = 0; i < loadout.presets.length; i++)
        {
            presets[i] = loadout.getPreset(i).makeCopy();

            if (!enableHPAndGoldEditor)
            {
                presets[i].values.put(PCLBaseStatEditor.StatType.HP, 0);
                presets[i].values.put(PCLBaseStatEditor.StatType.Gold, 0);
            }
        }

        presetButtons[0].setInteractable(loadout.canChangePreset(0));
        presetButtons[1].setInteractable(loadout.canChangePreset(1));
        presetButtons[2].setInteractable(loadout.canChangePreset(2));

        this.loadout = loadout;
        this.onClose = onClose;
        this.characterOption = option;
        this.data = data;

        startingDeck.setLabel(PGR.core.strings.charSelect.leftText + " | #y" + (loadout.getName().replace(" ", " #y")));

        PCLCard.canCropPortraits = false;
        toggleViewUpgrades(false);
        changePreset(loadout.preset);
    }

    private void openSeriesSelect()
    {
        if (characterOption != null && data != null)
        {
            PGR.core.seriesSelection.open(characterOption, data, this.onClose);
        }
    }

    public void repositionSlotEditor(PCLCardSlotEditor cardSlotEditor, int index)
    {
        cardSlotEditor.translate(screenW(0.1f), screenH(0.75f - (index * 0.05f)));
    }

    public void rightClickPreset(int preset)
    {
        rightClickedSlot = preset;
        if (rightClickedSlot != this.preset)
        {
            ArrayList<ContextOption> list = new ArrayList<>();
            list.add(ContextOption.CopyFrom);
            list.add(ContextOption.CopyTo);

            contextMenu.setPosition(InputHelper.mX, InputHelper.mY);
            contextMenu.setItems(list);
            contextMenu.openOrCloseMenu();
        }

    }

    public void save()
    {
        for (int i = 0, presetsLength = presets.length; i < presetsLength; i++)
        {
            loadout.presets[i] = presets[i];
        }

        loadout.preset = preset;
        data.saveLoadouts();
        AbstractDungeon.closeCurrentScreen();
    }

    public void setSlotsActive(boolean active)
    {
        if (active)
        {
            final PCLLoadoutData data = presets[preset];
            for (int i = 0; i < slotsEditors.size(); i++)
            {
                final PCLCardSlotEditor editor = slotsEditors.get(i);
                editor.setActive(data.cardsSize() > i);
                editor.setSlot(editor.isActive ? data.getCardSlot(i) : null);
            }
            for (int i = 0; i < relicsEditors.size(); i++)
            {
                final PCLRelicSlotEditor reditor = relicsEditors.get(i);
                reditor.setActive(data.relicsSize() > i);
                reditor.setSlot(reditor.isActive ? data.getRelicSlot(i) : null);
            }
            for (PCLBaseStatEditor beditor : baseStatEditors)
            {
                beditor.setLoadout(data);
            }
            val.refresh(presets[preset]);
        }
        else
        {
            for (PCLCardSlotEditor editor : slotsEditors)
            {
                editor.setActive(false);
            }
        }
    }

    public void toggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    public void trySelectCard(PCLCardSlot cardSlot)
    {
        cardSelectionEffect = new PCLCardSlotSelectionEffect(cardSlot);
        setSlotsActive(false);
    }

    public void trySelectRelic(PCLRelicSlot relicSlot)
    {
        relicSelectionEffect = new PCLRelicSlotSelectionEffect(relicSlot);
        setSlotsActive(false);
    }

    public enum ContextOption
    {
        CopyTo(PGR.core.strings.charSelect.copyTo, (screen, index) -> {
            screen.presets[index] = screen.presets[screen.preset];
            screen.changePreset(index);
        }),
        CopyFrom(PGR.core.strings.charSelect.copyFrom, (screen, index) -> {
            screen.presets[screen.preset] = screen.presets[index];
            screen.setSlotsActive(true);
        });

        public final String name;
        public final ActionT2<PCLLoadoutEditor, Integer> onSelect;

        ContextOption(String name, ActionT2<PCLLoadoutEditor, Integer> onSelect)
        {
            this.name = name;
            this.onSelect = onSelect;
        }
    }
}