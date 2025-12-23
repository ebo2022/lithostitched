package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record ComponentsVectorFunction(DensityFunction x, DensityFunction y, DensityFunction z) implements VectorFunction {
    public static final MapCodec<ComponentsVectorFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("x").forGetter(ComponentsVectorFunction::x),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("y").forGetter(ComponentsVectorFunction::y),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("z").forGetter(ComponentsVectorFunction::z)
    ).apply(instance, ComponentsVectorFunction::new));

    @Override
    public Vec3 compute(FunctionContext context) {
        return new Vec3(this.x.compute(context), this.y.compute(context), this.z.compute(context));
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        double[] xArray = new double[array.length];
        double[] yArray = new double[array.length];
        double[] zArray = new double[array.length];
        this.x.fillArray(xArray, provider);
        this.y.fillArray(yArray, provider);
        this.z.fillArray(zArray, provider);

        for (int i = 0; i < array.length; ++i) {
            array[i] = new Vec3(xArray[i], yArray[i], zArray[i]);
        }
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return new ComponentsVectorFunction(visitor.visitDensity(this.x), visitor.visitDensity(this.y), visitor.visitDensity(this.z));
    }

    @Override
    public MapCodec<ComponentsVectorFunction> codec() {
        return CODEC;
    }
}
