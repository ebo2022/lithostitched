package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

record VectorFunctionHolderHolder(Holder<VectorFunction> function) implements VectorFunction {
    @Override
    public Vec3 compute(DensityFunction.FunctionContext context) {
        return this.function.value().compute(context);
    }

    @Override
    public void fillArray(Vec3[] output, ContextProvider provider) {
        this.function.value().fillArray(output, provider);
    }

    @Override
    public VectorFunction mapAll(VectorFunction.Visitor visitor) {
        return visitor.visit(new VectorFunctionHolderHolder(new Holder.Direct<>(this.function.value().mapAll(visitor))));
    }

    @Override
    public double minX() {
        return this.function.isBound() ? this.function.value().minX() : Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxX() {
        return this.function.isBound() ? this.function.value().maxX() : Double.POSITIVE_INFINITY;
    }

    @Override
    public double minY() {
        return this.function.isBound() ? this.function.value().minY() : Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxY() {
        return this.function.isBound() ? this.function.value().maxY() : Double.POSITIVE_INFINITY;
    }

    @Override
    public double minZ() {
        return this.function.isBound() ? this.function.value().minZ() : Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxZ() {
        return this.function.isBound() ? this.function.value().maxZ() : Double.POSITIVE_INFINITY;
    }

    @Override
    public MapCodec<VectorFunctionHolderHolder> codec() {
        throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
    }
}