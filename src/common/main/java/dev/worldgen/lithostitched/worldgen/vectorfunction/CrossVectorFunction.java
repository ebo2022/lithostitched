package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record CrossVectorFunction(VectorFunction argument1, VectorFunction argument2) implements VectorFunction.PureBiTransformer {
    public static final MapCodec<CrossVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, CrossVectorFunction::new, CrossVectorFunction::argument1, CrossVectorFunction::argument2);

    @Override
    public Vec3 transform(Vec3 argument1, Vec3 argument2) {
        return argument1.cross(argument2);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.apply(new CrossVectorFunction(visitor.apply(this.argument1), visitor.apply(this.argument2)));
    }

    @Override
    public MapCodec<CrossVectorFunction> codec() {
        return CODEC;
    }
}
