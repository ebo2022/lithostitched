package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record ConstantVectorFunction(Vec3 value) implements VectorFunction.SimpleFunction {
    public static final MapCodec<ConstantVectorFunction> CODEC = LithostitchedCodecs.singleArgument(Vec3.CODEC, ConstantVectorFunction::new, ConstantVectorFunction::value);

    @Override
    public Vec3 compute(FunctionContext context) {
        return this.value;
    }

    @Override
    public MapCodec<ConstantVectorFunction> codec() {
        return CODEC;
    }
}
