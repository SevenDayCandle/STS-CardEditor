package pinacolada.cards.base.cardText;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.GameUtilities;

import java.util.LinkedList;

public class PCLTextLine
{
    protected final static float IMG_HEIGHT = 420f * Settings.scale;
    protected final static float IMG_WIDTH = 300f * Settings.scale;
    protected final static float DESC_BOX_WIDTH = IMG_WIDTH * 0.81f;//0.79f;
    protected final static float DESC_OFFSET_Y = IMG_HEIGHT * 0.255f;
    protected final LinkedList<PCLTextToken> tokens = new LinkedList<>();
    protected final PCLTextContext context;

    public float width = 0;
    public float additionalWidth = 0;

    public PCLTextLine(PCLTextContext context)
    {
        this.context = context;
    }

    // Greedily add as many tokens to first line as possible, then move to the next
    public void add(PCLTextToken token)
    {
        float tokenWidth = token.getWidth(context);
        if ((tokens.isEmpty() && token.type != PCLTextTokenType.Whitespace) ||
                ((tokenWidth + width) < context.lineWidth || (token.type == PCLTextTokenType.Punctuation && token.rawText.length() == 1)))
        {
            tokens.add(token);
            width += tokenWidth;
            additionalWidth += token.getAdditionalWidth(context);
        }
        else
        {
            PCLTextLine newLine = context.addLine();

            if (token.type != PCLTextTokenType.Whitespace)
            {
                newLine.tokens.add(token);
                newLine.width += tokenWidth;
                newLine.additionalWidth += token.getAdditionalWidth(context);
            }

            trimEnd();
        }
    }

    public float calculateHeight(BitmapFont font)
    {
        if (tokens.isEmpty())
        {
            return font.getCapHeight() * 0.5f;
        }
        else
        {
            return font.getCapHeight();
        }
    }

    public PCLTextToken getEnd()
    {
        return tokens.getLast();
    }

    public float getEndWidth()
    {
        return tokens.size() > 0 ? tokens.getLast().getWidth(context) : 0;
    }

    public PCLTextToken getStart()
    {
        return tokens.size() > 0 ? tokens.getFirst() : null;
    }

    public PCLTextToken popEnd()
    {
        PCLTextToken popped = tokens.pollLast();
        if (popped != null)
        {
            width -= popped.getWidth(context);
            additionalWidth -= popped.getAdditionalWidth(context);
        }
        return popped;
    }

    public void pushEnd(PCLTextToken token)
    {
        tokens.add(token);
        width += token.getWidth(context);
        additionalWidth += token.getAdditionalWidth(context);
    }

    public void pushStart(PCLTextToken token)
    {
        tokens.push(token);
        width += token.getWidth(context);
        additionalWidth += token.getAdditionalWidth(context);
    }

    public void render(SpriteBatch sb)
    {
        final PCLCard card = context.card;

        // Additional width is only shown in battle
        context.startX = card.current_x - ((width + (GameUtilities.inBattle() ? additionalWidth : 0)) * card.drawScale * 0.5f);
        context.startY = context.startY - (calculateHeight(context.font) * 1.45f);

        for (PCLTextToken token : tokens)
        {
            token.render(sb, context);
        }
    }

    protected void trimEnd()
    {
        int size = tokens.size();
        if (size > 0 && tokens.getLast().type == PCLTextTokenType.Whitespace)
        {
            popEnd();
            trimEnd();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (PCLTextToken token : tokens)
        {
            sb.append(token.rawText);
        }

        return sb.toString();
    }
}