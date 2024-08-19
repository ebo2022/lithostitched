package dev.worldgen.lithostitched.worldgen.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.registry.LithostitchedRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.function.Function;

public interface TerrainRule {
    @SuppressWarnings("unchecked")
    Codec<TerrainRule> CODEC = Codec.lazyInitialized(() -> {
        var ruleRegistry = BuiltInRegistries.REGISTRY.get(LithostitchedRegistries.TERRAIN_RULE_TYPE.location());
        if (ruleRegistry == null) throw new NullPointerException("Terrain rule registry does not exist yet!");
        return ((Registry<MapCodec<? extends TerrainRule>>) ruleRegistry).byNameCodec();
    }).dispatch(TerrainRule::codec, Function.identity());



    MapCodec<? extends TerrainRule> codec();

    final class Context implements DensityFunction.FunctionContext {

        private final BlockPos.MutableBlockPos cursor;

        private Context(BlockPos.MutableBlockPos cursor) {
            this.cursor = cursor;
        }

        @Override
        public int blockX() {
            return this.cursor.getX();
        }

        @Override
        public int blockY() {
            return this.cursor.getY();
        }

        @Override
        public int blockZ() {
            return this.cursor.getZ();
        }

        public enum Mode {

        }
    }

    interface Condition {}
}
