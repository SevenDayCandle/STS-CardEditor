package pinacolada.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.*;
import extendedui.utilities.EUIColors;
import pinacolada.effects.utility.CombinedEffect;
import pinacolada.effects.vfx.*;
import pinacolada.effects.vfx.megacritCopy.*;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLCoreImages;
import pinacolada.utilities.PCLRenderHelpers;

public class VFX
{
    public static final PCLCoreImages.Effects IMAGES = PGR.core.images.effects;

    public static BiteEffect2 bite(Hitbox target)
    {
        return bite(target, Color.WHITE);
    }

    public static BiteEffect2 bite(Hitbox target, Color color)
    {
        return bite(target.cX, target.cY - (40.0F * Settings.scale), color);
    }

    public static BiteEffect2 bite(float cX, float cY, Color color)
    {
        return new BiteEffect2(cX, cY - (40.0F * Settings.scale), color);
    }

    public static BleedEffect bleed(Hitbox target)
    {
        return new BleedEffect(target.cX, target.cY - (50.0F * Settings.scale), 32);
    }

    public static CircularWaveEffect circularWave(Hitbox target)
    {
        return circularWave(target.cX, target.cY);
    }

    public static CircularWaveEffect circularWave(float cX, float cY)
    {
        return new CircularWaveEffect(cX, cY);
    }

    public static ClashEffect2 clash(Hitbox target)
    {
        return new ClashEffect2(target.cX, target.cY);
    }

    public static ClawEffect2 claw(Hitbox target, Color color1, Color color2)
    {
        return claw(randomX(target, 0.2f), randomY(target, 0.2f), color1, color2);
    }

    public static ClawEffect2 claw(float cX, float cY, Color color1, Color color2)
    {
        return new ClawEffect2(cX, cY, color1, color2);
    }

    public static CleaveEffect cleave(boolean fromPlayer)
    {
        return new CleaveEffect(fromPlayer);
    }

    public static DaggerSprayEffect daggerSpray()
    {
        return new DaggerSprayEffect(flipHorizontally());
    }

    public static CombinedEffect dark(Hitbox hb, int variance)
    {
        return dark(randomX(hb, variance), randomY(hb, variance));
    }

    public static CombinedEffect dark(float cX, float cY)
    {
        final CombinedEffect effect = new CombinedEffect();
        effect.add(new OrbFlareEffect2(cX, cY).setColors(OrbFlareEffect.OrbFlareColor.DARK)).renderBehind = false;
        for (int i = 0; i < 4; i++)
        {
            effect.add(new DarkOrbActivateParticle(cX, cY)).renderBehind = false;
        }

        return effect;
    }

    public static DarknessEffect darkness(Hitbox target, float spread)
    {
        return darkness(randomX(target, spread), randomY(target, spread));
    }

    public static DarknessEffect darkness(float cX, float cY)
    {
        return new DarknessEffect(cX, cY);
    }

    public static EffekseerEffect eFX(PCLEffekseerEFX key)
    {
        return eFX(key, Settings.WIDTH * 0.75f, AbstractDungeon.player != null ? AbstractDungeon.player.hb.cY : Settings.HEIGHT * 0.35f);
    }

    public static EffekseerEffect eFX(PCLEffekseerEFX key, Hitbox hb)
    {
        return eFX(key, hb.cX, hb.cY);
    }

    public static EffekseerEffect eFX(PCLEffekseerEFX key, float x, float y)
    {
        return new EffekseerEffect(key, x, y);
    }

    public static ElectricityEffect electric(Hitbox target, float spread)
    {
        return electric(target.cX, target.cY).setSpread(spread);
    }

    public static ElectricityEffect electric(float cX, float cY)
    {
        return new ElectricityEffect(cX, cY);
    }

    public static FallingIceEffect fallingIce(int frostCount)
    {
        return new FallingIceEffect(frostCount, flipHorizontally());
    }

    public static FireBurstParticleEffect fireBurstParticle(float cX, float cY)
    {
        return new FireBurstParticleEffect(cX, cY, Color.RED);
    }

    public static FireBurstEffect fireball(Hitbox source, Hitbox target)
    {
        return new FireBurstEffect(source.cX, source.cY, target.cX, target.cY).setColor(Color.RED, Color.ORANGE);
    }

    public static FlameBarrierEffect flameBarrier(Hitbox source)
    {
        return new FlameBarrierEffect(source.cX, source.cY);
    }

    public static boolean flipHorizontally()
    {
        return AbstractDungeon.player.flipHorizontal || AbstractDungeon.getMonsters().shouldFlipVfx();
    }

    public static AnimatedParticleEffect gunshot(Hitbox target, float spread)
    {
        return gunshot(randomX(target, spread), randomY(target, spread));
    }

    public static AnimatedParticleEffect gunshot(float cX, float cY)
    {
        return new AnimatedParticleEffect(PCLEffect.IMAGES.shot.texture(), cX, cY, 4, 4);
    }

    public static HemokinesisEffect2 hemokinesis(Hitbox source, Hitbox target)
    {
        return new HemokinesisEffect2(target.cX, target.cY, source.cX, source.cY);
    }

    public static IntimidateEffect intimidate(Hitbox source)
    {
        return new IntimidateEffect(source.cX, source.cY);
    }

    public static IronWaveEffect2 ironWave(Hitbox source, Hitbox target)
    {
        return ironWave(source.cX, source.cY, target.cX);
    }

    public static IronWaveEffect2 ironWave(float cX, float cY, float targetX)
    {
        return new IronWaveEffect2(cX, cY, targetX);
    }

    public static LaserBeamEffect2 laser(Hitbox source, Color color)
    {
        return new LaserBeamEffect2(source.cX, source.cY).setColor(color);
    }

    public static LightningEffect2 lightning(Hitbox target)
    {
        return lightning(target.cX, target.cY);
    }

    public static LightningEffect2 lightning(float cX, float cY)
    {
        return new LightningEffect2(cX, cY);
    }

    public static MeteorFallEffect meteorFall(Hitbox hb)
    {
        return new MeteorFallEffect(randomX(hb, 0.2f), randomY(hb, 0.2f));
    }

    public static MindblastEffect2 mindblast(float dialogX, float dialogY)
    {
        return new MindblastEffect2(dialogX, dialogY, flipHorizontally());
    }

    public static PsychokinesisEffect psychokinesis(Hitbox target)
    {
        return psychokinesis(target.cX, target.cY);
    }

    public static PsychokinesisEffect psychokinesis(float cX, float cY)
    {
        return new PsychokinesisEffect(cX, cY);
    }

    public static float randomX(Hitbox hb, float variance)
    {
        return hb.cX + (variance == 0 ? 0 : (MathUtils.random(-variance, variance) * hb.width));
    }

    public static float randomY(Hitbox hb, float variance)
    {
        return hb.cY + (variance == 0 ? 0 : (MathUtils.random(-variance, variance) * hb.height));
    }

    public static RazorWindEffect razorWind(Hitbox source)
    {
        return razorWind(source, source, MathUtils.random(1000.0F, 1200.0F), MathUtils.random(-20.0F, 20.0F));
    }

    public static RazorWindEffect razorWind(Hitbox source, Hitbox target, float horizontalSpeed, float horizontalAcceleration)
    {
        return new RazorWindEffect(source.cX, source.cY, randomY(target, 0.33f), horizontalSpeed, horizontalAcceleration);
    }

    public static RockBurstEffect rockBurst(Hitbox target, float scale)
    {
        return new RockBurstEffect(target.cX, target.cY, scale);
    }

    public static RockBurstEffect rockBurst(float cX, float cY)
    {
        return new RockBurstEffect(cX, cY, 1);
    }

    public static ShieldEffect shield(Hitbox target)
    {
        return shield(target.cX, target.cY);
    }

    public static ShieldEffect shield(float cX, float cY)
    {
        return new ShieldEffect(cX, cY);
    }

    public static ShockWaveEffect shockWave(Hitbox source, Color color)
    {
        return shockWave(source, color, ShockWaveEffect.ShockWaveType.ADDITIVE);
    }

    public static ShockWaveEffect shockWave(Hitbox source, Color color, ShockWaveEffect.ShockWaveType type)
    {
        return new ShockWaveEffect(source.cX, source.cY, color.cpy(), type);
    }

    public static ShootingStarsEffect shootingStars(Hitbox source, float spreadY)
    {
        return new ShootingStarsEffect(source.cX, source.cY).setSpread(0, spreadY).flipHorizontally(flipHorizontally());
    }

    public static ExplosionSmallEffect2 smallExplosion(Hitbox source)
    {
        return smallExplosion(source.cX, source.cY);
    }

    public static ExplosionSmallEffect2 smallExplosion(float cX, float cY)
    {
        return new ExplosionSmallEffect2(cX, cY);
    }

    public static ExplosionSmallEffect2 smallExplosion(Hitbox source, float variance)
    {
        return new ExplosionSmallEffect2(randomX(source, variance), randomY(source, variance));
    }

    public static SmallLaserEffect2 smallLaser(Hitbox source, Hitbox target, Color color)
    {
        return smallLaser(source, target, color, 0.2f);
    }

    public static SmallLaserEffect2 smallLaser(Hitbox source, Hitbox target, Color color, float variance)
    {
        return new SmallLaserEffect2(source.cX, source.cY, randomX(target, variance), randomY(target, variance))
                .setColors(color, EUIColors.lerp(color, Color.BLACK, 0.3f));
    }

    public static SnowballEffect snowball(Hitbox source, Hitbox target)
    {
        return new SnowballEffect(source.cX, source.cY, randomX(target, 0.15f), randomY(target, 0.15f)).setColor(Color.SKY, Color.NAVY);
    }

    public static SnowballImpactEffect snowballImpact(Hitbox target, float spread)
    {
        return new SnowballImpactEffect(randomX(target, spread), randomY(target, spread));
    }

    public static SnowballImpactEffect snowballImpact(float cX, float cY)
    {
        return new SnowballImpactEffect(cX, cY);
    }

    public static SparkImpactEffect sparkImpact(Hitbox target, float spread)
    {
        return new SparkImpactEffect(randomX(target, spread), randomY(target, spread));
    }

    public static SparkImpactEffect sparkImpact(float cX, float cY)
    {
        return new SparkImpactEffect(cX, cY);
    }

    public static StrongPunchEffect strongPunch(Hitbox target)
    {
        return strongPunch(target.cX, target.cY);
    }

    public static StrongPunchEffect strongPunch(float x, float y)
    {
        return (StrongPunchEffect) new StrongPunchEffect(x, y, 2).setDuration(1f, true);
    }

    public static ColoredSweepingBeamEffect sweepingBeam(AbstractCreature source)
    {
        return sweepingBeam(source.hb, source.flipHorizontal, Color.CYAN);
    }

    public static ColoredSweepingBeamEffect sweepingBeam(Hitbox source, boolean flipHorizontal, Color color)
    {
        return new ColoredSweepingBeamEffect(source.cX, source.cY, flipHorizontal, color);
    }

    public static ThrowDaggerEffect2 throwDagger(Hitbox target, float variance)
    {
        return new ThrowDaggerEffect2(randomX(target, variance), randomY(target, variance));
    }

    public static TornadoEffect tornado(Hitbox source)
    {
        return tornado(source.cX, source.cY);
    }

    public static TornadoEffect tornado(float cX, float cY)
    {
        return new TornadoEffect(cX, cY);
    }

    public static VerticalImpactEffect2 verticalImpact(Hitbox target)
    {
        return new VerticalImpactEffect2(target.cX + target.width / 4f, target.cY - target.height / 4f);
    }

    public static FadingParticleEffect water(Hitbox target, float spread)
    {
        return water(randomX(target, spread), randomY(target, spread));
    }

    public static FadingParticleEffect water(float cX, float cY)
    {
        return (FadingParticleEffect) new FadingParticleEffect(IMAGES.waterSplash1.texture(), cX, cY).setColor(Color.WHITE)
                .setBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                .setOpacity(MathUtils.random(0.7f, 1f))
                .setDuration(1.3f, false);
    }

    public static FadingParticleEffect water2(Hitbox target, float spread)
    {
        return water2(randomX(target, spread), randomY(target, spread));
    }

    public static FadingParticleEffect water2(float cX, float cY)
    {
        return (FadingParticleEffect) new FadingParticleEffect(IMAGES.waterSplash2.texture(), cX, cY).setColor(Color.WHITE)
                .setBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                .setOpacity(MathUtils.random(0.7f, 1f))
                .setDuration(1.3f, false);
    }

    public static WeightyImpactEffect weightyImpact(Hitbox target)
    {
        return weightyImpact(target, new Color(1.0F, 1.0F, 0.1F, 0.0F));
    }

    public static WeightyImpactEffect weightyImpact(Hitbox target, Color color)
    {
        return new WeightyImpactEffect(target.cX, target.cY);
    }

    public static AnimatedParticleEffect whack(Hitbox target, float spread)
    {
        return whack(randomX(target, spread), randomY(target, spread));
    }

    public static AnimatedParticleEffect whack(float cX, float cY)
    {
        return new AnimatedParticleEffect(PCLEffect.IMAGES.whack.texture(), cX, cY, 4, 4);
    }

    public static WhirlwindEffect whirlwind()
    {
        return whirlwind(new Color(0.9F, 0.9F, 1.0F, 1.0F), false);
    }

    public static WhirlwindEffect whirlwind(Color color, boolean reverse)
    {
        return new WhirlwindEffect(color, reverse);
    }
}
