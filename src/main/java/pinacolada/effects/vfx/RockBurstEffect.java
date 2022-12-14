package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import extendedui.ui.TextureCache;
import extendedui.utilities.EUIColors;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.PCLEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.resources.PGR;
import pinacolada.utilities.RandomizedList;

public class RockBurstEffect extends PCLEffect
{
    private static final TextureCache[] particles = {PGR.core.images.effects.earthParticle1, PGR.core.images.effects.earthParticle2, PGR.core.images.effects.earthParticle3};
    private static final RandomizedList<TextureCache> textures = new RandomizedList<>();
    public static final int PROJECTILES = 68;
    public static final float RADIUS = 240;
    protected float x;
    protected float y;

    public static Texture getRandomTexture()
    {
        if (textures.size() <= 1) // Adds some randomness but still ensures all textures are cycled through
        {
            textures.addAll(particles);
        }

        return textures.retrieveUnseeded(true).texture();
    }

    public RockBurstEffect(float startX, float startY, float scale)
    {
        super(1f, true);

        this.x = startX;
        this.y = startY;
        this.scale = scale;
        this.color = Color.WHITE.cpy();
    }

    @Override
    protected void firstUpdate()
    {
        SFX.play(scale > 1 ? SFX.BLUNT_HEAVY : SFX.BLUNT_FAST, 0.9f, 1.1f);
        PCLEffects.Queue.add(VFX.whack(x, y).setScale(0.25f * Settings.scale).setColor(Color.TAN));

        for (int i = 0; i < PROJECTILES; ++i)
        {
            float angle = random(-500f, 500f);
            PCLEffects.Queue.add(new FadingParticleEffect(getRandomTexture(), x, y)
                            .setColor(EUIColors.random(0.7f, 1f, true))
                            .setScale(scale * random(0.18f, 0.66f))
                            .setRotation(0, random(500f, 800f))
                            .setTargetPosition(x + RADIUS * MathUtils.cos(angle), y + RADIUS * MathUtils.sin(angle), random(220f, 330f)));
        }

        complete();
    }
}
