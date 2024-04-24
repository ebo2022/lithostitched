package dev.worldgen.lithostitched.worldgen.modifier.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.platform.Services;

public record ModLoadedModifierPredicate(String modId) implements ModifierPredicate {
    public static final MapCodec<ModLoadedModifierPredicate> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
        Codec.STRING.fieldOf("mod_id").forGetter(ModLoadedModifierPredicate::modId)
    ).apply(instance, ModLoadedModifierPredicate::new));
    @Override
    public boolean test() {
        return Services.PLATFORM.isModLoaded(this.modId());
    }

    @Override
    public MapCodec<? extends ModifierPredicate> codec() {
        return CODEC;
    }
}
