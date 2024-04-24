package dev.worldgen.lithostitched.worldgen.modifier.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.registry.LithostitchedRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;


public interface ModifierPredicate {
    @SuppressWarnings("unchecked")
    Codec<ModifierPredicate> CODEC = Codec.lazyInitialized(() -> {
        var predicateRegistry = BuiltInRegistries.REGISTRY.get(LithostitchedRegistries.MODIFIER_PREDICATE_TYPE.location());
        if (predicateRegistry == null) throw new NullPointerException("Modifier predicate type registry does not exist yet!");
        return ((Registry<MapCodec<? extends ModifierPredicate>>) predicateRegistry).byNameCodec();
    }).dispatch(ModifierPredicate::codec, Function.identity());

    boolean test();

    MapCodec<? extends ModifierPredicate> codec();
}

