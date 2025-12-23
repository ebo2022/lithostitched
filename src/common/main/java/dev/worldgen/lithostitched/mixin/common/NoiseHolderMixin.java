package dev.worldgen.lithostitched.mixin.common;

import dev.worldgen.lithostitched.duck.NoiseDuck;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(DensityFunction.NoiseHolder.class)
public class NoiseHolderMixin implements NoiseDuck {
    @Shadow @Final private NormalNoise noise;

    @Override
    public double lithostitched$withGradient(double x, double y, double z, double[] gradOut) {
        return this.noise == null ? 0.0F : ((NoiseDuck) this.noise).lithostitched$withGradient(x, y, z, gradOut);
    }
}
