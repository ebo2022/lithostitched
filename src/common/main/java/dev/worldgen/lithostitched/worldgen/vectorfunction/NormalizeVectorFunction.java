package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record NormalizeVectorFunction(VectorFunction input) implements VectorFunction.PureTransformer {
    public static final MapCodec<NormalizeVectorFunction> CODEC = LithostitchedCodecs.singleArgument(VectorFunction.HOLDER_HELPER_CODEC, NormalizeVectorFunction::new, NormalizeVectorFunction::input);

    @Override
    public Vec3 transform(Vec3 input) {
        return input.normalize();
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.apply(new NormalizeVectorFunction(visitor.apply(this.input)));
    }

    @Override
    public MapCodec<NormalizeVectorFunction> codec() {
        return CODEC;
    }
}
