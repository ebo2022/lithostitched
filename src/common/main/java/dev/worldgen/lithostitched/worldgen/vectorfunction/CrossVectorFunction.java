package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record CrossVectorFunction(VectorFunction argument1, VectorFunction argument2, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) implements VectorFunction.PureBiTransformer {
    public static final MapCodec<CrossVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, CrossVectorFunction::new, CrossVectorFunction::argument1, CrossVectorFunction::argument2);

    public CrossVectorFunction(VectorFunction argument1, VectorFunction argument2) {
        this(
                argument1, argument2,
                argument1.minY() * argument2.minZ() - argument1.maxZ() * argument2.maxY(),
                argument1.maxY() * argument2.maxZ() - argument1.minZ() * argument2.minY(),
                argument1.minZ() * argument2.minX() - argument1.maxX() * argument2.maxZ(),
                argument1.maxZ() * argument2.maxX() - argument1.minX() * argument2.maxZ(),
                argument1.minX() * argument2.minY() - argument1.maxY() * argument2.maxX(),
                argument1.maxX() * argument2.maxY() - argument2.minY() * argument2.minX()
        );
    }

    @Override
    public Vec3 transform(Vec3 argument1, Vec3 argument2) {
        return argument1.cross(argument2);
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.visit(new CrossVectorFunction(this.argument1.mapAll(visitor), this.argument2.mapAll(visitor)));
    }

    @Override
    public MapCodec<CrossVectorFunction> codec() {
        return CODEC;
    }
}
