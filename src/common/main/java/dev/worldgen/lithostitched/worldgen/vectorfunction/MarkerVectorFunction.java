package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record MarkerVectorFunction(Type type, VectorFunction wrapped) implements VectorFunction.MarkerOrMarked {
    @Override
    public Vec3 compute(DensityFunction.FunctionContext context) {
        return this.wrapped.compute(context);
    }

    @Override
    public void fillArray(Vec3[] output, ContextProvider provider) {
        this.wrapped.fillArray(output, provider);
    }

    @Override
    public MapCodec<? extends VectorFunction> codec() {
        return this.type.codec;
    }

    public enum Type implements StringRepresentable {
        Interpolated("interpolated"),
        FlatCache("flat_cache"),
        Cache2D("cache_2d"),
        CacheOnce("cache_once"),
        CacheAllInCell("cache_all_in_cell");

        private final String name;
        private final MapCodec<MarkerVectorFunction> codec = LithostitchedCodecs.singleArgument(VectorFunction.HOLDER_HELPER_CODEC, wrapped -> new MarkerVectorFunction(this, wrapped), MarkerVectorFunction::wrapped);

        Type(final String $$0) {
            this.name = $$0;
        }

        public String getSerializedName() {
            return this.name;
        }

        public MapCodec<MarkerVectorFunction> codec() {
            return this.codec;
        }
    }
}
