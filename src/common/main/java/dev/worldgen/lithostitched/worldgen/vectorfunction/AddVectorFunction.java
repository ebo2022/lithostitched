package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record AddVectorFunction(VectorFunction argument1, VectorFunction argument2) implements VectorFunction.PureBiTransformer {
    public static final MapCodec<AddVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, AddVectorFunction::new, AddVectorFunction::argument1, AddVectorFunction::argument2);

    @Override
    public Vec3 transform(Vec3 argument1, Vec3 argument2) {
        return argument1.add(argument2);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return new AddVectorFunction(visitor.apply(this.argument1), visitor.apply(this.argument2));
    }

    @Override
    public MapCodec<AddVectorFunction> codec() {
        return CODEC;
    }
}
