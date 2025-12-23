package dev.worldgen.lithostitched.mixin.common;

import dev.worldgen.lithostitched.duck.NoiseDuck;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NormalNoise.class)
public class NormalNoiseMixin implements NoiseDuck {
    @Shadow @Final private double valueFactor;
    @Shadow @Final private PerlinNoise first;
    @Shadow @Final private PerlinNoise second;

    @Override
    public double lithostitched$withGradient(double x, double y, double z, double[] gradOut) {
        double x2 = x * 1.0181268882175227;
        double y2 = y * 1.0181268882175227;
        double z2 = z * 1.0181268882175227;
        double val = (this.first.getValue(x, y, z) + this.second.getValue(x2, y2, z2)) * this.valueFactor;

        double[] firstGrad = new double[3];
        ((NoiseDuck) this.first).lithostitched$withGradient(x, y, z, firstGrad);
        double[] secondGrad = new double[3];
        ((NoiseDuck) this.second).lithostitched$withGradient(x2, y2, z2, secondGrad);

        gradOut[0] = firstGrad[0] + secondGrad[0];
        gradOut[1] = firstGrad[1] + secondGrad[1];
        gradOut[2] = firstGrad[2] + secondGrad[2];

        return val;
    }
}
