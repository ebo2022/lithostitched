package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record AddVectorFunction(VectorFunction argument1, VectorFunction argument2, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) implements VectorFunction.PureBiTransformer {
    public static final MapCodec<AddVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, AddVectorFunction::new, AddVectorFunction::argument1, AddVectorFunction::argument2);

    public AddVectorFunction(VectorFunction argument1, VectorFunction argument2) {
        this(
                argument1, argument2,
                argument1.minX() + argument2.minX(),
                argument1.maxX() + argument2.maxX(),
                argument1.minY() + argument2.minY(),
                argument1.maxY() + argument2.maxY(),
                argument1.minZ() + argument2.minZ(),
                argument1.maxZ() + argument2.maxZ()
        );
    }

    @Override
    public Vec3 transform(Vec3 argument1, Vec3 argument2) {
        return argument1.add(argument2);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.visit(new AddVectorFunction(this.argument1.mapAll(visitor), this.argument2.mapAll(visitor)));
    }

    @Override
    public MapCodec<AddVectorFunction> codec() {
        return CODEC;
    }
}
