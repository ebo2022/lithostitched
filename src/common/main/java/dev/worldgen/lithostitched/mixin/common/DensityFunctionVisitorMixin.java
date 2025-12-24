package dev.worldgen.lithostitched.mixin.common;

import dev.worldgen.lithostitched.worldgen.vectorfunction.VectorFunction;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;

// mixin to support density functions with vector inputs
@Mixin(DensityFunction.Visitor.class)
public interface DensityFunctionVisitorMixin extends VectorFunction.Visitor {
}
