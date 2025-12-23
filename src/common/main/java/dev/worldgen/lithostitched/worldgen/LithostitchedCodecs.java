package dev.worldgen.lithostitched.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.util.weighted.WeightedList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Collection of Codecs used by Lithostitched.
 * @author Apollo
 */
public interface LithostitchedCodecs {
    Codec<HolderSet<Block>> BLOCK_SET = RegistryCodecs.homogeneousList(Registries.BLOCK);
    MapCodec<Float> CHANCE = Codec.floatRange(0.0F, 1.0F).fieldOf("chance");
    Codec<InclusiveRange<Integer>> INT_RANGE = Codec.withAlternative(
        InclusiveRange.INT,
        RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min_inclusive").orElse(Integer.MIN_VALUE).forGetter(InclusiveRange::minInclusive),
            Codec.INT.fieldOf("max_inclusive").orElse(Integer.MAX_VALUE).forGetter(InclusiveRange::maxInclusive)
        ).apply(instance, InclusiveRange::new))
    );

    static <T> MapCodec<HolderSet<T>> registrySet(ResourceKey<Registry<T>> registry, String name) {
        return RegistryCodecs.homogeneousList(registry).fieldOf(name);
    }

    static <T> Codec<List<T>> compactList(Codec<T> codec) {
        return Codec.withAlternative(codec.listOf(), codec, List::of);
    }

    static <T> Codec<WeightedList<T>> compactWeightedList(Codec<T> codec, boolean allowsEmpty) {
        Codec<WeightedList<T>> weightedCodec = allowsEmpty ? WeightedList.codec(codec) : WeightedList.nonEmptyCodec(codec);
        return Codec.withAlternative(weightedCodec, codec, WeightedList::of);
    }

    static <A, T> MapCodec<T> singleArgument(Codec<A> codec, Function<A, T> to, Function<T, A> from) {
        return codec.fieldOf("argument").xmap(to, from);
    }

    static <A, B, T> MapCodec<T> doubleArgument(Codec<A> codec1, Codec<B> codec2, BiFunction<A, B, T> to, Function<T, A> from1, Function<T, B> from2) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec1.fieldOf("argument1").forGetter(from1),
                codec2.fieldOf("argument2").forGetter(from2)
        ).apply(instance, to));
    }

    static <A, T> MapCodec<T> doubleArgument(Codec<A> codec, BiFunction<A, A, T> to, Function<T, A> from1, Function<T, A> from2) {
        return doubleArgument(codec, codec, to, from1, from2);
    }
}
