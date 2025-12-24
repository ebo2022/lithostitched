package dev.worldgen.lithostitched.worldgen.densityfunction;

import net.minecraft.world.level.levelgen.DensityFunction;

public interface MarkerDensityFunction extends DensityFunction.SimpleFunction {
    @Override
    default double compute(FunctionContext context) {
        throw new IllegalStateException("Marker density function should never be computed!");
    }

    @Override
    default double minValue() {
        return 0;
    }

    @Override
    default double maxValue() {
        return 0;
    }
}
