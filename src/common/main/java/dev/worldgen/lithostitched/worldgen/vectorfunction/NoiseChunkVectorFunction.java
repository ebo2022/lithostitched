package dev.worldgen.lithostitched.worldgen.vectorfunction;

import dev.worldgen.lithostitched.duck.NoiseChunkDuck;
import net.minecraft.core.QuartPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class NoiseChunkVectorFunction implements VectorFunction.MarkerOrMarked {
    protected final NoiseChunk noiseChunk;
    protected final VectorFunction wrapped;

    protected NoiseChunkVectorFunction(NoiseChunk noiseChunk, VectorFunction wrapped) {
        this.noiseChunk = noiseChunk;
        this.wrapped = wrapped;
    }

    @Override
    public VectorFunction wrapped() {
        return this.wrapped;
    }

    public static class FlatCache extends NoiseChunkVectorFunction {
        final Vec3[] values;
        final int sizeXZ;

        public FlatCache(NoiseChunk noiseChunk, VectorFunction noiseFiller, boolean fill) {
            super(noiseChunk, noiseFiller);
            this.sizeXZ = this.noiseChunk.noiseSizeXZ + 1;
            this.values = new Vec3[this.sizeXZ * this.sizeXZ];
            if (fill) {
                for(int x = 0; x <= this.noiseChunk.noiseSizeXZ; ++x) {
                    int quartX = this.noiseChunk.firstNoiseX + x;
                    int blockX = QuartPos.toBlock(quartX);

                    for(int z = 0; z <= this.noiseChunk.noiseSizeXZ; ++z) {
                        int quartZ = this.noiseChunk.firstNoiseZ + z;
                        int blockZ = QuartPos.toBlock(quartZ);
                        this.values[x + z * this.sizeXZ] = noiseFiller.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ));
                    }
                }
            }

        }

        @Override
        public Vec3 compute(DensityFunction.FunctionContext context) {
            int quartX = QuartPos.fromBlock(context.blockX());
            int quartZ = QuartPos.fromBlock(context.blockZ());
            int x = quartX - this.noiseChunk.firstNoiseX;
            int z = quartZ - this.noiseChunk.firstNoiseZ;
            return x >= 0 && z >= 0 && x < this.sizeXZ && z < this.sizeXZ ? this.values[x + z * this.sizeXZ] : this.wrapped.compute(context);
        }

        @Override
        public void fillArray(Vec3[] output, ContextProvider provider) {
            provider.fillAllDirectly(output, this);
        }

        @Override
        public MarkerVectorFunction.Type type() {
            return MarkerVectorFunction.Type.FlatCache;
        }
    }

    public static  class CacheAllInCell extends NoiseChunkVectorFunction {
        private final Vec3[] values;

        private CacheAllInCell(NoiseChunk noiseChunk, VectorFunction noiseFiller) {
            super(noiseChunk, noiseFiller);
            this.values = new Vec3[this.noiseChunk.cellWidth * this.noiseChunk.cellWidth * this.noiseChunk.cellHeight];
            ((NoiseChunkDuck) this.noiseChunk).lithostitched$vectorCellCaches().add(this);
        }

        public Vec3 compute(DensityFunction.FunctionContext context) {
            if (context != this.noiseChunk) {
                return this.wrapped.compute(context);
            } else if (!this.noiseChunk.interpolating) {
                throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop");
            } else {
                int x = this.noiseChunk.inCellX;
                int y = this.noiseChunk.inCellY;
                int z = this.noiseChunk.inCellZ;
                return x >= 0 && y >= 0 && z >= 0 && x < this.noiseChunk.cellWidth && y < this.noiseChunk.cellHeight && z < this.noiseChunk.cellWidth ? this.values[((this.noiseChunk.cellHeight - 1 - y) * this.noiseChunk.cellWidth + x) * this.noiseChunk.cellWidth + z] : this.wrapped.compute(context);
            }
        }

        public void fillArray(Vec3[] output, ContextProvider contextProvider) {
            contextProvider.fillAllDirectly(output, this);
        }

        @Override
        public MarkerVectorFunction.Type type() {
            return MarkerVectorFunction.Type.CacheAllInCell;
        }
    }

    public static class NoiseInterpolator extends NoiseChunkVectorFunction {
        private Vec3[][] slice0;
        private Vec3[][] slice1;
        private Vec3 noise000;
        private Vec3 noise001;
        private Vec3 noise100;
        private Vec3 noise101;
        private Vec3 noise010;
        private Vec3 noise011;
        private Vec3 noise110;
        private Vec3 noise111;
        private Vec3 valueXZ00;
        private Vec3 valueXZ10;
        private Vec3 valueXZ01;
        private Vec3 valueXZ11;
        private Vec3 valueZ0;
        private Vec3 valueZ1;
        private Vec3 value;

        public NoiseInterpolator(NoiseChunk noiseChunk, VectorFunction wrapped) {
            super(noiseChunk, wrapped);
            this.slice0 = this.allocateSlice(this.noiseChunk.cellCountY, this.noiseChunk.cellCountXZ);
            this.slice1 = this.allocateSlice(this.noiseChunk.cellCountY, this.noiseChunk.cellCountXZ);
            ((NoiseChunkDuck) this.noiseChunk).lithostitched$vectorInterpolators().add(this);
        }

        private Vec3[][] allocateSlice(int cellCountY, int cellCountZ) {
            int sizeZ = cellCountZ + 1;
            int sizeY = cellCountY + 1;
            Vec3[][] result = new Vec3[sizeZ][sizeY];

            for (int cellZIndex = 0; cellZIndex < sizeZ; ++cellZIndex) {
                result[cellZIndex] = new Vec3[sizeY];
            }

            return result;
        }

        private void selectCellYZ(final int cellYIndex, final int cellZIndex) {
            this.noise000 = this.slice0[cellZIndex][cellYIndex];
            this.noise001 = this.slice0[cellZIndex + 1][cellYIndex];
            this.noise100 = this.slice1[cellZIndex][cellYIndex];
            this.noise101 = this.slice1[cellZIndex + 1][cellYIndex];
            this.noise010 = this.slice0[cellZIndex][cellYIndex + 1];
            this.noise011 = this.slice0[cellZIndex + 1][cellYIndex + 1];
            this.noise110 = this.slice1[cellZIndex][cellYIndex + 1];
            this.noise111 = this.slice1[cellZIndex + 1][cellYIndex + 1];
        }

        private void updateForY(final double factorY) {
            this.valueXZ00 = lerp(factorY, this.noise000, this.noise010);
            this.valueXZ10 = lerp(factorY, this.noise100, this.noise110);
            this.valueXZ01 = lerp(factorY, this.noise001, this.noise011);
            this.valueXZ11 = lerp(factorY, this.noise101, this.noise111);
        }

        private void updateForX(double factorX) {
            this.valueZ0 = lerp(factorX, this.valueXZ00, this.valueXZ10);
            this.valueZ1 = lerp(factorX, this.valueXZ01, this.valueXZ11);
        }

        private void updateForZ(final double factorZ) {
            this.value = lerp(factorZ, this.valueZ0, this.valueZ1);
        }

        @Override
        public Vec3 compute(DensityFunction.FunctionContext context) {
            if (context != this.noiseChunk) {
                return this.wrapped.compute(context);
            } else if (!this.noiseChunk.interpolating) {
                throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop");
            } else {
                return this.noiseChunk.fillingCell ? lerp3(
                        (double) this.noiseChunk.inCellX / this.noiseChunk.cellWidth,
                        (double) this.noiseChunk.inCellY / this.noiseChunk.cellHeight,
                        (double) this.noiseChunk.inCellZ / this.noiseChunk.cellWidth,
                        this.noise000,
                        this.noise100,
                        this.noise010,
                        this.noise110,
                        this.noise001,
                        this.noise101,
                        this.noise011,
                        this.noise111
                ) : this.value;
            }
        }

        @Override
        public void fillArray(final Vec3[] output, ContextProvider contextProvider) {
            if (this.noiseChunk.fillingCell) {
                contextProvider.fillAllDirectly(output, this);
            } else {
                this.wrapped().fillArray(output, contextProvider);
            }
        }

        private void swapSlices() {
            Vec3[][] tmp = this.slice0;
            this.slice0 = this.slice1;
            this.slice1 = tmp;
        }

        @Override
        public MarkerVectorFunction.Type type() {
            return MarkerVectorFunction.Type.Interpolated;
        }

        public static Vec3 lerp(double alpha, Vec3 p1, Vec3 p2) {
            return new Vec3(Mth.lerp(alpha, p1.x, p2.x), Mth.lerp(alpha, p1.y, p2.y), Mth.lerp(alpha, p1.z, p2.z));
        }

        public static Vec3 lerp3(
                double alpha1,
                double alpha2,
                double alpha3,
                Vec3 p000,
                Vec3 p100,
                Vec3 p010,
                Vec3 p110,
                Vec3 p001,
                Vec3 p101,
                Vec3 p011,
                Vec3 p111
        ) {
            return new Vec3(
                    Mth.lerp(alpha3,
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p000.x, p100.x),
                                    Mth.lerp(alpha1, p010.x, p110.x)
                            ),
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p001.x, p101.x),
                                    Mth.lerp(alpha1, p011.x, p111.x)
                            )
                    ),
                    Mth.lerp(alpha3,
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p000.y, p100.y),
                                    Mth.lerp(alpha1, p010.y, p110.y)
                            ),
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p001.y, p101.y),
                                    Mth.lerp(alpha1, p011.y, p111.y)
                            )
                    ),
                    Mth.lerp(alpha3,
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p000.z, p100.z),
                                    Mth.lerp(alpha1, p010.z, p110.z)
                            ),
                            Mth.lerp(alpha2,
                                    Mth.lerp(alpha1, p001.z, p101.z),
                                    Mth.lerp(alpha1, p011.z, p111.z)
                            )
                    )
            );
        }
    }

    public static class CacheOnce extends NoiseChunkVectorFunction {
        private long lastCounter;
        private long lastArrayCounter;
        private Vec3 lastValue;
        @Nullable private Vec3[] lastArray;

        public CacheOnce(NoiseChunk noiseChunk, VectorFunction wrapped) {
            super(noiseChunk, wrapped);
        }

        @Override
        public Vec3 compute(DensityFunction.FunctionContext context) {
            if (context != this.noiseChunk) {
                return this.wrapped.compute(context);
            } else if (this.lastArray != null && this.lastArrayCounter == this.noiseChunk.arrayInterpolationCounter) {
                return this.lastArray[this.noiseChunk.arrayIndex];
            } else if (this.lastCounter == this.noiseChunk.interpolationCounter) {
                return this.lastValue;
            } else {
                this.lastCounter = this.noiseChunk.interpolationCounter;
                Vec3 value = this.wrapped.compute(context);
                this.lastValue = value;
                return value;
            }
        }

        @Override
        public void fillArray(Vec3[] output, ContextProvider contextProvider) {
            if (this.lastArray != null && this.lastArrayCounter == this.noiseChunk.arrayInterpolationCounter) {
                System.arraycopy(this.lastArray, 0, output, 0, output.length);
            } else {
                this.wrapped.fillArray(output, contextProvider);
                if (this.lastArray != null && this.lastArray.length == output.length) {
                    System.arraycopy(output, 0, this.lastArray, 0, output.length);
                } else {
                    this.lastArray = output.clone();
                }

                this.lastArrayCounter = this.noiseChunk.arrayInterpolationCounter;
            }
        }

        @Override
        public MarkerVectorFunction.Type type() {
            return MarkerVectorFunction.Type.CacheOnce;
        }
    }

    public static class Cache2D extends NoiseChunkVectorFunction {
        private long lastPos2D;
        private Vec3 lastValue;

        public Cache2D(NoiseChunk noiseChunk, VectorFunction wrapped) {
            super(noiseChunk, wrapped);
            this.lastPos2D = ChunkPos.INVALID_CHUNK_POS;
        }

        public Vec3 compute(DensityFunction.FunctionContext context) {
            int blockX = context.blockX();
            int blockZ = context.blockZ();
            long pos2D = ChunkPos.asLong(blockX, blockZ);
            if (this.lastPos2D == pos2D) {
                return this.lastValue;
            } else {
                this.lastPos2D = pos2D;
                Vec3 value = this.wrapped.compute(context);
                this.lastValue = value;
                return value;
            }
        }

        public void fillArray(Vec3[] output, ContextProvider contextProvider) {
            this.wrapped.fillArray(output, contextProvider);
        }

        @Override
        public MarkerVectorFunction.Type type() {
            return MarkerVectorFunction.Type.Cache2D;
        }
    }
}
