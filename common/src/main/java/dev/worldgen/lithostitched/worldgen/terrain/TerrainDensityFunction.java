package dev.worldgen.lithostitched.worldgen.terrain;

import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.jetbrains.annotations.Nullable;

/**
 * A group of specialized {@link DensityFunction}s used for terrain rules.
 * <p>Do not use these functions for regular worldgen; the game will crash if you attempt to do so.
 *
 * @author ebo2022
 */
public interface TerrainDensityFunction extends DensityFunction {
    double compute(Context context);

    @Override
    default double compute(FunctionContext context) {
        if (context instanceof Context context1) {
            return this.compute(context1);
        } else {
            throw new IllegalArgumentException("Extended context required for terrain density functions");
        }
    };

    interface Context extends DensityFunction.FunctionContext {
        int surfaceHeight();

        @Nullable
        Integer minSurfaceHeight();

        LevelHeightAccessor heightAccessor();

        Holder<Biome> currentBiome();

        PositionalRandomFactory random();

        BlockState defaultBlock();

        int seaLevel();
    }
}
