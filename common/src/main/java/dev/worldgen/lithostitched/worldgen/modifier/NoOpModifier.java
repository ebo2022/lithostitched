package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.worldgen.modifier.predicate.ModifierPredicate;

/**
 * A {@link Modifier} implementation that does nothing.
 * <p>Useful for overriding worldgen modifiers from other datapacks/mods</p>
 *
 * @author Apollo
 */
public class NoOpModifier extends Modifier {

    public static final MapCodec<NoOpModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> addModifierFields(instance).apply(instance, NoOpModifier::new));

    protected NoOpModifier(ModifierPredicate predicate) {
        super(predicate, ModifierPhase.NONE);
    }

    @Override
    public void applyModifier() {}

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
