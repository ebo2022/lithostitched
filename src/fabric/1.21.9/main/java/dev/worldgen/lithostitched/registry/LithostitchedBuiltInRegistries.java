package dev.worldgen.lithostitched.registry;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.bandlands.band.Band;
import dev.worldgen.lithostitched.worldgen.modifier.Modifier;
import dev.worldgen.lithostitched.worldgen.placementcondition.PlacementCondition;
import dev.worldgen.lithostitched.worldgen.processor.condition.ProcessorCondition;
import dev.worldgen.lithostitched.worldgen.vectorfunction.VectorFunction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.WritableRegistry;

/**
 * Built-in registries for Lithostitched on Fabric.
 *
 * @author SmellyModder (Luke Tonon)
 */
public interface LithostitchedBuiltInRegistries {
	WritableRegistry<MapCodec<? extends Modifier>> MODIFIER_TYPE = FabricRegistryBuilder.createSimple(LithostitchedRegistryKeys.MODIFIER_TYPE).buildAndRegister();
	WritableRegistry<MapCodec<? extends PlacementCondition>> PLACEMENT_CONDITION_TYPE = FabricRegistryBuilder.createSimple(LithostitchedRegistryKeys.PLACEMENT_CONDITION_TYPE).buildAndRegister();
	WritableRegistry<MapCodec<? extends ProcessorCondition>> PROCESSOR_CONDITION_TYPE = FabricRegistryBuilder.createSimple(LithostitchedRegistryKeys.PROCESSOR_CONDITION_TYPE).buildAndRegister();
	WritableRegistry<MapCodec<? extends Band>> BANDLANDS_BAND_TYPE = FabricRegistryBuilder.createSimple(LithostitchedRegistryKeys.BANDLANDS_BAND_TYPE).buildAndRegister();
	WritableRegistry<MapCodec<? extends VectorFunction>> VECTOR_FUNCTION_TYPE = FabricRegistryBuilder.createSimple(LithostitchedRegistryKeys.VECTOR_FUNCTION_TYPE).buildAndRegister();

	static void init() {

	}
}
