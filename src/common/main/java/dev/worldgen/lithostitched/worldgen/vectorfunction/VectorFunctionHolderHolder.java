package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.phys.Vec3;

record VectorFunctionHolderHolder(Holder<VectorFunction> function) implements VectorFunction {
    @Override
    public Vec3 compute(FunctionContext context) {
        return this.function.value().compute(context);
    }

    @Override
    public void fillArray(Vec3[] vec3s, ContextProvider provider) {
        this.function.value().fillArray(vec3s, provider);
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