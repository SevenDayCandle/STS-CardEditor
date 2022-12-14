package pinacolada.cards.base.cardText;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import extendedui.EUIUtils;
import extendedui.text.EUISmartText;
import extendedui.ui.tooltips.EUITooltip;

public abstract class SpecialToken extends PCLTextToken
{
    private static final PCLTextParser internalParser = new PCLTextParser(true);

    private SpecialToken()
    {
        super(null, null);
        // this could be:
        // {#G:+4.0} -> Green colored text
        // {kw(Preview):Entou Jyuu} -> Add 'Preview' keyword, but show gold colored 'Entou Jyuu'
        // {kw():Channel} -> do not parse keyword
        // {Attack} -> Use gold color by default
        // {#:Text} -> Reset to default color
    }

    public static int tryAdd(PCLTextParser parser)
    {
        if (parser.character == '{' && parser.remaining > 1)
        {
            builder.setLength(0);

            int index = 1;
            int indentation = 0;
            Color color = Settings.GOLD_COLOR;
            Character next = parser.nextCharacter(index);
            while (next != null)
            {
                switch (next)
                {
                    case '#':
                        color = null;
                    case '{':
                        indentation += 1;
                        break;
                    case '}':
                        if (indentation > 0)
                        {
                            indentation -= 1;
                            index += 1;
                            continue;
                        }
                        if (color == null)
                        {
                            color = Settings.GOLD_COLOR;
                        }
                        final String word = builder.toString();
                        final EUITooltip tooltip = EUITooltip.findByName(word
                                .replace(EUIUtils.SPLIT_LINE, " ")
                                .split("\\(")[0] // Ignore modifiers
                                .toLowerCase());

                        if (tooltip != null)
                        {
                            parser.addTooltip(tooltip);
                        }

                        if (word.startsWith("~"))
                        {
                            internalParser.initialize(parser.card, word.substring(1));
                        }
                        else if (word.startsWith("+"))
                        {
                            if (!parser.card.upgraded)
                            {
                                return index + 1;
                            }

                            if (word.length() == 1)
                            {
                                final WordToken plus = new WordToken(word);
                                plus.coloredString.setColor(Settings.GOLD_COLOR);
                                parser.addToken(plus);
                                return index + 1;
                            }

                            internalParser.initialize(parser.card, word.substring(1));
                        }
                        else if (word.startsWith("-"))
                        {
                            if (parser.card.upgraded)
                            {
                                return index + 1;
                            }

                            internalParser.initialize(parser.card, word.substring(1));
                        }
                        else
                        {
                            internalParser.initialize(parser.card, word);
                        }

                        for (PCLTextToken token : internalParser.getTokens())
                        {
                            if (token instanceof WordToken)
                            {
                                ((WordToken) token).coloredString.setColor(Settings.GOLD_COLOR);
                                ((WordToken) token).tooltip = tooltip;
                            }

                            parser.addToken(token);
                        }

                        return index + 1;
                    case ':':
                        if (color == null)
                        {
                            final String cString = builder.toString();
                            color = EUISmartText.getColor(cString);
                            break;
                        }
                    default:
                        builder.append(next);
                }
                index += 1;
                next = parser.nextCharacter(index);
            }
        }

        return 0;
    }
}