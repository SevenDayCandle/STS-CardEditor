package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import extendedui.ui.TextureCache;
import extendedui.utilities.EUIColors;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.PCLEffects;
import pinacolada.effects.VFX;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLRenderHelpers;
import pinacolada.utilities.RandomizedList;


public class SnowBurstEffect extends PCLEffect
{
    private static final TextureCache[] particles = {VFX.IMAGES.frostSnow1, VFX.IMAGES.frostSnow2, VFX.IMAGES.frostSnow3, VFX.IMAGES.frostSnow4};
    private static final RandomizedList<TextureCache> textures = new RandomizedList<>();
    public static final float RADIUS = 320;

    public static Texture getRandomTexture()
    {
        if (textures.size() <= 1) // Adds some randomness but still ensures all textures are cycled through
        {
            textures.addAll(particles);
        }

        return textures.retrieveUnseeded(true).texture();
    }

    protected float x;
    protected float y;

    public SnowBurstEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void firstUpdate()
    {
        for (int i = 0; i < 12; ++i)
        {
            float r = MathUtils.random(0, 360);
            PCLEffects.Queue.add(new FadingParticleEffect(PGR.core.images.effects.waterBubble.texture(), x + MathUtils.random(-16, 16), y + MathUtils.random(-16, 16))
                    .setBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                    .setColor(EUIColors.white(MathUtils.random(0.5f, 1f)))
                            .setFlip(MathUtils.randomBoolean(), false)
                            .setScale(scale * MathUtils.random(0.2f, 0.6f))
                            .setRotation(0f, 600f)
                            .setTargetPosition(x + RADIUS * MathUtils.cosDeg(r), y + RADIUS * MathUtils.sinDeg(r))).setDuration(0.6f, false);
            PCLEffects.Queue.add(new FadingParticleEffect(getRandomTexture(), x + MathUtils.random(-16, 16), y + MathUtils.random(-16, 16))
                    .setBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                    .setColor(EUIColors.white(MathUtils.random(0.5f, 1f)))
                            .setFlip(MathUtils.randomBoolean(), false)
                            .setScale(scale * MathUtils.random(0.09f, 0.5f))
                            .setRotation(0f, 600f)
                            .setTargetPosition(x + RADIUS * MathUtils.cosDeg(r), y + RADIUS * MathUtils.sinDeg(r))).setDuration(0.6f, false);
        }

        complete();
    }
}
