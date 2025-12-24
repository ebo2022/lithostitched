package dev.worldgen.lithostitched.worldgen.densityfunction;

import dev.worldgen.lithostitched.worldgen.vectorfunction.VectorFunction;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

// assume the context is extended; fillArray should only end up being called in NoiseChunk
public interface VectorOpDensityFunction extends DensityFunction {
    VectorFunction argument1();

    VectorFunction argument2();

    double transform(Vec3 argument1, Vec3 argument2);

    @Override
    default double compute(FunctionContext context) {
        return this.transform(this.argument1().compute(context), this.argument2().compute(context));
    }

    @Override
    default void fillArray(double[] output, ContextProvider provider) {
        Vec3[] array1 = new Vec3[output.length];
        this.argument1().fillArray(array1, (VectorFunction.ContextProvider) provider);

        Vec3[] array2 = new Vec3[output.length];
        this.argument2().fillArray(array2, (VectorFunction.ContextProvider) provider);

        for (int i = 0; i < output.length; ++i) {
            output[i] = this.transform(array1[i], array2[i]);
        }
    }
}
