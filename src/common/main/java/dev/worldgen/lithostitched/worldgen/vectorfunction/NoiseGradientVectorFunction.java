package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.duck.NoiseDuck;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record NoiseGradientVectorFunction(DensityFunction.NoiseHolder noise, double xzScale, double yScale) implements VectorFunction {
    public static final MapCodec<NoiseGradientVectorFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(NoiseGradientVectorFunction::noise),
            Codec.DOUBLE.fieldOf("xz_scale").forGetter(NoiseGradientVectorFunction::xzScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(NoiseGradientVectorFunction::yScale)
    ).apply(instance, NoiseGradientVectorFunction::new));

    @Override
    public Vec3 compute(FunctionContext context) {
        double[] grad = new double[3];
        ((NoiseDuck) (Object) this.noise).lithostitched$withGradient(this.xzScale * context.blockX(), this.yScale * context.blockY(), this.xzScale * context.blockZ(), grad);
        return new Vec3(grad[0], grad[1], grad[2]);
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        provider.fillAllDirectly(array, this);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.apply(new NoiseGradientVectorFunction(visitor.visitNoise(this.noise), this.xzScale, this.yScale));
    }

    @Override
    public MapCodec<NoiseGradientVectorFunction> codec() {
        return CODEC;
    }
}
