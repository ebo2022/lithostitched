package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.mixin.common.PlacedFeatureAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record RedirectFeatureModifier(Holder<PlacedFeature> placedFeature, Holder<ConfiguredFeature<?, ?>> redirectTo) implements Modifier {
    public static final MapCodec<RedirectFeatureModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PlacedFeature.CODEC.fieldOf("placed_feature").forGetter(RedirectFeatureModifier::placedFeature),
        ConfiguredFeature.CODEC.fieldOf("redirect_to").forGetter(RedirectFeatureModifier::redirectTo)
    ).apply(instance, RedirectFeatureModifier::new));

    @Override
    public void applyModifier() {
        ((PlacedFeatureAccessor)(Object)this.placedFeature().value()).setFeature(this.redirectTo());
    }

    @Override
    public ModifierPhase getPhase() {
        return ModifierPhase.MODIFY;
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}
