package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

record VectorFunctionHolderHolder(Holder<VectorFunction> function) implements VectorFunction {
    @Override
    public Vec3 compute(FunctionContext context) {
        return this.function.value().compute(context);
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        this.function.value().fillArray(array, provider);
    }

    @Override
    public VectorFunction mapAll(VectorFunction.Visitor visitor) {
        return visitor.apply(new VectorFunctionHolderHolder(new Holder.Direct<>(this.function.value().mapAll(visitor))));
    }

    @Override
    public MapCodec<VectorFunctionHolderHolder> codec() {
        throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
    }
}