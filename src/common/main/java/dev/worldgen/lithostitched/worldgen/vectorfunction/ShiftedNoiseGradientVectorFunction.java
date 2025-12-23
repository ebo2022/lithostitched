package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.duck.NoiseDuck;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record ShiftedNoiseGradientVectorFunction(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale, double yScale, DensityFunction.NoiseHolder noise) implements VectorFunction {
    public static final MapCodec<ShiftedNoiseGradientVectorFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(ShiftedNoiseGradientVectorFunction::shiftX),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(ShiftedNoiseGradientVectorFunction::shiftY),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(ShiftedNoiseGradientVectorFunction::shiftZ),
            Codec.DOUBLE.fieldOf("xz_scale").forGetter(ShiftedNoiseGradientVectorFunction::xzScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(ShiftedNoiseGradientVectorFunction::yScale),
            DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ShiftedNoiseGradientVectorFunction::noise)
    ).apply(instance, ShiftedNoiseGradientVectorFunction::new));

    @Override
    public Vec3 compute(FunctionContext context) {
        double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
        double y = context.blockY() * this.yScale + this.shiftY.compute(context);
        double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
        double[] grad = new double[3];
        ((NoiseDuck) (Object) this.noise).lithostitched$withGradient(x, y, z, grad);
        return new Vec3(grad[0], grad[1], grad[2]);
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        provider.fillAllDirectly(array, this);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.apply(new ShiftedNoiseGradientVectorFunction(visitor.visitDensity(this.shiftX), visitor.visitDensity(this.shiftY), visitor.visitDensity(this.shiftZ), this.xzScale, this.yScale, visitor.visitNoise(this.noise)));
    }

    @Override
    public MapCodec<ShiftedNoiseGradientVectorFunction> codec() {
        return CODEC;
    }
}
