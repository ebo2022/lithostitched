package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record ScaleVectorFunction(VectorFunction argument1, DensityFunction argument2) implements VectorFunction.TransformerWithContext {
    public static final MapCodec<ScaleVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(
            VectorFunction.HOLDER_HELPER_CODEC,
            DensityFunction.HOLDER_HELPER_CODEC,
            ScaleVectorFunction::new,
            ScaleVectorFunction::argument1,
            ScaleVectorFunction::argument2
    );

    @Override
    public VectorFunction input() {
        return this.argument1;
    }

    @Override
    public Vec3 transform(Vec3 input, FunctionContext context) {
        return input.scale(this.argument2.compute(context));
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        this.argument1.fillArray(array, provider);

        double[] array2 = new double[array.length];
        this.argument2.fillArray(array2, provider);

        for (int i = 0; i < array.length; ++i) {
            array[i] = array[i].scale(array2[i]);
        }
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.apply(new ScaleVectorFunction(visitor.apply(this.argument1), visitor.visitDensity(this.argument2)));
    }

    @Override
    public MapCodec<? extends VectorFunction> codec() {
        return CODEC;
    }
}
