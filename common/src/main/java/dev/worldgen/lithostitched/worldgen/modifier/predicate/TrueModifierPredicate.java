package dev.worldgen.lithostitched.worldgen.modifier.predicate;

import com.mojang.serialization.MapCodec;

public class TrueModifierPredicate implements ModifierPredicate {
    public static final TrueModifierPredicate INSTANCE = new TrueModifierPredicate();
    public static final MapCodec<TrueModifierPredicate> CODEC = MapCodec.unit(INSTANCE);
    @Override
    public boolean test() {
        return true;
    }

    @Override
    public MapCodec<? extends ModifierPredicate> codec() {
        return CODEC;
    }
}
