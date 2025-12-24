package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public enum PositionVectorFunction implements VectorFunction.SimpleFunction {
    INSTANCE;

    public static final MapCodec<PositionVectorFunction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Vec3 compute(DensityFunction.FunctionContext context) {
        return new Vec3(context.blockX(), context.blockY(), context.blockZ());
    }

    @Override
    public double minX() {
        return -33554432;
    }

    @Override
    public double maxX() {
        return 33554431;
    }

    @Override
    public double minY() {
        return -6144;
    }

    @Override
    public double maxY() {
        return 6143;
    }

    @Override
    public double minZ() {
        return -33554432;
    }

    @Override
    public double maxZ() {
        return 33554431;
    }

    @Override
    public MapCodec<PositionVectorFunction> codec() {
        return CODEC;
    }
}
