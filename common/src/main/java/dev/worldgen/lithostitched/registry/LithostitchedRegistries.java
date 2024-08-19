package dev.worldgen.lithostitched.registry;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.LithostitchedCommon;
import dev.worldgen.lithostitched.worldgen.modifier.Modifier;
import dev.worldgen.lithostitched.worldgen.terrain.TerrainRule;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * Class containing the resource keys of every registry registered by Lithostitched.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class LithostitchedRegistries {
	public static final ResourceKey<Registry<Modifier>> WORLDGEN_MODIFIER = createRegistryKey("worldgen_modifier");
	public static final ResourceKey<Registry<MapCodec<? extends Modifier>>> MODIFIER_TYPE = createRegistryKey("modifier_type");
	public static final ResourceKey<Registry<MapCodec<? extends TerrainRule>>> TERRAIN_RULE_TYPE = createRegistryKey("terrain_rule_type");
	public static final ResourceKey<Registry<MapCodec<? extends TerrainRule.Condition>>> TERRAIN_CONDITION_TYPE = createRegistryKey("terrain_condition_type");

	private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(LithostitchedCommon.MOD_ID, name));
	}
}
