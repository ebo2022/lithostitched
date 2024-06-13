package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.registry.LithostitchedRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The interface used for applying worldgen modifiers.
 *
 * @author Apollo
 */
public interface Modifier {
    @SuppressWarnings("unchecked")
    Codec<Modifier> CODEC = Codec.lazyInitialized(() -> {
        var modifierRegistry = BuiltInRegistries.REGISTRY.get(LithostitchedRegistries.MODIFIER_TYPE.location());
        if (modifierRegistry == null) throw new NullPointerException("Worldgen modifier registry does not exist yet!");
        return ((Registry<MapCodec<? extends Modifier>>) modifierRegistry).byNameCodec();
    }).dispatch(Modifier::codec, Function.identity());

    void applyModifier();

    ModifierPhase getPhase();

    MapCodec<? extends Modifier> codec();

    // Apply all worldgen modifiers in the worldgen modifier registry
    static void applyModifiers(MinecraftServer server) {
        Registry<Modifier> modifiers = server.registryAccess().registryOrThrow(LithostitchedRegistries.WORLDGEN_MODIFIER);
        for (ModifierPhase phase : ModifierPhase.values()) {
            if (phase == ModifierPhase.NONE) continue;
            for (Modifier modifier : modifiers.stream().filter(modifier -> modifier.getPhase() == phase).collect(Collectors.toSet())) {
                modifier.applyModifier();
            }
        }
    }

    enum ModifierPhase implements StringRepresentable {
        /**
         * Phase for modifiers to never apply.
         * Useful for modifiers that don't use the regular modifier system for applying modifications, like Forge biome modifiers and the AddSurfaceRule modifier.
         */
        NONE("none"),

        /**
         * Phase for modifiers that need to run before any other steps.
         */
        BEFORE_ALL("before_all"),

        /**
         * Phase for modifiers that add to worldgen, such as template pool and structure set additions.
         */
        ADD("add"),

        /**
         * Phase for modifiers that remove from worldgen, such as feature and mob spawn removals.
         */
        REMOVE("remove"),

        /**
         * Phase for modifiers that replace/modify parts of worldgen, like climate replacements and placed feature redirections.
         */
        MODIFY("modify"),

        /**
         * Phase for modifiers that need to run after all other steps.
         */
        AFTER_ALL("after_all");

        private final String name;

        ModifierPhase(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
