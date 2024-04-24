package dev.worldgen.lithostitched.worldgen.modifier.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record AllOfModifierPredicate(List<ModifierPredicate> predicates) implements ModifierPredicate {
    public static final MapCodec<AllOfModifierPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
        ModifierPredicate.CODEC.listOf().fieldOf("predicates").forGetter(AllOfModifierPredicate::predicates)
    ).apply(instance, AllOfModifierPredicate::new));
    @Override
    public boolean test() {
        for (ModifierPredicate predicate : this.predicates()) {
            if (!predicate.test()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public MapCodec<? extends ModifierPredicate> codec() {
        return CODEC;
    }
}
