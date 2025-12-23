package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.phys.Vec3;

public record AddVectorFunction(VectorFunction argument1, VectorFunction argument2) implements VectorFunction {
    public static final MapCodec<AddVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, AddVectorFunction::new, AddVectorFunction::argument1, AddVectorFunction::argument2);

    @Override
    public Vec3 compute(FunctionContext context) {
        return this.argument1.compute(context).add(this.argument2.compute(context));
    }

    @Override
    public void fillArray(Vec3[] array, ContextProvider provider) {
        this.argument1.fillArray(array, provider);

        Vec3[] array2 = new Vec3[array.length];
        this.argument2.fillArray(array2, provider);

        for (int i = 0; i < array.length; ++i) {
            array[i] = array[i].add(array2[i]);
        }
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return new AddVectorFunction(visitor.apply(this.argument1), visitor.apply(this.argument2));
    }

    @Override
    public MapCodec<AddVectorFunction> codec() {
        return null;
    }
}
