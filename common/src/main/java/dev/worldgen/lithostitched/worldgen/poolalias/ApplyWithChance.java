package dev.worldgen.lithostitched.worldgen.poolalias;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record ApplyWithChance(float chance, List<PoolAliasBinding> poolAliases) implements PoolAliasBinding {
    public static final MapCodec<ApplyWithChance> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.floatRange(0, 1).fieldOf("chance").forGetter(ApplyWithChance::chance),
        Codec.list(PoolAliasBinding.CODEC).fieldOf("pool_aliases").forGetter(ApplyWithChance::poolAliases)
    ).apply(instance, ApplyWithChance::new));


    @Override
    public void forEachResolved(RandomSource random, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> consumer) {
        if (this.chance > 0 && random.nextFloat() < this.chance) {
            for (PoolAliasBinding poolAlias : this.poolAliases) {
                poolAlias.forEachResolved(random, consumer);
            }
        }
    }

    // This should never run, it appears it's only used in vanilla data generation.
    @Override
    public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
        return Stream.of();
    }

    @Override
    public MapCodec<? extends PoolAliasBinding> codec() {
        return CODEC;
    }
}
