package dev.worldgen.lithostitched.duck;

import dev.worldgen.lithostitched.worldgen.vectorfunction.NoiseChunkVectorFunction;

import java.util.List;

public interface NoiseChunkDuck {
    List<NoiseChunkVectorFunction.CacheAllInCell> lithostitched$vectorCellCaches();
    List<NoiseChunkVectorFunction.NoiseInterpolator> lithostitched$vectorInterpolators();
}
