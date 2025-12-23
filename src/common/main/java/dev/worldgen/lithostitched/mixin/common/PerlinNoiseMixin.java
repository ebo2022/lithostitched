package dev.worldgen.lithostitched.mixin.common;

import dev.worldgen.lithostitched.duck.NoiseDuck;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PerlinNoise.class)
public class PerlinNoiseMixin implements NoiseDuck {
    @Shadow @Final private double lowestFreqInputFactor;
    @Shadow @Final private double lowestFreqValueFactor;
    @Shadow @Final @Nullable private ImprovedNoise[] noiseLevels;
    @Shadow @Final private DoubleList amplitudes;

    @Override
    public double lithostitched$withGradient(double x, double y, double z, double[] gradOut) {
        double value = 0.0F;
        double factor = this.lowestFreqInputFactor;
        double valueFactor = this.lowestFreqValueFactor;

        for(int i = 0; i < this.noiseLevels.length; ++i) {
            ImprovedNoise noise = this.noiseLevels[i];
            if (noise != null) {
                double[] grad = new double[3];
                double noiseVal = noise.noiseWithDerivative(PerlinNoise.wrap(x * factor), PerlinNoise.wrap(y * factor), PerlinNoise.wrap(z * factor), grad);
                gradOut[0] += grad[0];
                gradOut[1] += grad[1];
                gradOut[2] += grad[2];
                value += this.amplitudes.getDouble(i) * noiseVal * valueFactor;
            }

            factor *= 2.0F;
            valueFactor /= 2.0F;
        }

        return value;
    }
}
