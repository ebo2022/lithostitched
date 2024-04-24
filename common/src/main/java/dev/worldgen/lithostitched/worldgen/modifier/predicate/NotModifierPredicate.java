package dev.worldgen.lithostitched.worldgen.modifier.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NotModifierPredicate(ModifierPredicate predicate) implements ModifierPredicate {
    public static final MapCodec<NotModifierPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
        ModifierPredicate.CODEC.fieldOf("predicate").forGetter(NotModifierPredicate::predicate)
    ).apply(instance, NotModifierPredicate::new));
    @Override
    public boolean test() {
        return !predicate().test();
    }

    @Override
    public MapCodec<? extends ModifierPredicate> codec() {
        return CODEC;
    }
}
