package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.worldgen.terrain.TerrainRule;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A {@link Modifier} implementation that adds a terrain
 */
public record TerrainModifier(HolderSet<Biome> biomes, Stage stage, TerrainRule rule) implements Modifier {
    public static final MapCodec<TerrainModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(TerrainModifier::biomes),
            Stage.CODEC.fieldOf("stage").forGetter(TerrainModifier::stage),
            TerrainRule.CODEC.fieldOf("rule").forGetter(TerrainModifier::rule)
    ).apply(instance, TerrainModifier::new));


    @Override
    public void applyModifier() {

    }

    @Override
    public ModifierPhase getPhase() {
        return ModifierPhase.NONE;
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return null;
    }

    public static Map<Holder<Biome>, TerrainModifier> createCheckedMap(Stage stage, Registry<TerrainModifier> registry) {
        Map<Holder<Biome>, TerrainModifier> map = new Object2ObjectOpenHashMap<>();
        registry.entrySet().stream().filter(entry -> !entry.getKey().location().getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) && entry.getValue().stage == stage).forEach(entry ->
                entry.getValue().biomes.forEach(biome -> {
                    TerrainModifier modifier = entry.getValue();
                    TerrainModifier existing = map.put(biome, modifier);
                    if (existing != null)
                        throw new IllegalStateException(
                                "Forbidden overlap between terrain modifiers "
                                        + registry.getKey(existing)
                                        + " and "
                                        + entry.getKey().location()
                                        + "; both are trying to modify biome "
                                        + biome.unwrapKey().orElseThrow().location()
                        );
                })
        );
        return map;
    }

    public enum Stage implements StringRepresentable {
        PRE_SURFACE("pre_surface"),
        POST_SURFACE("post_surface");

        private final String name;
        public static final Codec<Stage> CODEC = StringRepresentable.fromEnum(Stage::values);

        Stage(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
