package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record ConstantVectorFunction(Vec3 value) implements VectorFunction.SimpleFunction {
    public static final MapCodec<ConstantVectorFunction> CODEC = LithostitchedCodecs.singleArgument(Vec3.CODEC, ConstantVectorFunction::new, ConstantVectorFunction::value);

    @Override
    public Vec3 compute(DensityFunction.FunctionContext context) {
        return this.value;
    }

    @Override
    public double minX() {
        return this.value.x;
    }

    @Override
    public double maxX() {
        return this.value.x;
    }

    @Override
    public double minY() {
        return this.value.y;
    }

    @Override
    public double maxY() {
        return this.value.y;
    }

    @Override
    public double minZ() {
        return this.value.z;
    }

    @Override
    public double maxZ() {
        return this.value.z;
    }

    @Override
    public MapCodec<ConstantVectorFunction> codec() {
        return CODEC;
    }
}
