package dev.worldgen.lithostitched.mixin.common;

import dev.worldgen.lithostitched.duck.NoiseChunkDuck;
import dev.worldgen.lithostitched.worldgen.vectorfunction.NoiseChunkVectorFunction;
import dev.worldgen.lithostitched.worldgen.vectorfunction.VectorFunction;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(NoiseChunk.class)
public abstract class NoiseChunkMixin implements NoiseChunkDuck {
    @Shadow @Final private DensityFunction.ContextProvider sliceFillingContextProvider;
    @Shadow @Final public int cellCountY;
    @Shadow private int cellStartBlockY;
    @Shadow @Final private int cellNoiseMinY;
    @Shadow @Final public int cellHeight;
    @Shadow public long interpolationCounter;
    @Shadow public int inCellY;
    @Shadow public int arrayIndex;
    @Unique private final List<NoiseChunkVectorFunction.CacheAllInCell> lithostitched$vectorCellCaches = new ArrayList<>();
    @Unique private final List<NoiseChunkVectorFunction.NoiseInterpolator> lithostitched$vectorInterpolators = new ArrayList<>();
    @Unique private VectorFunction.ContextProvider lithostitched$vectorSliceFillingContextProvider;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(int cellCountXZ, RandomState randomState, int chunkMinBlockX, int chunkMinBlockZ, NoiseSettings noiseSettings, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings settings, Aquifer.FluidPicker globalFluidPicker, Blender blender, CallbackInfo ci) {
        this.lithostitched$vectorSliceFillingContextProvider = new VectorFunction.ContextProvider() {
            @Override
            public void fillAllDirectly(Vec3[] output, VectorFunction function) {
                for(int cellYIndex = 0; cellYIndex < NoiseChunkMixin.this.cellCountY + 1; ++cellYIndex) {
                    NoiseChunkMixin.this.cellStartBlockY = (cellYIndex + NoiseChunkMixin.this.cellNoiseMinY) * NoiseChunkMixin.this.cellHeight;
                    ++NoiseChunkMixin.this.interpolationCounter;
                    NoiseChunkMixin.this.inCellY = 0;
                    NoiseChunkMixin.this.arrayIndex = cellYIndex;
                    output[cellYIndex] = function.compute((DensityFunction.FunctionContext) NoiseChunkMixin.this);
                }
            }

            @Override
            public DensityFunction.FunctionContext forIndex(int i) {
                return NoiseChunkMixin.this.sliceFillingContextProvider.forIndex(i);
            }

            @Override
            public void fillAllDirectly(double[] array, DensityFunction function) {
                NoiseChunkMixin.this.sliceFillingContextProvider.fillAllDirectly(array, function);
            }
        };
    }

    @Inject(method = "wrapNew", at = @At("HEAD"))
    private void wrapNew(DensityFunction par1, CallbackInfoReturnable<DensityFunction> cir) {

    }

    @Override
    public List<NoiseChunkVectorFunction.CacheAllInCell> lithostitched$vectorCellCaches() {
        return this.lithostitched$vectorCellCaches;
    }

    @Override
    public List<NoiseChunkVectorFunction.NoiseInterpolator> lithostitched$vectorInterpolators() {
        return this.lithostitched$vectorInterpolators;
    }
}
