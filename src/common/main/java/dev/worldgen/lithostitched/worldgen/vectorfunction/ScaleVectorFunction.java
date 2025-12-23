package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record ScaleVectorFunction(VectorFunction argument1, DensityFunction argument2) implements VectorFunction.TransformerWithContext, VectorFunction.SimpleFunction {
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
    public MapCodec<? extends VectorFunction> codec() {
        return null;
    }
}
