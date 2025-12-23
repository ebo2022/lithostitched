package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.phys.Vec3;

public enum PositionVectorFunction implements VectorFunction.SimpleFunction {
    INSTANCE;

    public static final MapCodec<PositionVectorFunction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Vec3 compute(FunctionContext context) {
        return new Vec3(context.blockX(), context.blockY(), context.blockZ());
    }

    @Override
    public MapCodec<PositionVectorFunction> codec() {
        return CODEC;
    }
}
